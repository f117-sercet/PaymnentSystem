package com.payment.pay.mch.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket server 服务类
 * /ws/channelUserId/{appId}/{客戶端自定義ID}
 *
 * @author: 段世超
 * @aate: Created in 2023/2/15 16:59
 */
@ServerEndpoint("/api/anon/ws/channelUserId/{appId}/{cid}")
@Component
public class WsChannelUserIdServer {

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

    @OnOpen
    public void  onOpen(Session session, @PathParam("payOrderId") String payOrderId, @PathParam("cid") String cid) {

        try {
            //设置当前属性
            this.cid = cid;
            this.payOrderId = payOrderId;
            this.session = session;

            Set<WsPayOrderServer> wsServerSet = wsOrderIdMap.get(payOrderId);
            if(wsServerSet == null) {
                wsServerSet = new CopyOnWriteArraySet<>();
            }
            //wsServerSet.add(this);
            wsOrderIdMap.put(payOrderId, wsServerSet);

            //addOnlineCount(); //在线数加1
            logger.info("cid[{}],payOrderId[{}]连接开启监听！当前在线人数为{}", cid, payOrderId, onlineClientSize);

        } catch (Exception e) {
            logger.error("ws监听异常cid[{}],payOrderId[{}]", cid, payOrderId, e);
        }
    }




}
