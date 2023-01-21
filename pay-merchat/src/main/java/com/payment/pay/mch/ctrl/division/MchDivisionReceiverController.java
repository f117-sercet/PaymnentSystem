package com.payment.pay.mch.ctrl.division;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.MchDivisionReceiver;
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
    }
