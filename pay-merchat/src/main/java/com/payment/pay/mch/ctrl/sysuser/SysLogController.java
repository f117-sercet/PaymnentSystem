package com.payment.pay.mch.ctrl.sysuser;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.SysLog;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.SysLogService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * Description： 系统日志类
 *
 * @author: 段世超
 * @aate: Created in 2023/3/9 17:37
 */
@RestController
@RequestMapping("api/sysLog")
public class SysLogController extends CommonCtrl {

    @Resource
    SysLogService sysLogService;

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:15
     * @describe: 日志记录列表
     */
    @PreAuthorize("hasAuthority('ENT_LOG_LIST')")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list() {
        SysLog sysLog = getObject(SysLog.class);
        JSONObject paramJSON = getReqParamJSON();
        // 查询列表
        LambdaQueryWrapper<SysLog> condition = SysLog.gw();
        condition.orderByDesc(SysLog::getCreatedAt);
        if (sysLog.getUserId() != null) {
            condition.eq(SysLog::getUserId, sysLog.getUserId());
        }
        if (sysLog.getUserName() != null) {
            condition.eq(SysLog::getUserName, sysLog.getUserName());
        }
        if (StringUtils.isNotEmpty(sysLog.getSysType())) {
            condition.eq(SysLog::getSysType, sysLog.getSysType());
        }
        if (paramJSON != null) {
            if (StringUtils.isNotEmpty(paramJSON.getString("createdStart"))) {
                condition.ge(SysLog::getCreatedAt, paramJSON.getString("createdStart"));
            }
            if (StringUtils.isNotEmpty(paramJSON.getString("createdEnd"))) {
                condition.le(SysLog::getCreatedAt, paramJSON.getString("createdEnd"));
            }
        }
        IPage<SysLog> pages = sysLogService.page(getIPage(), condition);
        return ApiRes.page(pages);
    }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:16
     * @describe: 查看日志信息
     */
    @PreAuthorize("hasAuthority('ENT_SYS_LOG_VIEW')")
    @RequestMapping(value="/{sysLogId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("sysLogId") String sysLogId) {
        SysLog sysLog = sysLogService.getById(sysLogId);
        if (sysLog == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        return ApiRes.ok(sysLog);
    }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 16:16
     * @describe: 删除日志信息
     */
    @PreAuthorize("hasAuthority('ENT_SYS_LOG_DEL')")
    @MethodLog(remark = "删除日志信息")
    @RequestMapping(value="/{selectedIds}", method = RequestMethod.DELETE)
    public ApiRes delete(@PathVariable("selectedIds") String selectedIds) {
        String[] ids = selectedIds.split(",");
        List<Long> idsList = new LinkedList<>();
        for (String id : ids) {
            idsList.add(Long.valueOf(id));
        }
        boolean result = sysLogService.removeByIds(idsList);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_DELETE);
        }
        return ApiRes.ok();
    }
}
