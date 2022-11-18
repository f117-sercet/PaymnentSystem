package com.pay.mgr.ctrl.merchant;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.entity.MchPayPassage;
import com.pay.pay.core.entity.PayWay;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.MchInfoService;
import com.pay.pay.service.impl.MchPayPassageService;
import com.pay.pay.service.impl.PayWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * Description： 商户支付通道管理类
 *
 * @author: 段世超
 * @aate: Created in 2022/11/11 18:06
 */
@RestController
@RequestMapping("/api/mch/payPassages")
public class MchPayPassageConfigController extends CommonCtrl {

    @Autowired
    private
    MchPayPassageService mchPayPassageService;
    @Autowired
    private PayWayService payWayService;
    @Autowired
    private MchInfoService mchInfoService;
    @Autowired
    private MchAppService mchAppService;

    /****
     * 查询支付方式列表,并添加是否支持配置支付通道状态
     * @return
     */
    public ApiRes list(){
        String appId = getValStringRequired("appId");
        String wayCode = getValString("wayCode");
        String wayName = getValString("wayName");

        // 支付方式集合
        LambdaQueryWrapper<PayWay> wrapper = PayWay.gw();

        if (StrUtil.isNotBlank(wayCode)) {
            wrapper.eq(PayWay::getWayCode,wayCode);
        }
        if (StrUtil.isNotBlank(wayName)) {
            wrapper.like(PayWay::getWayName,wayName);
        }
        IPage<PayWay> payWayPage = payWayService.page(getIPage(),wrapper);
        if (!CollectionUtils.isEmpty(payWayPage.getRecords())){

            // 支付方式代码集合
            List<String> wayCodeList = new LinkedList<>();
            payWayPage.getRecords().stream().forEach(payWay->wayCodeList.add(payWay.getWayCode()));

            // 应用支付通道集合
            // 应用支付通道集合
            List<MchPayPassage> mchPayPassageList = mchPayPassageService.list(MchPayPassage.gw()
                    .select(MchPayPassage::getWayCode, MchPayPassage::getState)
                    .eq(MchPayPassage::getAppId, appId)
                    .in(MchPayPassage::getWayCode, wayCodeList));

        }
        return ApiRes.ok();
    }

}
