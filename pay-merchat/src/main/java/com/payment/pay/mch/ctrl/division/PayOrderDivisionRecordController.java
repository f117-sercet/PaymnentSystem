package com.payment.pay.mch.ctrl.division;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.components.mq.model.PayOrderDivisionMQ;
import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.PayOrderDivisionRecord;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.PayOrderDivisionRecordService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * 分账记录
 *
 * @author: 段世超
 * @aate: Created in 2023/1/29 9:45
 */
public class PayOrderDivisionRecordController extends CommonCtrl {

    @Resource
    private PayOrderDivisionRecordService payOrderDivisionRecordService;
    @Resource
    private IMQSender mqSender;

    /** list */
    @PreAuthorize("hasAnyAuthority( 'ENT_DIVISION_RECORD_LIST' )")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list() {

        PayOrderDivisionRecord queryObject = getObject(PayOrderDivisionRecord.class);
        JSONObject paramJSON = getReqParamJSON();

        LambdaQueryWrapper<PayOrderDivisionRecord> condition = PayOrderDivisionRecord.gw();
        condition.eq(PayOrderDivisionRecord::getMchNo, getCurrentMchNo());

        if(queryObject.getReceiverId() != null){
            condition.eq(PayOrderDivisionRecord::getReceiverId, queryObject.getReceiverId());
        }

        if(queryObject.getReceiverGroupId() != null){
            condition.eq(PayOrderDivisionRecord::getReceiverGroupId, queryObject.getReceiverGroupId());
        }

        if(StringUtils.isNotEmpty(queryObject.getAppId())){
            condition.like(PayOrderDivisionRecord::getAppId, queryObject.getAppId());
        }

        if(queryObject.getState() != null){
            condition.eq(PayOrderDivisionRecord::getState, queryObject.getState());
        }

        if(StringUtils.isNotEmpty(queryObject.getPayOrderId())){
            condition.eq(PayOrderDivisionRecord::getPayOrderId, queryObject.getPayOrderId());
        }

        if(StringUtils.isNotEmpty(queryObject.getAccNo())){
            condition.eq(PayOrderDivisionRecord::getAccNo, queryObject.getAccNo());
        }

        if (paramJSON != null) {
            if (StringUtils.isNotEmpty(paramJSON.getString("createdStart"))) {
                condition.ge(PayOrderDivisionRecord::getCreatedAt, paramJSON.getString("createdStart"));
            }
            if (StringUtils.isNotEmpty(paramJSON.getString("createdEnd"))) {
                condition.le(PayOrderDivisionRecord::getCreatedAt, paramJSON.getString("createdEnd"));
            }
        }

        condition.orderByDesc(PayOrderDivisionRecord::getCreatedAt); //时间倒序

        IPage<PayOrderDivisionRecord> pages = payOrderDivisionRecordService.page(getIPage(true), condition);
        return ApiRes.page(pages);
    }


    /** detail */
    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECORD_VIEW' )")
    @RequestMapping(value="/{recordId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("recordId") Long recordId) {
        PayOrderDivisionRecord record = payOrderDivisionRecordService
                .getOne(PayOrderDivisionRecord.gw()
                        .eq(PayOrderDivisionRecord::getMchNo, getCurrentMchNo())
                        .eq(PayOrderDivisionRecord::getRecordId, recordId));
        if (record == null) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        return ApiRes.ok(record);
    }

    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECORD_RESEND' )")
    @RequestMapping(value="/resend/{recordId}", method = RequestMethod.POST)
    public ApiRes resend(@PathVariable("recordId") Long recordId){

        PayOrderDivisionRecord record = payOrderDivisionRecordService.getOne(PayOrderDivisionRecord.gw()
                .eq(PayOrderDivisionRecord::getMchNo, getCurrentMchNo())
                .eq(PayOrderDivisionRecord::getRecordId, recordId));
        if (record == null) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_DELETE);
        }
        if (record.getState() != PayOrderDivisionRecord.STATE_FAIL) {
            throw new BizException("请选择失败的分账记录");
        }
        // 更新订单状态 & 记录状态
        payOrderDivisionRecordService.updateResendState(record.getPayOrderId());

        // 重发到MQ
        mqSender.send(PayOrderDivisionMQ.build(record.getPayOrderId(), null, null, true));
        return ApiRes.ok(record);
    }
}
