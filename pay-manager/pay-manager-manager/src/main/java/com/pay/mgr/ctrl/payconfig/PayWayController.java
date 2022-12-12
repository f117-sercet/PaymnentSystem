package com.pay.mgr.ctrl.payconfig;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.MchPayPassage;
import com.pay.pay.core.entity.PayOrder;
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

    /**
     * 更新支付方式
     * @param wayCode
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PC_WAY_EDIT')")
    @PutMapping("/{wayCode}")
    @MethodLog(remark = "更新支付方式")
    public ApiRes update(@PathVariable("wayCode") String wayCode) {
        PayWay payWay = getObject(PayWay.class);
        payWay.setWayCode(wayCode);
        boolean result = payWayService.updateById(payWay);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_UPDATE);
        }
        return ApiRes.ok();
    }

    @DeleteMapping("/{wayCode}")
    @MethodLog(remark = "删除支付方式")
    public ApiRes delete(@PathVariable("wayCode") String wayCode) {

        // 校验该支付方式是否有商户已配置通道或者已有订单
        if (mchPayPassageService.count(MchPayPassage.gw().eq(MchPayPassage::getWayCode, wayCode)) > 0
                || payOrderService.count(PayOrder.gw().eq(PayOrder::getWayCode, wayCode)) > 0) {
            throw new BizException("该支付方式已有商户配置通道或已发生交易，无法删除！");
        }

        boolean result = payWayService.removeById(wayCode);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_DELETE);
        }
        return ApiRes.ok();
    }


}
