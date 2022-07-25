package com.pay.pay.service.mapper;

import com.pay.pay.core.entity.PayOrderDivisionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 分账记录表 Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
public interface PayOrderDivisionRecordMapper extends BaseMapper<PayOrderDivisionRecord> {


               /*************查询全部分账成功金额************/

              Long sumSuccessDivisionAmount(String payOrderOd);
}
