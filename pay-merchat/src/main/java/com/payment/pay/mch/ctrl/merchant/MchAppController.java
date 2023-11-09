package com.payment.pay.mch.ctrl.merchant;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.components.mq.model.ResetIsvMchAppInfoConfigMQ;
import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.payMbg.service.impl.MchAppService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Description： 商户应用管理
 *
 * @author: 段世超
 * @aate: Created in 2023/1/30 9:48
 */
@RestController
@RequestMapping("/api/mchApps")
public class MchAppController extends CommonCtrl {

    @Resource
    private MchAppService mchAppService;
    @Resource
    private IMQSender mqSender;

    /**
     * @Author: 段世超
     * @Description: 应用列表
     * @Date: 9:59 2021/6/16
     */
    @PreAuthorize("hasAuthority('ENT_MCH_APP_LIST')")
    @GetMapping
    public ApiRes list() {
        MchApp mchApp = getObject(MchApp.class);
        mchApp.setMchNo(getCurrentMchNo());

        IPage<MchApp> pages = mchAppService.selectPage(getIPage(true), mchApp);
        return ApiRes.ok(pages);
    }

    /**
     * 新建应用
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_MCH_APP_ADD')")
    @MethodLog(remark = "新建应用")
    @PostMapping
    public ApiRes add(){
        MchApp mchApp = getObject(MchApp.class);
        mchApp.setMchNo(getCurrentMchNo());
        mchApp.setAppId(IdUtil.objectId());

        boolean result = mchAppService.save(mchApp);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_CREATE);
        }
        return ApiRes.ok();
    }

    /**
     * 应用详情
     * @param appId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ENT_MCH_APP_VIEW', 'ENT_MCH_APP_EDIT')")
    @GetMapping("/{appId}")
    public ApiRes detail(@PathVariable("appId") String appId) {
        MchApp mchApp = mchAppService.selectById(appId);

        if (mchApp == null || !mchApp.getMchNo().equals(getCurrentMchNo())) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        return ApiRes.ok(mchApp);
    }
    @PreAuthorize("hasAuthority('ENT_MCH_APP_EDIT')")
    @MethodLog(remark = "更新应用信息")
    @PutMapping("/{appId}")
    public ApiRes update(@PathVariable("appId") String appId){

            MchApp mchApp = getObject(MchApp.class);
            mchApp.setAppId(appId);

            MchApp dbRecord = mchAppService.getById(appId);
            if (!dbRecord.getMchNo().equals(getCurrentMchNo())) {
                throw new BizException("无权操作！");
            }

            boolean result = mchAppService.updateById(mchApp);
            if (!result) {
                return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_UPDATE);
            }
            // 推送修改应用消息
            mqSender.send(ResetIsvMchAppInfoConfigMQ.build(ResetIsvMchAppInfoConfigMQ.RESET_TYPE_MCH_APP, null, mchApp.getMchNo(), appId));
            return ApiRes.ok();
        }
    @PreAuthorize("hasAuthority('ENT_MCH_APP_DEL')")
    @MethodLog(remark = "删除应用")
    @DeleteMapping("/{appId}")
    public ApiRes delete(@PathVariable("appId") String appId) {
        MchApp mchApp = mchAppService.getById(appId);

        if (!mchApp.getMchNo().equals(getCurrentMchNo())) {
            throw new BizException("无权操作！");
        }

        mchAppService.removeByAppId(appId);

        // 推送mq到目前节点进行更新数据
        mqSender.send(ResetIsvMchAppInfoConfigMQ.build(ResetIsvMchAppInfoConfigMQ.RESET_TYPE_MCH_APP, null, mchApp.getMchNo(), appId));
        return ApiRes.ok();
    }

}
