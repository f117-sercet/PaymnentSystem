package com.pay.payment.model;

import com.pay.pay.core.entity.PayOrder;
import com.pay.payment.msg.ChannelRetMsg;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
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
 * @aate: Created in 2023/11/14 11:22
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

        return  processOrder(token, payOrder,false);
    }
    public List<String> processOrder(String order) {
        return processOrder(order, "null");
    }

    public List<String> processOrder(String order, String afterOrderId) {
        String ppOrderId = "null";
        String ppCatptId = "null";
        if (order != null) {
            if (order.contains(",")) {
                String[] split = order.split(",");
                if (split.length == 2) {
                    ppCatptId = split[1];
                    ppOrderId = split[0];
                }
            }
        }
        if (afterOrderId != null && !"null".equalsIgnoreCase(afterOrderId)) {
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
     * 由于 Paypal 创建订单后需要进行一次 Capture(捕获) 才可以正确获取到订单的支付状态
     * @param token
     * @param payOrder
     * @param isCapture
     * @return
     * @throws IOException
     */
    public ChannelRetMsg processOrder(String token, PayOrder payOrder, boolean isCapture) throws IOException{

        // 退款需要用到
        String ppOrderId = this.processOrder(payOrder.getChannelOrderNo(), token).get(0);
        String ppCatptId = this.processOrder(payOrder.getChannelOrderNo()).get(1);


        ChannelRetMsg channelRetMsg = ChannelRetMsg.waiting();
        channelRetMsg.setResponseEntity(textResp("ERROR"));
    }

    protected ResponseEntity textResp(String text) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity(text, httpHeaders, HttpStatus.OK);

    }
}
