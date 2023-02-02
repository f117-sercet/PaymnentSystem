package com.payment.pay.mch.ctrl.merchant;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.MchInfo;
import com.pay.pay.core.entity.MchPayPassage;
import com.pay.pay.core.entity.PayWay;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchInfoService;
import com.pay.pay.service.impl.MchPayPassageService;
import com.pay.pay.service.impl.PayWayService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Description： 商户支付通道配置类
 *
 * @author: 段世超
 * @aate: Created in 2023/2/2 9:51
 */
@RestController
@RequestMapping("/api/mch/payPassages")
public class MchPayPassageConfigController extends CommonCtrl {

    @Resource
    private MchPayPassageService mchPayPassageService;
    @Resource
    private PayWayService payWayService;
    @Resource
    private MchInfoService mchInfoService;


    /**
     * 查询支付方式列表，并添加是否配置支付通道状态
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_PASSAGE_LIST')")
    @GetMapping
    public ApiRes list() {

        String appId = getValStringRequired("appId");
        String wayCode = getValString("wayCode");
        String wayName = getValString("wayName");

        //支付方式集合
        LambdaQueryWrapper<PayWay> wrapper = PayWay.gw();
        if (StrUtil.isNotBlank(wayCode)) {
            wrapper.eq(PayWay::getWayCode, wayCode);
        }
        if (StrUtil.isNotBlank(wayName)) {
            wrapper.like(PayWay::getWayName, wayName);
        }
        IPage<PayWay> payWayPage = payWayService.page(getIPage(), wrapper);

        if (!CollectionUtils.isEmpty(payWayPage.getRecords())) {

            // 支付方式代码集合
            List<String> wayCodeList = new LinkedList<>();
            payWayPage.getRecords().stream().forEach(payWay -> wayCodeList.add(payWay.getWayCode()));

            // 商户支付通道集合
            List<MchPayPassage> mchPayPassageList = mchPayPassageService.list(MchPayPassage.gw()
                    .select(MchPayPassage::getWayCode, MchPayPassage::getState)
                    .eq(MchPayPassage::getAppId, appId)
                    .eq(MchPayPassage::getMchNo, getCurrentMchNo())
                    .in(MchPayPassage::getWayCode, wayCodeList));

            for (PayWay payWay : payWayPage.getRecords()) {
                payWay.addExt("passageState", CS.NO);
                for (MchPayPassage mchPayPassage : mchPayPassageList) {
                    // 某种支付方式多个通道的情况下，只要有一个通道状态为开启，则该支付方式对应为开启状态
                    if (payWay.getWayCode().equals(mchPayPassage.getWayCode()) && mchPayPassage.getState() == CS.YES) {
                        payWay.addExt("passageState", CS.YES);
                        break;
                    }
                }
            }
        }

        return ApiRes.page(payWayPage);
    }

    /**
     * @Author: 段世超
     * @Description: 根据appId、支付方式查询可用的支付接口列表
     */
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_PASSAGE_CONFIG')")
    @GetMapping("/availablePayInterface/{appId}/{wayCode}")
    public ApiRes availablePayInterface(@PathVariable("appId") String appId, @PathVariable("wayCode") String wayCode) {

        String mchNo = getCurrentUser().getSysUser().getBelongInfoId();
        MchInfo mchInfo = mchInfoService.getById(mchNo);
        if (mchInfo == null || mchInfo.getState() != CS.YES) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        // 根据支付方式查询可用支付接口列表
        List<JSONObject> list = mchPayPassageService.selectAvailablePayInterfaceList(wayCode, appId, CS.INFO_TYPE_MCH_APP, mchInfo.getType());

        return ApiRes.ok(list);
    }

    /**
     * 段世超
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ApiRes detail(@PathVariable("id") Long id) {

        MchPayPassage payPassage = mchPayPassageService.getById(id);
        if (payPassage == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        if (!payPassage.getMchNo().equals(getCurrentUser().getSysUser().getBelongInfoId())) {
            return ApiRes.fail(ApiCodeEnum.SYS_PERMISSION_ERROR);
        }
        payPassage.setRate(payPassage.getRate().multiply(new BigDecimal("100")));
        return ApiRes.ok(payPassage);

    }

    /**
     * 应用支付通道配置
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_PASSAGE_ADD')")
    @PostMapping
    @MethodLog(remark = "更新应用支付通道")
    public ApiRes saveOrUpdate(){

        String reqParams = getValStringRequired("reqParams");
        try {
            List<MchPayPassage> mchPayPassageList = JSONArray.parseArray(reqParams, MchPayPassage.class);
            mchPayPassageService.saveOrUpdateBatchSelf(mchPayPassageList, getCurrentMchNo());
            return ApiRes.ok();
        }catch (Exception e) {
            return ApiRes.fail(ApiCodeEnum.SYSTEM_ERROR);
        }
    }
}
