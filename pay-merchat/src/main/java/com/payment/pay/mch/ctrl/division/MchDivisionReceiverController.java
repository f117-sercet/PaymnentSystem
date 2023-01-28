package com.payment.pay.mch.ctrl.division;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.JeepayClient;
import com.jeequan.jeepay.exception.JeepayException;
import com.jeequan.jeepay.model.DivisionReceiverBindReqModel;
import com.jeequan.jeepay.request.DivisionReceiverBindRequest;
import com.jeequan.jeepay.response.DivisionReceiverBindResponse;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.entity.MchDivisionReceiver;
import com.pay.pay.core.entity.MchDivisionReceiverGroup;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.MchDivisionReceiverGroupService;
import com.pay.pay.service.impl.MchDivisionReceiverService;
import com.pay.pay.service.impl.SysConfigService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Description： 商户分账接收者账号关系维护
 *
 * @author: 段世超
 * @aate: Created in 2023/1/21 15:17
 */
@RestController
@RequestMapping("api/divisionReceivers")
public class MchDivisionReceiverController extends CommonCtrl {

    @Resource
    private MchDivisionReceiverService mchDivisionReceiverService;
    @Resource
    private MchDivisionReceiverGroupService mchDivisionReceiverGroupService;
    @Resource
    private MchAppService mchAppService;
    @Resource
    private SysConfigService sysConfigService;

    @PreAuthorize("hasAnyAuthority( 'ENT_DIVISION_RECEIVER_LIST' )")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list(){

        MchDivisionReceiver queryObject = getObject(MchDivisionReceiver.class);

        LambdaQueryWrapper<MchDivisionReceiver> condition = MchDivisionReceiver.gw();
        condition.eq(MchDivisionReceiver::getMchNo, getCurrentMchNo());

        if(queryObject.getReceiverId() != null){
            condition.eq(MchDivisionReceiver::getReceiverId, queryObject.getReceiverId());
        }

        if(StringUtils.isNotEmpty(queryObject.getReceiverAlias())){
            condition.like(MchDivisionReceiver::getReceiverAlias, queryObject.getReceiverAlias());
        }

        if(queryObject.getReceiverGroupId() != null){
            condition.eq(MchDivisionReceiver::getReceiverGroupId, queryObject.getReceiverGroupId());
        }

        if(StringUtils.isNotEmpty(queryObject.getReceiverGroupName())){
            condition.like(MchDivisionReceiver::getReceiverGroupName, queryObject.getReceiverGroupName());
        }

        if(StringUtils.isNotEmpty(queryObject.getAppId())){
            condition.like(MchDivisionReceiver::getAppId, queryObject.getAppId());
        }

        if(queryObject.getState() != null){
            condition.eq(MchDivisionReceiver::getState, queryObject.getState());
        }

        condition.orderByDesc(MchDivisionReceiver::getCreatedAt); //时间倒序

        IPage<MchDivisionReceiver> pages = mchDivisionReceiverService.page(getIPage(true), condition);
        return ApiRes.page(pages);
    }

    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECEIVER_VIEW' )")
    @RequestMapping(value="/{recordId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("recordId") Long recordId){
        MchDivisionReceiver record = mchDivisionReceiverService
                .getOne(MchDivisionReceiver.gw()
                        .eq(MchDivisionReceiver::getMchNo, getCurrentMchNo())
                        .eq(MchDivisionReceiver::getReceiverId, recordId));

        if (record == null) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        return ApiRes.ok(record);
    }
    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECEIVER_ADD' )")
    @RequestMapping(value = "",method = RequestMethod.POST)
    @MethodLog(remark = "新增分账接收账号")
    public ApiRes add(){

        DivisionReceiverBindReqModel model = getObject(DivisionReceiverBindReqModel.class);
        MchApp mchApp = mchAppService.getById(model.getAppId());

        if (mchApp == null || mchApp.getState() != CS.PUB_USABLE || !mchApp.getMchNo().equals(getCurrentMchNo())) {
            throw new BizException("商户应用不存在或不可用");
        }
        DivisionReceiverBindRequest request = new DivisionReceiverBindRequest();
        request.setBizModel(model);
        model.setMchNo(this.getCurrentMchNo());
        model.setDivisionProfit(new BigDecimal(model.getDivisionProfit()).divide(new BigDecimal(100)).toString());
        JeepayClient jeepayClient = new JeepayClient(sysConfigService.getDBApplicationConfig().getPaySiteUrl(), mchApp.getAppSecret());

        try {
            DivisionReceiverBindResponse response = jeepayClient.execute(request);
            if(response.getCode() != 0){
                throw new BizException(response.getMsg());
            }
            return ApiRes.ok(response.get());
        } catch (JeepayException e) {
            throw new BizException(e.getMessage());
        }
    }
    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECEIVER_EDIT' )")
    @RequestMapping(value="/{recordId}", method = RequestMethod.PUT)
    @MethodLog(remark = "更新分账接收账号")
    public ApiRes update(@PathVariable("recordId") Long recordId){

        MchDivisionReceiver reqReceiver = getObject(MchDivisionReceiver.class);
        MchDivisionReceiver record = new MchDivisionReceiver();
        record.setReceiverAlias(reqReceiver.getReceiverAlias());
        record.setReceiverGroupId(reqReceiver.getReceiverGroupId());
        record.setState(reqReceiver.getState());
        if (reqReceiver.getDivisionProfit()!=null) {
            record.setDivisionProfit(reqReceiver.getDivisionProfit().divide(new BigDecimal(100)));
        }
        if(record.getReceiverGroupId() != null){
            MchDivisionReceiverGroup groupRecord = mchDivisionReceiverGroupService.findByIdAndMchNo(record.getReceiverGroupId(), getCurrentMchNo());
            if (record == null) {
                throw new BizException("账号组不存在");
            }
            record.setReceiverGroupId(groupRecord.getReceiverGroupId());
            record.setReceiverGroupName(groupRecord.getReceiverGroupName());
        }

        LambdaUpdateWrapper<MchDivisionReceiver> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MchDivisionReceiver::getReceiverId, recordId);
        updateWrapper.eq(MchDivisionReceiver::getMchNo, getCurrentMchNo());
        mchDivisionReceiverService.update(record, updateWrapper);
        return ApiRes.ok();
    }
    /** delete */
    @PreAuthorize("hasAuthority('ENT_DIVISION_RECEIVER_DELETE')")
    @RequestMapping(value="/{recordId}", method = RequestMethod.DELETE)
    @MethodLog(remark = "删除分账接收账号")
    public ApiRes del(@PathVariable("recordId") Long recordId) {
        MchDivisionReceiver record = mchDivisionReceiverService.getOne(MchDivisionReceiver.gw()
                .eq(MchDivisionReceiver::getReceiverGroupId, recordId).eq(MchDivisionReceiver::getMchNo, getCurrentMchNo()));
        if (record == null) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        mchDivisionReceiverService.removeById(recordId);
        return ApiRes.ok();
    }
}

