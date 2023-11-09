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
package com.pay.payment.service;


import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.entity.MchInfo;
import com.pay.pay.core.entity.PayInterfaceConfig;
import com.pay.pay.core.model.params.IsvParams;
import com.pay.pay.core.model.params.IsvsubMchParams;
import com.pay.pay.core.model.params.NormalMchParams;
import com.pay.pay.core.model.params.alipay.AlipayIsvParams;
import com.pay.pay.core.model.params.alipay.AlipayNormalMchParams;
import com.pay.pay.core.model.params.pppay.PpPayNormalMchParams;
import com.pay.pay.core.model.params.wxpay.WxpayIsvParams;
import com.pay.pay.core.model.params.wxpay.WxpayNormalMchParams;
import com.pay.payMbg.service.impl.MchAppService;
import com.pay.payMbg.service.impl.MchInfoService;
import com.pay.payMbg.service.impl.PayInterfaceConfigService;
import com.pay.payMbg.service.impl.SysConfigService;
import com.pay.payment.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/*
* 配置信息查询服务 （兼容 缓存 和 直接查询方式）
*
* @author terrfly
* @site https://www.jeequan.com
* @date 2021/11/18 14:41
*/
@Slf4j
@Service
public class ConfigContextQueryService {

    @Resource
   private ConfigContextService configContextService;
    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private MchAppService mchAppService;
    @Resource
    private PayInterfaceConfigService payInterfaceConfigService;

    private boolean isCache(){
        return SysConfigService.IS_USE_CACHE;
    }

    public MchApp queryMchApp(String mchNo, String mchAppId){

        if(isCache()){
            return configContextService.getMchAppConfigContext(mchNo, mchAppId).getMchApp();
        }

        return mchAppService.getOneByMch(mchNo, mchAppId);
    }

    public MchAppConfigContext queryMchInfoAndAppInfo(String mchNo, String mchAppId){

        if(isCache()){
            return configContextService.getMchAppConfigContext(mchNo, mchAppId);
        }

        MchInfo mchInfo = mchInfoService.getById(mchNo);
        MchApp mchApp = queryMchApp(mchNo, mchAppId);

        if(mchInfo == null || mchApp == null){
            return null;
        }

        MchAppConfigContext result = new MchAppConfigContext();
        result.setMchInfo(mchInfo);
        result.setMchNo(mchNo);
        result.setMchType(mchInfo.getType());

        result.setMchApp(mchApp);
        result.setAppId(mchAppId);

        return result;
    }


    public NormalMchParams queryNormalMchParams(String mchNo, String mchAppId, String ifCode){

        if(isCache()){
            return configContextService.getMchAppConfigContext(mchNo, mchAppId).getNormalMchParamsByIfCode(ifCode);
        }

        // 查询商户的所有支持的参数配置
        PayInterfaceConfig payInterfaceConfig = payInterfaceConfigService.getOne(PayInterfaceConfig.gw()
                .select(PayInterfaceConfig::getIfCode, PayInterfaceConfig::getIfParams)
                .eq(PayInterfaceConfig::getState, CS.YES)
                .eq(PayInterfaceConfig::getInfoType, CS.INFO_TYPE_MCH_APP)
                .eq(PayInterfaceConfig::getInfoId, mchAppId)
                .eq(PayInterfaceConfig::getIfCode, ifCode)
        );

        if(payInterfaceConfig == null){
            return null;
        }

        return NormalMchParams.factory(payInterfaceConfig.getIfCode(), payInterfaceConfig.getIfParams());
    }


