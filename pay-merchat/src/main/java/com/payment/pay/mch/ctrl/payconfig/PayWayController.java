/*
 * Copyright (c) 2021-2031, 河北计全科技有限公司 (https://www.jeequan.com & jeequan@126.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payment.pay.mch.ctrl.payconfig;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.pay.core.entity.PayWay;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.MchPayPassageService;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.pay.service.impl.PayWayService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 支付方式配置类
 *
 */
@RestController
@RequestMapping("api/payWays")
public class PayWayController extends CommonCtrl {

	@Resource
	PayWayService payWayService;
	@Resource
	MchPayPassageService mchPayPassageService;
	@Resource
	PayOrderService payOrderService;

	/**
	 * @Author: 段世超
	 * @Description: list
	 * @Date: 15:52 2021/4/27
	 */
	@PreAuthorize("hasAuthority('ENT_PAY_ORDER_SEARCH_PAY_WAY')")
	@GetMapping
	public ApiRes list() {

		PayWay queryObject = getObject(PayWay.class);

		LambdaQueryWrapper<PayWay> condition = PayWay.gw();
		if(StringUtils.isNotEmpty(queryObject.getWayCode())){
			condition.like(PayWay::getWayCode, queryObject.getWayCode());
		}
		if(StringUtils.isNotEmpty(queryObject.getWayName())){
			condition.like(PayWay::getWayName, queryObject.getWayName());
		}
		condition.orderByAsc(PayWay::getWayCode);

		IPage<PayWay> pages = payWayService.page(getIPage(true), condition);

		return ApiRes.page(pages);
	}


}
