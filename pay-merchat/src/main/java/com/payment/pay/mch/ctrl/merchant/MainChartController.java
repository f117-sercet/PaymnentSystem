package com.payment.pay.mch.ctrl.merchant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.entity.MchInfo;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchInfoService;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.pay.service.impl.SysUserService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description： 主页数据类
 *
 * @author: 段世超
 * @aate: Created in 2023/1/30 9:34
 */
@Slf4j
@RestController
@RequestMapping("api/mainChart")
public class MainChartController extends CommonCtrl {

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private MchInfoService mchInfoService;

    /**
     * 周交易总额
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_MCH_MAIN_PAY_AMOUNT_WEEK')")
    @RequestMapping(value="/payAmountWeek", method = RequestMethod.GET)
    public ApiRes payAmountWeek(){

        return ApiRes.ok(payOrderService.mainPageWeekCount(getCurrentMchNo()));
    }
    /**
     * 商户总数量、服务商总数量、总交易金额、总交易笔数
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_MCH_MAIN_NUMBER_COUNT')")
    @RequestMapping(value="/numCount", method = RequestMethod.GET)
    public ApiRes numCount() {
        return ApiRes.ok(payOrderService.mainPageNumCount(getCurrentMchNo()));
    }

    /** 交易统计 */
    @PreAuthorize("hasAuthority('ENT_MCH_MAIN_PAY_COUNT')")
    @RequestMapping(value="/payCount", method = RequestMethod.GET)
    public ApiRes payCount() {
        // 获取传入参数
        JSONObject paramJSON = getReqParamJSON();
        String createdStart = paramJSON.getString("createdStart");
        String createdEnd = paramJSON.getString("createdEnd");

        List<Map> mapList = payOrderService.mainPagePayCount(getCurrentMchNo(), createdStart, createdEnd);
        //返回数据
        return ApiRes.ok(mapList);
    }

    /** 支付方式统计 */
    @PreAuthorize("hasAuthority('ENT_MCH_MAIN_PAY_TYPE_COUNT')")
    @RequestMapping(value="/payTypeCount", method = RequestMethod.GET)
    public ApiRes payWayCount() {
        JSONObject paramJSON = getReqParamJSON();
        // 开始、结束时间
        String createdStart = paramJSON.getString("createdStart");
        String createdEnd = paramJSON.getString("createdEnd");
        ArrayList arrayResult = payOrderService.mainPagePayTypeCount(getCurrentMchNo(), createdStart, createdEnd);
        return ApiRes.ok(arrayResult);
    }

    /** 商户基本信息、用户基本信息 **/
    @PreAuthorize("hasAuthority('ENT_MCH_MAIN_USER_INFO')")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes userDetail() {
        SysUser sysUser = sysUserService.getById(getCurrentUser().getSysUser().getSysUserId());
        MchInfo mchInfo = mchInfoService.getById(getCurrentMchNo());
        JSONObject json = (JSONObject) JSON.toJSON(mchInfo);
        json.put("loginUsername", sysUser.getLoginUsername());
        json.put("realname", sysUser.getRealname());
        return ApiRes.ok(json);
    }
}
