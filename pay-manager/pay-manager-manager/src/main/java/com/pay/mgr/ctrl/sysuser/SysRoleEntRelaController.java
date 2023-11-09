package com.pay.mgr.ctrl.sysuser;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.entity.SysRoleEntRela;
import com.pay.pay.core.model.ApiRes;
import com.pay.payMbg.service.impl.SysRoleEntRelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 角色,权限管理
 *
 * @author: 段世超
 * @aate: Created in 2023/1/9 11:25
 */
@RestController
@RequestMapping("/api/sysRoleEntRelas")
public class SysRoleEntRelaController extends CommonCtrl {

    @Autowired
    private SysRoleEntRelaService sysRoleEntRelaService;


    /**列表**/
    @PreAuthorize("hasAnyAuthority('ENT_UR_ROLE_ADD','ENT_UR_ROLE_DIST')")
    @RequestMapping(value = "",method = RequestMethod.GET)
    public  ApiRes list(){

        SysRoleEntRela queryObject = getObject(SysRoleEntRela.class);
        LambdaQueryWrapper<SysRoleEntRela> condition = SysRoleEntRela.gw();
        if (queryObject.getRoleId() != null) {

            condition.eq(SysRoleEntRela::getRoleId,queryObject.getRoleId());
        }
        IPage pages = sysRoleEntRelaService.page(getIPage(true), condition);

        return ApiRes.page(pages);

    }

}
