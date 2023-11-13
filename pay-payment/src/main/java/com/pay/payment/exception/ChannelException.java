package com.pay.payment.exception;

import com.pay.payment.msg.ChannelRetMsg;
import lombok.Getter;

/**
 * Description： 请求渠道侧异常 exception
 * * 抛出此异常： 仅支持：  未知状态（需查单） 和 系统内异常
 *
 * @author: 段世超
 * @aate: Created in 2023/11/13 18:04
 */
@Getter
public class ChannelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ChannelRetMsg channelRetMsg;

    /** 业务自定义异常 **/
    private ChannelException(ChannelRetMsg channelRetMsg) {
        super(channelRetMsg != null ? channelRetMsg.getChannelErrMsg() : null);
        this.channelRetMsg = channelRetMsg;
    }

    /** 未知状态 **/
    public static ChannelException unknown(String channelErrMsg){
        return new ChannelException(ChannelRetMsg.unknown(channelErrMsg));
    }

    /** 系统内异常 **/
    public static ChannelException sysError(String channelErrMsg) {
        return new ChannelException(ChannelRetMsg.sysError(channelErrMsg));
    }

}
