package com.pay.pay.service.mapper;

import com.pay.pay.core.entity.MchNotifyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商户通知记录表 Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
public interface MchNotifyRecordMapper extends BaseMapper<MchNotifyRecord> {

    Integer updateNotifyResult(@Param("notifyId") Long notifyId, @Param("state") Byte state, @Param("resResult") String resResult);


    /**
     * 功能描述：更改通知中 & 增加允许重发通知次数
     * @param notifyId
     * @return
     */
    Integer updateIngAndAddNotifyCountLimit(@Param("notifyId")Long notifyId);

}
