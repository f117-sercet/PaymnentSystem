package com.payment.pay.mch.ctrl.paytest;

import com.alibaba.fastjson.JSONObject;
import com.jeequan.jeepay.util.JeepayKit;
import com.pay.pay.core.entity.MchApp;
import com.pay.payMbg.service.impl.MchAppService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import com.payment.pay.mch.websocket.server.WsPayOrderServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Description： 支付测试 - 回调函数
 *
 * @author: 段世超
 * @aate: Created in 2023/2/14 18:00
 */

@RestController
@RequestMapping("/api/anon/paytestNotify")
public class PayTestNotifyController extends CommonCtrl {

    @Resource
    private MchAppService mchAppService;

    @RequestMapping("/payOrder")
    public void payOrderNotify() throws IOException{

        // 请求参数
        JSONObject params = getReqParamJSON();

        String mchNo = params.getString("mchNo");
        String appId = params.getString("appId");
        String sign = params.getString("sign");

        MchApp mchApp = mchAppService.getById(appId);

        if (mchApp == null || !mchApp.getMchNo().equals(mchNo)) {
            response.getWriter().print("app is not exists");

            return;
        }
        params.remove("sign");

        if(!JeepayKit.getSign(params, mchApp.getAppSecret()).equalsIgnoreCase(sign)){
            response.getWriter().print("sign fail");
            return;
        }
        JSONObject msg = new JSONObject();
        msg.put("state", params.getIntValue("state"));
        msg.put("errCode", params.getString("errCode"));
        msg.put("errMsg", params.getString("errMsg"));

        //推送到前端
        WsPayOrderServer.sendMsgByOrderId(params.getString("payOrderId"), msg.toJSONString());

        response.getWriter().print("SUCCESS");

    }

}
