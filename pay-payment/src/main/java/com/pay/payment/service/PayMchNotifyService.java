package com.pay.payment.service;

import com.alibaba.fastjson.JSONObject;
import com.pay.components.mq.model.PayOrderMchNotifyMQ;
import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.entity.MchNotifyRecord;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.core.entity.RefundOrder;
import com.pay.pay.core.entity.TransferOrder;
import com.pay.pay.core.utils.JeepayKit;
import com.pay.pay.core.utils.StringKit;
import com.pay.pay.service.impl.MchNotifyRecordService;
import com.pay.payment.rqrs.payorder.QueryPayOrderRS;
import com.pay.payment.rqrs.refund.QueryRefundOrderRS;
import com.pay.payment.rqrs.transfer.QueryTransferOrderRS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

/**
 * Description：
 * 商户通知 service
 *
 * @author: 段世超
 * @create: Created in 2023/3/30 14:50
 */
@Slf4j
public class PayMchNotifyService {

    @Resource
    private MchNotifyRecordService mchNotifyRecordService;
    @Resource
    private ConfigContextQueryService configContextQueryService;
    @Resource
    private IMQSender mqSender;


    /** 商户通知信息， 只有订单是终态，才会发送通知， 如明确成功和明确失败 **/
    public void payOrderNotify(PayOrder dbPayOrder){

        try {
            // 通知地址为空
            if(org.apache.commons.lang3.StringUtils.isEmpty(dbPayOrder.getNotifyUrl())){
                return ;
            }

            //获取到通知对象
            MchNotifyRecord mchNotifyRecord = mchNotifyRecordService.findByPayOrder(dbPayOrder.getPayOrderId());

            if(mchNotifyRecord != null){

                log.info("当前已存在通知消息， 不再发送。");
                return ;
            }

            //商户app私钥
            String appSecret = configContextQueryService.queryMchApp(dbPayOrder.getMchNo(), dbPayOrder.getAppId()).getAppSecret();

            // 封装通知url
            String notifyUrl = createNotifyUrl(dbPayOrder, appSecret);
            mchNotifyRecord = new MchNotifyRecord();
            mchNotifyRecord.setOrderId(dbPayOrder.getPayOrderId());
            mchNotifyRecord.setOrderType(MchNotifyRecord.TYPE_PAY_ORDER);
            mchNotifyRecord.setMchNo(dbPayOrder.getMchNo());
            mchNotifyRecord.setMchOrderNo(dbPayOrder.getMchOrderNo()); //商户订单号
            mchNotifyRecord.setIsvNo(dbPayOrder.getIsvNo());
            mchNotifyRecord.setAppId(dbPayOrder.getAppId());
            mchNotifyRecord.setNotifyUrl(notifyUrl);
            mchNotifyRecord.setResResult("");
            mchNotifyRecord.setNotifyCount(0);
            mchNotifyRecord.setState(MchNotifyRecord.STATE_ING); // 通知中

            try {
                mchNotifyRecordService.save(mchNotifyRecord);
            } catch (Exception e) {
                log.info("数据库已存在[{}]消息，本次不再推送。", mchNotifyRecord.getOrderId());
                return ;
            }

            //推送到MQ
            Long notifyId = mchNotifyRecord.getNotifyId();
            mqSender.send(PayOrderMchNotifyMQ.build(notifyId));

        } catch (Exception e) {
            log.error("推送失败！", e);
        }
    }

    /** 商户通知信息，退款成功的发送通知 **/
    public void refundOrderNotify(RefundOrder dbRefundOrder){

        try {
            // 通知地址为空
            if(org.apache.commons.lang3.StringUtils.isEmpty(dbRefundOrder.getNotifyUrl())){
                return ;
            }

            //获取到通知对象
            MchNotifyRecord mchNotifyRecord = mchNotifyRecordService.findByRefundOrder(dbRefundOrder.getRefundOrderId());

            if(mchNotifyRecord != null){

                log.info("当前已存在通知消息， 不再发送。");
                return ;
            }

            //商户app私钥
            String appSecret = configContextQueryService.queryMchApp(dbRefundOrder.getMchNo(), dbRefundOrder.getAppId()).getAppSecret();

            // 封装通知url
            String notifyUrl = createNotifyUrl(dbRefundOrder, appSecret);
            mchNotifyRecord = new MchNotifyRecord();
            mchNotifyRecord.setOrderId(dbRefundOrder.getRefundOrderId());
            mchNotifyRecord.setOrderType(MchNotifyRecord.TYPE_REFUND_ORDER);
            mchNotifyRecord.setMchNo(dbRefundOrder.getMchNo());
            mchNotifyRecord.setMchOrderNo(dbRefundOrder.getMchRefundNo()); //商户订单号
            mchNotifyRecord.setIsvNo(dbRefundOrder.getIsvNo());
            mchNotifyRecord.setAppId(dbRefundOrder.getAppId());
            mchNotifyRecord.setNotifyUrl(notifyUrl);
            mchNotifyRecord.setResResult("");
            mchNotifyRecord.setNotifyCount(0);
            mchNotifyRecord.setState(MchNotifyRecord.STATE_ING); // 通知中

            try {
                mchNotifyRecordService.save(mchNotifyRecord);
            } catch (Exception e) {
                log.info("数据库已存在[{}]消息，本次不再推送。", mchNotifyRecord.getOrderId());
                return ;
            }

            //推送到MQ
            Long notifyId = mchNotifyRecord.getNotifyId();
            mqSender.send(PayOrderMchNotifyMQ.build(notifyId));

        } catch (Exception e) {
            log.error("推送失败！", e);
        }
    }


