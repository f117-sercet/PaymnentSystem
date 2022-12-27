package com.pay.mgr.ctrl.sysuser;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.entity.SysLog;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 系统日志记录类
 *
 * @author: 段世超
 * @aate: Created in 2022/12/27 17:53
 */
@RestController
@RequestMapping("api/syslog")
public class SysLogController extends CommonCtrl {

    @Autowired
    SysLogService sysLogService;


    /***
     * 日志纪录列表
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_LOG_LIST')")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list(){

        SysLog sysLog = getObject(SysLog.class);

        // 查询列表
        LambdaQueryWrapper<SysLog> conditions = SysLog.gw();
        conditions.orderByDesc(SysLog::getCreatedAt);
        if (sysLog.getUserId() != null){

            conditions.eq(SysLog::getUserId,sysLog.getUserId());
        }

        IPage<SysLog> pages = sysLogService.page(getIPage(), conditions);
        return ApiRes.page(pages);
    }
}
