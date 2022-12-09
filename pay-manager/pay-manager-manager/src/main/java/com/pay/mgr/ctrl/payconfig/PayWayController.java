package com.pay.mgr.ctrl.payconfig;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.entity.PayWay;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchPayPassageService;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.pay.service.impl.PayWayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 支付方式管理类
 *
 * @author: 段世超
 * @aate: Created in 2022/12/9 17:59
 */
@RestController
@RequestMapping("api/payWays")
public class PayWayController extends CommonCtrl {
    @Autowired
    PayWayService payWayService;
    @Autowired
    MchPayPassageService mchPayPassageService;
    @Autowired
    PayOrderService payOrderService;

    /**
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ENT_PC_WAY_LIST', 'ENT_PAY_ORDER_SEARCH_PAY_WAY')")
    @GetMapping
    public ApiRes list() {

        PayWay queryObject = getObject(PayWay.class);
        PayWay queryObject = getObject(PayWay.class);

        LambdaQueryWrapper<PayWay> condition = PayWay.gw();
        if(StringUtils.isNotEmpty(queryObject.getWayCode())){
            condition.like(PayWay::getWayCode, queryObject.getWayCode());
        }
        if(StringUtils.isNotEmpty(queryObject.getWayName())){
            condition.like(PayWay::getWayName, queryObject.getWayName());
        }
        condition.orderByAsc(PayWay::getWayCode);

        IPage<PayWay> pages = payWayService.page(getIPage(true), condition);

        return ApiRes.page(pages);
    }
}