    /** 商户通知信息，转账订单的通知接口 **/
    public void transferOrderNotify(TransferOrder dbTransferOrder){

        try {
            // 通知地址为空
            if(org.apache.commons.lang3.StringUtils.isEmpty(dbTransferOrder.getNotifyUrl())){
                return ;
            }

            //获取到通知对象
            MchNotifyRecord mchNotifyRecord = mchNotifyRecordService.findByTransferOrder(dbTransferOrder.getTransferId());

            if(mchNotifyRecord != null){
                log.info("当前已存在通知消息， 不再发送。");
                return ;
            }

            //商户app私钥
            String appSecret = configContextQueryService.queryMchApp(dbTransferOrder.getMchNo(), dbTransferOrder.getAppId()).getAppSecret();

            // 封装通知url
            String notifyUrl = createNotifyUrl(dbTransferOrder, appSecret);
            mchNotifyRecord = new MchNotifyRecord();
            mchNotifyRecord.setOrderId(dbTransferOrder.getTransferId());
            mchNotifyRecord.setOrderType(MchNotifyRecord.TYPE_TRANSFER_ORDER);
            mchNotifyRecord.setMchNo(dbTransferOrder.getMchNo());
            mchNotifyRecord.setMchOrderNo(dbTransferOrder.getMchOrderNo()); //商户订单号
            mchNotifyRecord.setIsvNo(dbTransferOrder.getIsvNo());
            mchNotifyRecord.setAppId(dbTransferOrder.getAppId());
            mchNotifyRecord.setNotifyUrl(notifyUrl);
            mchNotifyRecord.setResResult("");
            mchNotifyRecord.setNotifyCount(0);
            mchNotifyRecord.setState(MchNotifyRecord.STATE_ING); // 通知中

            try {
                mchNotifyRecordService.save(mchNotifyRecord);
            } catch (Exception e) {
                log.info("数据库已存在[{}]消息，本次不再推送。", mchNotifyRecord.getOrderId());
                return ;
            }

            //推送到MQ
            Long notifyId = mchNotifyRecord.getNotifyId();
            mqSender.send(PayOrderMchNotifyMQ.build(notifyId));

        } catch (Exception e) {
            log.error("推送失败！", e);
        }
    }


    /**
     * 创建响应URL
     */
    public String createNotifyUrl(PayOrder payOrder, String appSecret) {

        QueryPayOrderRS queryPayOrderRS = QueryPayOrderRS.buildByPayOrder(payOrder);
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(queryPayOrderRS);
        jsonObject.put("reqTime", System.currentTimeMillis()); //添加请求时间

        // 报文签名
        jsonObject.put("sign", JeepayKit.getSign(jsonObject, appSecret));

        // 生成通知
        return StringKit.appendUrlQuery(payOrder.getNotifyUrl(), jsonObject);
    }


    /**
     * 创建响应URL
     */
    public String createNotifyUrl(RefundOrder refundOrder, String appSecret) {

        QueryRefundOrderRS queryRefundOrderRS = QueryRefundOrderRS.buildByRefundOrder(refundOrder);
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(queryRefundOrderRS);
        jsonObject.put("reqTime", System.currentTimeMillis()); //添加请求时间

        // 报文签名
        jsonObject.put("sign", JeepayKit.getSign(jsonObject, appSecret));

        // 生成通知
        return StringKit.appendUrlQuery(refundOrder.getNotifyUrl(), jsonObject);
    }


    /**
     * 创建响应URL
     */
    public String createNotifyUrl(TransferOrder transferOrder, String appSecret) {

        QueryTransferOrderRS rs = QueryTransferOrderRS.buildByRecord(transferOrder);
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(rs);
        jsonObject.put("reqTime", System.currentTimeMillis()); //添加请求时间

        // 报文签名
        jsonObject.put("sign", JeepayKit.getSign(jsonObject, appSecret));

        // 生成通知
        return StringKit.appendUrlQuery(transferOrder.getNotifyUrl(), jsonObject);
    }


    /**
     * 创建响应URL
     */
    public String createReturnUrl(PayOrder payOrder, String appSecret) {

        if(StringUtils.isEmpty(payOrder.getReturnUrl())){
            return "";
        }

        QueryPayOrderRS queryPayOrderRS = QueryPayOrderRS.buildByPayOrder(payOrder);
        JSONObject jsonObject = (JSONObject)JSONObject.toJSON(queryPayOrderRS);
        jsonObject.put("reqTime", System.currentTimeMillis()); //添加请求时间

        // 报文签名
        jsonObject.put("sign", JeepayKit.getSign(jsonObject, appSecret));   // 签名

        // 生成跳转地址
        return StringKit.appendUrlQuery(payOrder.getReturnUrl(), jsonObject);

    }

}