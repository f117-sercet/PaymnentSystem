package com.pay.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.entity.PayOrderDivisionRecord;
import com.pay.pay.service.mapper.PayOrderDivisionRecordMapper;
import com.pay.pay.service.mapper.PayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description： 分账记录表
 *
 * @author: 段世超
 * @aate: Created in 2022/8/15 19:21
 */
@Service
public class PayOrderDivisionRecordService extends ServiceImpl<PayOrderDivisionRecordMapper, PayOrderDivisionRecord> {

    @Autowired
    private PayOrderMapper payOrderMapper;
}
