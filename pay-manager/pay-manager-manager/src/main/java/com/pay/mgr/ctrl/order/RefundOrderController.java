package com.pay.mgr.ctrl.order;

import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.service.impl.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
