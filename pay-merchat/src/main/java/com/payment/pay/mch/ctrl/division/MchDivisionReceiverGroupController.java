package com.payment.pay.mch.ctrl.division;

import com.pay.pay.service.impl.MchDivisionReceiverGroupService;
import com.pay.pay.service.impl.MchDivisionReceiverService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description： 商户分账接收者账号组
 *
 * @author: 段世超
 * @aate: Created in 2023/1/28 17:54
 */
@RestController
@RequestMapping("api/divisionReceiverGroups")
public class MchDivisionReceiverGroupController extends CommonCtrl {

    @Resource
    private MchDivisionReceiverGroupService mchDivisionReceiverGroupService;
    @Resource
    private MchDivisionReceiverService mchDivisionReceiverService;
}
