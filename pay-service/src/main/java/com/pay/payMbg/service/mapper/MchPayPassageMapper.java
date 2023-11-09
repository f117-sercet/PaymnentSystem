package com.pay.payMbg.service.mapper;

import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.entity.MchPayPassage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户支付通道表 Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
public interface MchPayPassageMapper extends BaseMapper<MchPayPassage> {

    List<JSONObject> selectAvailAblePayInterfaceList(Map params);

}
