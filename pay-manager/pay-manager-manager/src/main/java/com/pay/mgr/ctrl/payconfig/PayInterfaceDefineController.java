package com.pay.mgr.ctrl.payconfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.PayInterfaceConfig;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.payMbg.service.impl.PayInterfaceConfigService;
import com.pay.payMbg.service.impl.PayInterfaceDefineService;
import com.pay.payMbg.service.impl.PayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description：支付接口定义管理类
 *
 * @author: 段世超
 * @aate: Created in 2022/12/3 20:58
 */
@RestController
@RequestMapping("api/payIfDefines")
public class PayInterfaceDefineController extends CommonCtrl {

    @Autowired
    private PayInterfaceDefineService payInterfaceDefineService;
    @Autowired private PayOrderService payOrderService;
    @Autowired private PayInterfaceConfigService payInterfaceConfigService;

    /**
     * 列表
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PC_IF_DEFINE_LIST')")
    @GetMapping
    public ApiRes list() {

        List<PayInterfaceDefine> list = payInterfaceDefineService.list(PayInterfaceDefine.gw()
                .orderByAsc(PayInterfaceDefine::getCreatedAt)
        );
        return ApiRes.ok(list);
    }

    /**
     * detail
     * @param ifCode
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ENT_PC_IF_DEFINE_VIEW', 'ENT_PC_IF_DEFINE_EDIT')")
    @GetMapping("/{ifCode}")
    public ApiRes detail(@PathVariable("ifCode") String ifCode) {
        return ApiRes.ok(payInterfaceDefineService.getById(ifCode));
    }

    /**
     * 新增支付窗口
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PC_IF_DEFINE_ADD')")
    @PostMapping
    @MethodLog(remark = "新增支付接口")
    public ApiRes add() {

        PayInterfaceDefine payInterfaceDefine = getObject(PayInterfaceDefine.class);

        JSONArray jsonArray = new JSONArray();
        String[] wayCodes = getValStringRequired("wayCodeStrs").split(",");
        for (String wayCode : wayCodes) {
            JSONObject object = new JSONObject();
            object.put("wayCode", wayCode);
            jsonArray.add(object);
        }
        payInterfaceDefine.setWayCodes(jsonArray);

        boolean result = payInterfaceDefineService.save(payInterfaceDefine);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_CREATE);
        }
        return ApiRes.ok();
    }

    /**
     * 更新支付接口
     * @param ifCode
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PC_IF_DEFINE_EDIT')")
    @PutMapping("/{ifCode}")
    @MethodLog(remark = "更新支付接口")
    public ApiRes update(@PathVariable("ifCode") String ifCode){
        PayInterfaceDefine payInterfaceDefine = getObject(PayInterfaceDefine.class);
        payInterfaceDefine.setIfCode(ifCode);

        JSONArray jsonArray = new JSONArray();
        String[] wayCodes = getValStringRequired("wayCodeStrs").split(",");
        for (String wayCode : wayCodes) {
            JSONObject object = new JSONObject();
            object.put("wayCode", wayCode);
            jsonArray.add(object);
        }
        payInterfaceDefine.setWayCodes(jsonArray);

        boolean result = payInterfaceDefineService.updateById(payInterfaceDefine);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_UPDATE);
        }
        return ApiRes.ok();
    }
    @PreAuthorize("hasAuthority('ENT_PC_IF_DEFINE_DEL')")
    @DeleteMapping("/{ifCode}")
    @MethodLog(remark = "删除支付接口")
    public ApiRes delete(@PathVariable("ifCode") String ifCode) {

        // 校验该支付方式是否有服务商或商户配置参数或者已有订单
        if (payInterfaceConfigService.count(PayInterfaceConfig.gw().eq(PayInterfaceConfig::getIfCode, ifCode)) > 0
                || payOrderService.count(PayOrder.gw().eq(PayOrder::getIfCode, ifCode)) > 0) {
            throw new BizException("该支付接口已有服务商或商户配置参数或已发生交易，无法删除！");
        }

        boolean result = payInterfaceDefineService.removeById(ifCode);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_DELETE);
        }
        return ApiRes.ok();
    }
}
