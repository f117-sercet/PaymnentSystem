package com.payment.pay.mch.ctrl.paytest;

import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.MchPayPassageService;
import com.pay.pay.service.impl.SysConfigService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description： 支付测试类
 *
 * @author: 段世超
 * @aate: Created in 2023/2/10 17:34
 */
@RestController
@RequestMapping("/api/paytest")
public class payTestController extends CommonCtrl {

    @Resource
    private MchAppService mchAppService;
    @Resource
    private MchPayPassageService mchPayPassageService;
    @Resource
    private SysConfigService sysConfigService;
}
