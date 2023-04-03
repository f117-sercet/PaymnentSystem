package com.pay.payment.model;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.core.model.params.pppay.PpPayNormalMchParams;
import com.pay.payment.rqrs.msg.ChannelRetMsg;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.http.serializer.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2023/3/30 15:03
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalWrapper {

    private PayPalEnvironment environment;
    private PayPalHttpClient client;

    private String notifyWebhook;
    private String refundWebhook;


    public ChannelRetMsg processOrder(String token, PayOrder payOrder) throws IOException {
        return processOrder(token, payOrder, false);
    }

    public List<String> processOrder(String order) {
        return processOrder(order, "null");
    }

    public List<String> processOrder(String order, String afterOrderId){

        String ppOrderId = "null";
        String  ppCatptId = "null";
        if (order != null) {
            if (order.contains(",")) {
                String[] split = order.split(",");
                if (split.length == 2) {
                    ppCatptId = split[1];
                    ppOrderId = split[0];
                }
            }
        }
        if (afterOrderId !=null && !"null".equalsIgnoreCase(afterOrderId)) {
            ppOrderId = afterOrderId;
        }
        if ("null".equalsIgnoreCase(ppCatptId)) {
            ppCatptId = null;
        }
        if ("null".equalsIgnoreCase(ppOrderId)) {
            ppOrderId = null;
        }

        return Arrays.asList(ppOrderId, ppCatptId);
    }

    /**
     * 处理并捕获订单
     * @param token
     * @param payOrder
     * @param isCapture
     * @return
     */
    public ChannelRetMsg processOrder(String token, PayOrder payOrder, boolean isCapture) throws IOException {

        // Paypal 创建订单存在一个 Token，当订单捕获之后会有一个 捕获的ID ，退款需要用到
        String ppOrderId = this.processOrder(payOrder.getChannelOrderNo(), token).get(0);
        String ppCatptId = this.processOrder(payOrder.getChannelOrderNo()).get(1);

        ChannelRetMsg channelRetMsg = ChannelRetMsg.waiting();
        channelRetMsg.setResponseEntity(textResp("ERROR"));

        // 如果订单 ID还不存在，等待

        if (ppOrderId == null) {
            channelRetMsg.setChannelErrCode("201");
            channelRetMsg.setChannelErrMsg("捕获订单请求失败");
            return channelRetMsg;
        }else {

            Order order;
            channelRetMsg.setChannelOrderId(ppOrderId+","+"null");

            // 如果捕获ID不存在
            if (ppCatptId == null && isCapture) {
                OrderRequest orderRequest = new OrderRequest();
                OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(ppOrderId);
                ordersCaptureRequest.requestBody(orderRequest);

                // 捕获订单
                HttpResponse<Order> response = this.getClient().execute(ordersCaptureRequest);

                if (response.statusCode()!=201) {
                    channelRetMsg.setChannelErrCode("201");
                    channelRetMsg.setChannelErrMsg("捕获订单请求失败");
                    return channelRetMsg;
                }
                order = response.result();
            }else {
                OrdersGetRequest request = new OrdersGetRequest(ppOrderId);
                HttpResponse<Order> response = this.getClient().execute(request);

                if (response.statusCode()!=200) {
                    channelRetMsg.setChannelOrderId(ppOrderId);
                    channelRetMsg.setChannelErrCode("200");
                    channelRetMsg.setChannelErrMsg("请求订单详情失败");
                    return  channelRetMsg;
                }
                order = response.result();
            }
            String status = order.status();
            String orderJsonStr = new Json().serialize(order);
            JSONObject orderJson = JSONUtil.parseObj(orderJsonStr);
            for (PurchaseUnit purchaseUnit : order.purchaseUnits()) {
                if (purchaseUnit.payments()!=null) {
                    for (Capture capture : purchaseUnit.payments().captures()) {
                        ppCatptId = capture.id();
                        break;
                    }
                }
            }
            String orderUserId = orderJson.getByPath("payer.payer_id", String.class);

            ChannelRetMsg result = new ChannelRetMsg();
            result.setNeedQuery(true);
            result.setChannelOrderId(ppOrderId + "," + ppCatptId); // 渠道订单号
            result.setChannelUserId(orderUserId);  // 支付用户ID
            result.setChannelAttach(orderJsonStr); // Capture 响应数据
            result.setResponseEntity(textResp("SUCCESS")); // 响应数据
            result.setChannelState(ChannelRetMsg.ChannelState.WAITING); // 默认支付中
            result = dispatchCode(status, result); // 处理状态码
            return result;
        }

    }

    private ChannelRetMsg dispatchCode(String status, ChannelRetMsg result) {

        return ChannelRetMsg.confirmFail();
    }

    private ResponseEntity textResp(String text) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity(text, httpHeaders, HttpStatus.OK);
    }
    public  static PaypalWrapper buildPaypalWrapper(PpPayNormalMchParams ppPayNormalMchParams){

        PaypalWrapper paypalWrapper = new PaypalWrapper();
        PayPalEnvironment environment = new PayPalEnvironment.Live(ppPayNormalMchParams.getClientId(), ppPayNormalMchParams.getSecret());
        if (ppPayNormalMchParams.getSandbox() ==1) {
            environment = new PayPalEnvironment.Sandbox(ppPayNormalMchParams.getClientId(), ppPayNormalMchParams.getSecret());
        }
        paypalWrapper.setEnvironment(environment);
        paypalWrapper.setClient(new PayPalHttpClient(environment));
        paypalWrapper.setNotifyWebhook(ppPayNormalMchParams.getNotifyWebhook());
        paypalWrapper.setRefundWebhook(ppPayNormalMchParams.getRefundWebhook());
        return paypalWrapper;

    }
}
