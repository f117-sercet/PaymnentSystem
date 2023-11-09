package com.pay.mgr.ctrl.order;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.entity.RefundOrder;
import com.pay.pay.core.model.ApiRes;
import com.pay.payMbg.service.impl.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 退款订单类型
 *
 * @author: 段世超
 * @aate: Created in 2022/11/29 22:32
 */
@RestController
@RequestMapping("/api/refundOrder")
public class RefundOrderController extends CommonCtrl {

    @Autowired
    private RefundOrderService refundOrderService;

    /**
     * 退款订单列表
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_REFUND_LIST')")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list() {

        RefundOrder refundOrder = getObject(RefundOrder.class);
        JSONObject paramJSON = getReqParamJSON();
        LambdaQueryWrapper<RefundOrder> wrapper = RefundOrder.gw();
        IPage<RefundOrder> pages = refundOrderService.pageList(getIPage(), wrapper, refundOrder, paramJSON);

        return ApiRes.page(pages);
    }

    /**
     * 退款订单信息
     * @param refundOrderId
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_REFUND_ORDER_VIEW')")
    @RequestMapping(value="/{refundOrderId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("refundOrderId") String refundOrderId) {
        RefundOrder refundOrder = refundOrderService.getById(refundOrderId);
        if (refundOrder == null) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        return ApiRes.ok(refundOrder);
    }
}
