package com.payment.pay.mch.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2023/2/14 18:12
 */
@ServerEndpoint("/api/anon/ws/payOrder/{payOrderId}/{cid}")
@Component
public class WsPayOrderServer {


    private final static Logger logger = LoggerFactory.getLogger(WsPayOrderServer.class);

    //当前在线客户端 数量
    private static int onlineClientSize = 0;

    // payOrderId 与 WsPayOrderServer 存储关系, ConcurrentHashMap保证线程安全
    private static Map<String, Set<WsPayOrderServer>> wsOrderIdMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //客户端自定义ID
    private String cid = "";

    //支付订单号
    private String payOrderId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("payOrderId") String payOrderId, @PathParam("cid") String cid) {

        try {
            //设置当前属性
            this.cid = cid;
            this.payOrderId = payOrderId;
            this.session = session;

            Set<WsPayOrderServer> wsServerSet = wsOrderIdMap.get(payOrderId);
            if(wsServerSet == null) {
                wsServerSet = new CopyOnWriteArraySet<>();
            }
            wsServerSet.add(this);
            wsOrderIdMap.put(payOrderId, wsServerSet);

            addOnlineCount(); //在线数加1
            logger.info("cid[{}],payOrderId[{}]连接开启监听！当前在线人数为{}", cid, payOrderId, onlineClientSize);

        } catch (Exception e) {
            logger.error("ws监听异常cid[{}],payOrderId[{}]", cid, payOrderId, e);
        }
    }

    @OnClose
    public void onClose(){

        Set wsSet = wsOrderIdMap.get(this.payOrderId);
        wsSet.remove(this);
        if (wsSet.isEmpty()) {
            wsOrderIdMap.remove(this.payOrderId);
        }

        subOnlineCount(); //在线人数减一
        logger.info("cid[{}],payOrderId[{}]连接关闭！当前在线人数为{}", cid, payOrderId, onlineClientSize);

    }

    public void onError(Session session,Throwable error){

        logger.error("ws发生错误",error);
    }

    /**
     * 服务器主动推送
     * @param message
     * @throws IOException
     */
    public  void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }

    /***
     * 根据订单ID,推送消息
     * 捕捉所有的异常，避免影响业务
     * @param payOrderId
     * @param msg
     */
    public static void sendMsgByOrderId(String payOrderId, String msg){

        try{
            logger.info("推送ws消息到浏览器，payOrderId={},msg={}",payOrderId,msg);
            Set<WsPayOrderServer> wsSet = wsOrderIdMap.get(payOrderId);
            if (wsSet == null || wsSet.isEmpty()) {

                logger.info("payOrderId[{}] 无ws监听客户端",payOrderId);
                return;
            }

            for (WsPayOrderServer item : wsSet) {

                try {
                    item.sendMessage(msg);
                }catch (Exception e){
                    logger.info("推送设备消息时异常，payOrderId={}, cid={}", payOrderId, item.cid, e);
                    continue;
                }
            }

        }catch (Exception e){
            logger.info("推送消息时异常，payOrderId={}", payOrderId, e);

        }
    }


    public static synchronized int getOnlineClientSize() {
        return onlineClientSize;
    }


    private static synchronized void addOnlineCount() {
        onlineClientSize++;
    }
    private static synchronized void subOnlineCount() {

        onlineClientSize--;
    }
}