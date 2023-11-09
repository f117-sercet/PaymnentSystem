package com.pay.mgr.ctrl.order;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.components.mq.model.PayOrderMchNotifyMQ;
import com.pay.components.mq.vender.IMQSender;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.MchNotifyRecord;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.payMbg.service.impl.MchNotifyRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 商户通知类
 *
 * @author: 段世超
 * @aate: Created in 2022/11/25 14:53
 */
@RestController
@RequestMapping("/api/mchNotify")
public class MchNotifyController  extends CommonCtrl {

    @Autowired
    private MchNotifyRecordService mchNotifyService;
    @Autowired private IMQSender mqSender;

    /**
     * 商户通知列表
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_NOTIFY_LIST')")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list() {

        MchNotifyRecord mchNotify = getObject(MchNotifyRecord.class);

        JSONObject paramJSON = getReqParamJSON();
        LambdaQueryWrapper<MchNotifyRecord> wrapper = MchNotifyRecord.gw();
        if (StringUtils.isNotEmpty(mchNotify.getOrderId())) {
            wrapper.eq(MchNotifyRecord::getOrderId, mchNotify.getOrderId());
        }
        if (StringUtils.isNotEmpty(mchNotify.getMchNo())) {
            wrapper.eq(MchNotifyRecord::getMchNo, mchNotify.getMchNo());
        }
        if (StringUtils.isNotEmpty(mchNotify.getIsvNo())) {
            wrapper.eq(MchNotifyRecord::getIsvNo, mchNotify.getIsvNo());
        }
        if (StringUtils.isNotEmpty(mchNotify.getMchOrderNo())) {
            wrapper.eq(MchNotifyRecord::getMchOrderNo, mchNotify.getMchOrderNo());
        }
        if (mchNotify.getOrderType() != null) {
            wrapper.eq(MchNotifyRecord::getOrderType, mchNotify.getOrderType());
        }
        if (mchNotify.getState() != null) {
            wrapper.eq(MchNotifyRecord::getState, mchNotify.getState());
        }
        if (StringUtils.isNotEmpty(mchNotify.getAppId())) {
            wrapper.eq(MchNotifyRecord::getAppId, mchNotify.getAppId());
        }


            if (paramJSON != null) {
                if (StringUtils.isNotEmpty(paramJSON.getString("createdStart"))) {
                    wrapper.ge(MchNotifyRecord::getCreatedAt, paramJSON.getString("createdStart"));
                }
                if (StringUtils.isNotEmpty(paramJSON.getString("createdEnd"))) {
                    wrapper.le(MchNotifyRecord::getCreatedAt, paramJSON.getString("createdEnd"));
                }
            }
        wrapper.orderByDesc(MchNotifyRecord::getCreatedAt);
        IPage<MchNotifyRecord> pages = mchNotifyService.page(getIPage(), wrapper);

        return ApiRes.page(pages);
        }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:14
     * @describe: 商户通知信息
     */
    @PreAuthorize("hasAuthority('ENT_MCH_NOTIFY_VIEW')")
    @RequestMapping(value="/{notifyId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("notifyId") String notifyId) {
        MchNotifyRecord mchNotify = mchNotifyService.getById(notifyId);
        if (mchNotify == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        return ApiRes.ok(mchNotify);
    }

    /*
     * 功能描述: 商户通知重发操作
     * @Author: terrfly
     * @Date: 2021/6/21 17:41
     */
    @PreAuthorize("hasAuthority('ENT_MCH_NOTIFY_RESEND')")
    @RequestMapping(value="resend/{notifyId}", method = RequestMethod.POST)
    public ApiRes resend(@PathVariable("notifyId") Long notifyId) {

        MchNotifyRecord mchNotify = mchNotifyService.getById(notifyId);
        if (mchNotify == null){

            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        if (mchNotify.getState() !=  MchNotifyRecord.STATE_FAIL){

            throw new BizException("请选择失败的通知记录");
        }

        //更新通知中
        mchNotifyService.getBaseMapper().updateIngAndAddNotifyCountLimit(notifyId);

        //调起MQ重发
        mqSender.send(PayOrderMchNotifyMQ.build(notifyId));

        return ApiRes.ok(mchNotify);

    }
}
