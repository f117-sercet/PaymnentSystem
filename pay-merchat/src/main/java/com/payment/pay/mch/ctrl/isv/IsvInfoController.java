package com.payment.pay.mch.ctrl.isv;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.entity.IsvInfo;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.IsvInfoService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
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
}
