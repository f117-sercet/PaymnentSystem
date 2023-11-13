package com.pay.payment.model;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.pay.pay.core.constants.CS;
import com.pay.payment.exception.ChannelException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2023/11/13 17:59
 */
@Slf4j
@Data
@AllArgsConstructor
public class AlipayClientWrapper {

    //默认为 不使用证书方式
    private Byte useCert = CS.NO;

    /** 缓存支付宝client 对象 **/
    private AlipayClient alipayClient;

    public <T extends AlipayResponse> T execute(AlipayRequest<T> request) {
        try {

            T alipayResp = null;
            if (useCert != null && useCert == CS.YES) { //证书加密方式

                alipayResp = alipayClient.certificateExecute(request);
            } else { // key 或者 空都为默认普通加密方式
                alipayResp = alipayClient.execute(request);

            }
            return alipayResp;

        } catch (AlipayApiException e) {
            log.error("调起支付宝execute[AlipayApiException]异常！", e);
            //如果数据返回出现验签异常，则需要抛出： UNKNOWN 异常。
            throw ChannelException.sysError(e.getMessage());

        } catch (Exception e) {
            log.error("调起支付宝execute[Exception]异常！", e);
            throw ChannelException.sysError("调用支付宝client服务异常");
        }
    }
}
