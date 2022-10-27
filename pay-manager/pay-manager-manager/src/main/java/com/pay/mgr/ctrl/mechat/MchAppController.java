package com.pay.mgr.ctrl.mechat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.components.mq.vender.IMQSender;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.MchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description： 商户应用管理类
 *
 * @author: 段世超
 * @aate: Created in 2022/10/27 19:15
 */
public class MchAppController extends CommonCtrl {

    @Autowired
    private MchInfoService mchInfoService;
    @Autowired private MchAppService mchAppService;
    @Autowired private IMQSender mqSender;

    @PreAuthorize("hasAuthority('ENT_MCH_APP_LIST')")
    @GetMapping
    public ApiRes list(){

        MchApp mchApp = getObject(MchApp.class);

        IPage<MchApp> pages = mchAppService.selectPage(getIPage(), mchApp);
        return ApiRes.ok(pages);

    }
}
