package com.payment.pay.mch.ctrl.order;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.core.entity.PayWay;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.pay.service.impl.PayWayService;
import com.pay.pay.service.impl.SysConfigService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description： 支付订单管理类
 *
 * @author: 段世超
 * @aate: Created in 2023/2/2 10:22
 */
@RestController
@RequestMapping("/api/payOrder")
public class PayOrderController extends CommonCtrl {

    @Resource
    private PayOrderService payOrderService;
    @Resource
    private PayWayService payWayService;
    @Resource
    private MchAppService mchAppService;
    @Resource
    private SysConfigService sysConfigService;

    @PreAuthorize("hasAuthority('ENT_ORDER_LIST')")
    @GetMapping
    public ApiRes list(){
        PayOrder payOrder = getObject(PayOrder.class);
        JSONObject paramJSON = getReqParamJSON();

        LambdaQueryWrapper<PayOrder> wrapper = PayOrder.gw();
        wrapper.eq(PayOrder::getMchNo, getCurrentMchNo());

        IPage<PayOrder> pages = payOrderService.listByPage(getIPage(), payOrder, paramJSON, wrapper);

        // 得到所有支付方式
        Map<String, String> payWayNameMap = new HashMap<>();
        List<PayWay> payWayList = payWayService.list();
        if (!CollectionUtils.isEmpty(payWayList)) {
            for (PayWay payWay:payWayList) {
                payWayNameMap.put(payWay.getWayCode(), payWay.getWayName());
            }
            for (PayOrder order:pages.getRecords()) {
                // 存入支付方式名称
                if (StringUtils.isNotEmpty(payWayNameMap.get(order.getWayCode()))) {
                    order.addExt("wayName", payWayNameMap.get(order.getWayCode()));
                }else {
                    order.addExt("wayName", order.getWayCode());
                }
            }
        }

        return ApiRes.page(pages);

    }

    /**
     * 订单支付信息
     * @param payOrderId
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PAY_ORDER_VIEW')")
    @GetMapping("/{payOrderId}")
    public ApiRes detail(@PathVariable("payOrderId") String payOrderId) {
        PayOrder payOrder = payOrderService.getById(payOrderId);
        if (payOrder == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        if (!payOrder.getMchNo().equals(getCurrentMchNo())) {
            return ApiRes.fail(ApiCodeEnum.SYS_PERMISSION_ERROR);
        }
        return ApiRes.ok(payOrder);
    }
}
