package com.pay.mgr.ctrl.payconfig;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.PayWay;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchPayPassageService;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.pay.service.impl.PayWayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 细节
     * @param wayCode
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ENT_PC_WAY_VIEW', 'ENT_PC_WAY_EDIT')")
    @GetMapping("/{wayCode}")
    public ApiRes detail(@PathVariable("wayCode") String wayCode) {
        return ApiRes.ok(payWayService.getById(wayCode));
    }

    /**
     * 新增支付方式
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PC_WAY_ADD')")
    @PostMapping
    @MethodLog(remark = "新增支付方式")
    public ApiRes add() {
        PayWay payWay = getObject(PayWay.class);

        if (payWayService.count(PayWay.gw().eq(PayWay::getWayCode, payWay.getWayCode())) > 0) {
            throw new BizException("支付方式代码已存在");
        }
        payWay.setWayCode(payWay.getWayCode().toUpperCase());

        boolean result = payWayService.save(payWay);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_CREATE);
        }
        return ApiRes.ok();
    }
}