    public IsvsubMchParams queryIsvsubMchParams(String mchNo, String mchAppId, String ifCode){

        if(isCache()){
            return configContextService.getMchAppConfigContext(mchNo, mchAppId).getIsvsubMchParamsByIfCode(ifCode);
        }

        // 查询商户的所有支持的参数配置
        PayInterfaceConfig payInterfaceConfig = payInterfaceConfigService.getOne(PayInterfaceConfig.gw()
                .select(PayInterfaceConfig::getIfCode, PayInterfaceConfig::getIfParams)
                .eq(PayInterfaceConfig::getState, CS.YES)
                .eq(PayInterfaceConfig::getInfoType, CS.INFO_TYPE_MCH_APP)
                .eq(PayInterfaceConfig::getInfoId, mchAppId)
                .eq(PayInterfaceConfig::getIfCode, ifCode)
        );

        if(payInterfaceConfig == null){
            return null;
        }

        return IsvsubMchParams.factory(payInterfaceConfig.getIfCode(), payInterfaceConfig.getIfParams());
    }



    public IsvParams queryIsvParams(String isvNo, String ifCode){

        if(isCache()){
            IsvConfigContext isvConfigContext = configContextService.getIsvConfigContext(isvNo);
            return isvConfigContext == null ? null : isvConfigContext.getIsvParamsByIfCode(ifCode);
        }

        // 查询商户的所有支持的参数配置
        PayInterfaceConfig payInterfaceConfig = payInterfaceConfigService.getOne(PayInterfaceConfig.gw()
                .select(PayInterfaceConfig::getIfCode, PayInterfaceConfig::getIfParams)
                .eq(PayInterfaceConfig::getState, CS.YES)
                .eq(PayInterfaceConfig::getInfoType, CS.INFO_TYPE_ISV)
                .eq(PayInterfaceConfig::getInfoId, isvNo)
                .eq(PayInterfaceConfig::getIfCode, ifCode)
        );

        if(payInterfaceConfig == null){
            return null;
        }

        return IsvParams.factory(payInterfaceConfig.getIfCode(), payInterfaceConfig.getIfParams());

    }

    public AlipayClientWrapper getAlipayClientWrapper(MchAppConfigContext mchAppConfigContext){

        if(isCache()){
            return
                    configContextService.getMchAppConfigContext(mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId()).getAlipayClientWrapper();
        }

        if(mchAppConfigContext.isIsvsubMch()){

            AlipayIsvParams alipayParams = (AlipayIsvParams)queryIsvParams(mchAppConfigContext.getMchInfo().getIsvNo(), CS.IF_CODE.ALIPAY);
            return AlipayClientWrapper.buildAlipayClientWrapper(alipayParams);
        }else{

            AlipayNormalMchParams alipayParams = (AlipayNormalMchParams)queryNormalMchParams(mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId(), CS.IF_CODE.ALIPAY);
            return AlipayClientWrapper.buildAlipayClientWrapper(alipayParams);
        }

    }

    public WxServiceWrapper getWxServiceWrapper(MchAppConfigContext mchAppConfigContext){

        if(isCache()){
            return
                    configContextService.getMchAppConfigContext(mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId()).getWxServiceWrapper();
        }

        if(mchAppConfigContext.isIsvsubMch()){

            WxpayIsvParams wxParams = (WxpayIsvParams)queryIsvParams(mchAppConfigContext.getMchInfo().getIsvNo(), CS.IF_CODE.WXPAY);
            return WxServiceWrapper.buildWxServiceWrapper(wxParams);
        }else{

            WxpayNormalMchParams wxParams = (WxpayNormalMchParams)queryNormalMchParams(mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId(), CS.IF_CODE.WXPAY);
            return WxServiceWrapper.buildWxServiceWrapper(wxParams);
        }

    }

    public PaypalWrapper getPaypalWrapper(MchAppConfigContext mchAppConfigContext){
        if(isCache()){
            return
                    configContextService.getMchAppConfigContext(mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId()).getPaypalWrapper();
        }
        PpPayNormalMchParams ppPayNormalMchParams = (PpPayNormalMchParams) queryNormalMchParams(mchAppConfigContext.getMchNo(), mchAppConfigContext.getAppId(), CS.IF_CODE.PPPAY);;
        return PaypalWrapper.buildPaypalWrapper(ppPayNormalMchParams);

    }

}
