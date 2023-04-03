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
package com.pay.payment.rqrs.payway;


import com.pay.pay.core.constants.CS;
import com.pay.payment.rqrs.payorder.UnifiedOrderRQ;
import lombok.Data;

/*
 * 支付方式： YSF_JSAPI
 *

 */
@Data
public class YsfJsapiOrderRQ extends UnifiedOrderRQ {

    /** 构造函数 **/
    public YsfJsapiOrderRQ(){
        this.setWayCode(CS.PAY_WAY_CODE.YSF_JSAPI);
    }

}
