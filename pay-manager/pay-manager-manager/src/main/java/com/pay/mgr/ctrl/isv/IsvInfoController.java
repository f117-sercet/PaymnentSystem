package com.pay.mgr.ctrl.isv;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.components.mq.vender.IMQSender;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.IsvInfo;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.IsvInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 服务商管理类
 *
 * @author: 段世超
 * @aate: Created in 2022/9/21 18:17
 */
@RestController
@RequestMapping("/api/isvInfo")
public class IsvInfoController extends CommonCtrl {

    @Autowired
    private IsvInfoService isvInfoService;

    @Autowired

    private IMQSender mqSender;


   public  ApiRes list(){

       IsvInfo isvInfo = getObject(IsvInfo.class);
       LambdaQueryWrapper<IsvInfo> wrapper = isvInfo.gw();
       if (StringUtils.isNotEmpty(isvInfo.getIsvNo())){

           wrapper.eq(IsvInfo::getIsvNo,isvInfo.getIsvNo());
       }

       if (StringUtils.isNotEmpty(wrapper.getEntity().getIsvName())){

       }
       if (isvInfo.getState() != null){

           wrapper.eq(IsvInfo::getState,isvInfo.getState());
       }
       wrapper.orderByDesc(IsvInfo::getCreatedAt);
       IPage<IsvInfo> pages = isvInfoService.page(getIPage(true),wrapper);

       return ApiRes.page(pages);
   }

    /**
     * 新增服务商信息
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_ISV_INFO_ADD')")
    @MethodLog(remark = "新增服务商")
    @RequestMapping(value="", method = RequestMethod.POST)
    public ApiRes add() {

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

   }
