package com.pay.payment.model;

import com.pay.pay.core.entity.PayOrder;
import com.pay.payment.rqrs.msg.ChannelRetMsg;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

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
    }

}
