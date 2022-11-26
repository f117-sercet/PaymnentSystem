package com.pay.mgr.ctrl.order;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.JeepayClient;
import com.jeequan.jeepay.exception.JeepayException;
import com.jeequan.jeepay.model.RefundOrderCreateReqModel;
import com.jeequan.jeepay.request.RefundOrderCreateRequest;
import com.jeequan.jeepay.response.RefundOrderCreateResponse;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.core.entity.PayWay;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.utils.SeqKit;
import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.pay.service.impl.PayWayService;
import com.pay.pay.service.impl.SysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description： 支付订单
 *
 * @author: 段世超
 * @aate: Created in 2022/11/26 17:32
 */
@RestController
@RequestMapping("/api/payOrder")
public class PayOrderController extends CommonCtrl {
    @Autowired
    private PayOrderService payOrderService;
    @Autowired private PayWayService payWayService;
    @Autowired private SysConfigService sysConfigService;
    @Autowired private MchAppService mchAppService;

    /**
     * 订单信息列表
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_ORDER_LIST')")
    @RequestMapping(value="list", method = RequestMethod.GET)
    public ApiRes list(){

        PayOrder payOrder = getObject(PayOrder.class);
        JSONObject paramJSON = getReqParamJSON();
        LambdaQueryWrapper<PayOrder> wrapper = PayOrder.gw();
        IPage<PayOrder> pages = payOrderService.listByPage(getIPage(), payOrder, paramJSON, wrapper);

        // 得到所有支付
        Map<String,String> payWayNameMap = new HashMap<String,String>();
        List<PayWay> payWayList = payWayService.list();
        for (PayWay payWay : payWayList) {
            payWayNameMap.put(payWay.getWayCode(),payWay.getWayName());
        }
        for (PayOrder order:pages.getRecords()) {
            // 存入支付方式名称
            if (StringUtils.isNotEmpty(payWayNameMap.get(order.getWayCode()))) {
                order.addExt("wayName", payWayNameMap.get(order.getWayCode()));
            }else {
                order.addExt("wayName", order.getWayCode());
            }
        }
        return ApiRes.page(pages);
        }

    /**
     * 支付订单信息
      * @param payOrderId
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PAY_ORDER_VIEW')")
    @RequestMapping(value="/{payOrderId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("payOrderId") String payOrderId){
        PayOrder payOrder = payOrderService.getById(payOrderId);
        if (payOrder == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        return ApiRes.ok(payOrder);
    }

    @MethodLog(remark = "发起订单退款")
    @PreAuthorize("hasAuthority('ENT_PAY_ORDER_REFUND')")
    @PostMapping("/refunds/{payOrderId}")
    public ApiRes refund(@PathVariable("payOrderId") String payOrderId){

        Long refundAmount = getRequiredAmountL("refundAmount");
        String refundReason = getValStringRequired("refundReason");

        PayOrder payOrder = payOrderService.getById(payOrderId);
        if (payOrder == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        if(payOrder.getState() != PayOrder.STATE_SUCCESS){
            throw new BizException("订单状态不正确");
        }

        if(payOrder.getRefundAmount() + refundAmount > payOrder.getAmount()){
            throw new BizException("退款金额超过订单可退款金额！");
        }


        RefundOrderCreateRequest request = new RefundOrderCreateRequest();
        RefundOrderCreateReqModel model = new RefundOrderCreateReqModel();
        request.setBizModel(model);

        model.setMchNo(payOrder.getMchNo());     // 商户号
        model.setAppId(payOrder.getAppId());
        model.setPayOrderId(payOrderId);
        model.setMchRefundNo(SeqKit.genMhoOrderId());
        model.setRefundAmount(refundAmount);
        model.setRefundReason(refundReason);
        model.setCurrency("CNY");

        MchApp mchApp = mchAppService.getById(payOrder.getAppId());

        JeepayClient jeepayClient = new JeepayClient(sysConfigService.getDBApplicationConfig().getPaySiteUrl(), mchApp.getAppSecret());

        try {
            RefundOrderCreateResponse response = jeepayClient.execute(request);
            if(response.getCode() != 0){
                throw new BizException(response.getMsg());
            }
            return ApiRes.ok(response.get());
        } catch (JeepayException e) {
            throw new BizException(e.getMessage());
        }
    }

}
