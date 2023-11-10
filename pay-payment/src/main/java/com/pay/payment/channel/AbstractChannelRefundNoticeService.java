package com.pay.payment.channel;

import com.pay.pay.core.beans.RequestKitBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 退款类抽象
 *
 * @author: 段世超
 * @aate: Created in 2023/11/10 17:52
 */
public abstract class AbstractChannelRefundNoticeService implements  IChannelNoticeService {

    @Autowired
    private RequestKitBean requestKitBean;
    @Autowired private ChannelCertConfigKitBean channelCertConfigKitBean;
    @Autowired protected ConfigContextQueryService configContextQueryService;

    @Override
    public ResponseEntity doNotifyOrderNotExists(HttpServletRequest request) {
            return textResp("order not exists");
        }
    @Override
    public ResponseEntity doNotifyOrderStateUpdateFail(HttpServletRequest request) {
        return textResp("update status error");
    }

    protected ResponseEntity textResp(String  text) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return  new ResponseEntity(text, httpHeaders, HttpStatus.OK);
    }




}
