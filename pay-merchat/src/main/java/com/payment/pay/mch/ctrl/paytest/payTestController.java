package com.payment.pay.mch.ctrl.paytest;

import com.alibaba.fastjson.JSONObject;
import com.jeequan.jeepay.JeepayClient;
import com.jeequan.jeepay.exception.JeepayException;
import com.jeequan.jeepay.model.PayOrderCreateReqModel;
import com.jeequan.jeepay.request.PayOrderCreateRequest;
import com.jeequan.jeepay.response.PayOrderCreateResponse;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.entity.MchPayPassage;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.DBApplicationConfig;
import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.MchPayPassageService;
import com.pay.pay.service.impl.SysConfigService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Description： 支付测试类
 *
 * @author: 段世超
 * @aate: Created in 2023/2/10 17:34
 */
@RestController
@RequestMapping("/api/paytest")
public class payTestController extends CommonCtrl {

    @Resource
    private MchAppService mchAppService;
    @Resource
    private MchPayPassageService mchPayPassageService;
    @Resource
    private SysConfigService sysConfigService;

    /**
     * 查询商户对应应用下支持的支付方式
     **/
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_TEST_PAYWAY_LIST')")
    @GetMapping("/payways/{appId}")
    public ApiRes payWayList(@PathVariable("appId") String appId) {

        Set<String> payWaySet = new HashSet<>();
        mchPayPassageService.list(
                MchPayPassage.gw().select(MchPayPassage::getWayCode)
                        .eq(MchPayPassage::getMchNo, getCurrentMchNo())
                        .eq(MchPayPassage::getAppId, appId)
                        .eq(MchPayPassage::getState, CS.PUB_USABLE)
        ).stream().forEach(r -> payWaySet.add(r.getWayCode()));

        return ApiRes.ok(payWaySet);
    }

    /**
     * 调起下单接口
     **/
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_TEST_DO')")
    @PostMapping("/payOrders")
    public ApiRes doPay() {

        //获取请求参数
        String appId = getValStringRequired("appId");
        Long amount = getRequiredAmountL("amount");
        String mchOrderNo = getValStringRequired("mchOrderNo");
        String wayCode = getValStringRequired("wayCode");

        Byte divisionMode = getValByteRequired("divisionMode");
        String orderTitle = getValStringRequired("orderTitle");

        if (StringUtils.isEmpty(orderTitle)) {
            throw new BizException("订单标题不能为空");
        }

        // 前端明确了支付参数的类型 payDataType
        String payDataType = getValString("payDataType");
        String authCode = getValString("authCode");


        MchApp mchApp = mchAppService.getById(appId);
        if (mchApp == null || mchApp.getState() != CS.PUB_USABLE || !mchApp.getAppId().equals(appId)) {
            throw new BizException("商户应用不存在或不可用");
        }

        PayOrderCreateRequest request = new PayOrderCreateRequest();
        PayOrderCreateReqModel model = new PayOrderCreateReqModel();
        request.setBizModel(model);

        model.setMchNo(getCurrentMchNo()); // 商户号
        model.setAppId(appId);
        model.setMchOrderNo(mchOrderNo);
        model.setWayCode(wayCode);
        model.setAmount(amount);

        // payPal通道使用USD类型货币
        if (wayCode.equalsIgnoreCase("pp_pc")) {
            model.setCurrency("USD");
        } else {
            model.setCurrency("CNY");
        }

        model.setClientIp(getClientIp());
        model.setSubject(orderTitle + "[" + getCurrentMchNo() + "商户联调]");
        model.setBody(orderTitle + "[" + getCurrentMchNo() + "商户联调】");

        DBApplicationConfig dbApplicationConfig = sysConfigService.getDBApplicationConfig();
        model.setNotifyUrl(dbApplicationConfig.getMchSiteUrl() + "/api/annon/payTestNotify/payOrder"); // 回调地址

        model.setDivisionMode(divisionMode); // 分账模式

        // 设置扩展参数
        JSONObject exParams = new JSONObject();

        if (StringUtils.isNotEmpty(payDataType)) {

            exParams.put("payDataType", payDataType.trim());
        }
        if (StringUtils.isNotEmpty(authCode)) {
            exParams.put("authCode", authCode.trim());
        }
        model.setChannelExtra(exParams.toString());

        JeepayClient jeepayClient = new JeepayClient(dbApplicationConfig.getPaySiteUrl(), mchApp.getAppSecret());

        try {
            PayOrderCreateResponse response = jeepayClient.execute(request);
            if (response.getCode()!=0) {
                throw new BizException(response.getMsg());
            }
            return ApiRes.ok(response.get());
        } catch (JeepayException e) {
            throw new RuntimeException(e);
        }
    }
}
