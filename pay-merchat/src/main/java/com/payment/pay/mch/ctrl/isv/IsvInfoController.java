package com.payment.pay.mch.ctrl.isv;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.components.mq.model.ResetIsvMchAppInfoConfigMQ;
import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.IsvInfo;
import com.pay.pay.core.model.ApiRes;
import com.pay.payMbg.service.impl.IsvInfoService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description： 服务商管类型
 *
 * @author: 段世超
 * @aate: Created in 2023/2/23 18:08
 */
@RestController
@RequestMapping("/api/isvInfo")
public class IsvInfoController extends CommonCtrl {

    @Resource
    private IsvInfoService isvInfoService;
    @Resource
    private IMQSender mqSender;

    /**
     * 查询服务商信息列表
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_ISV_LIST')")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list() {

        IsvInfo isvInfo = getObject(IsvInfo.class);
        LambdaQueryWrapper<IsvInfo> wrapper = IsvInfo.gw();
        if (StringUtils.isNotEmpty(isvInfo.getIsvNo())) {
            wrapper.eq(IsvInfo::getIsvNo, isvInfo.getIsvNo());
        }
        if (StringUtils.isNotEmpty(isvInfo.getIsvName())) {
            wrapper.eq(IsvInfo::getIsvName, isvInfo.getIsvName());
        }
        if (isvInfo.getState() != null) {
            wrapper.eq(IsvInfo::getState, isvInfo.getState());
        }
        wrapper.orderByDesc(IsvInfo::getCreatedAt);
        IPage<IsvInfo> pages = isvInfoService.page(getIPage(true), wrapper);

        return ApiRes.page(pages);
    }

    @PreAuthorize("hasAuthority('ENT_ISV_INFO_ADD')")
    @MethodLog(remark = "新增服务商")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public ApiRes add(){

        IsvInfo isvInfo = getObject(IsvInfo.class);
        String isvNo = "V" + DateUtil.currentSeconds();
        isvInfo.setIsvNo(isvNo);
        isvInfo.setCreatedUid(getCurrentUser().getSysUser().getSysUserId());
        isvInfo.setCreatedBy(getCurrentUser().getSysUser().getRealname());
        boolean result = isvInfoService.save(isvInfo);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_CREATE);
        }
        return ApiRes.ok();

    }


    /**
     * @author: 段世超
     * @date: 2021/6/7 16:13
     * @describe: 删除服务商信息
     */
    @PreAuthorize("hasAuthority('ENT_ISV_INFO_DEL')")
    @MethodLog(remark = "删除服务商")
    @RequestMapping(value="/{isvNo}", method = RequestMethod.DELETE)
    public ApiRes delete(@PathVariable("isvNo") String isvNo) {
        isvInfoService.removeByIsvNo(isvNo);

        // 推送mq到目前节点进行更新数据
        mqSender.send(ResetIsvMchAppInfoConfigMQ.build(ResetIsvMchAppInfoConfigMQ.RESET_TYPE_ISV_INFO, isvNo, null, null));
        return ApiRes.ok();
    }

    /**
     * 查看服务商信息
     * @param isvNo
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ENT_ISV_INFO_VIEW', 'ENT_ISV_INFO_EDIT')")
    @RequestMapping(value="/{isvNo}", method = RequestMethod.GET)
    public ApiRes details(@PathVariable("isvNo") String isvNo) {

        IsvInfo isvInfo = isvInfoService.getById(isvNo);
        if (isvInfo == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        return ApiRes.ok(isvInfo);
    }


}
