package com.pay.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.entity.PayOrderDivisionRecord;
import com.pay.pay.service.mapper.PayOrderDivisionRecordMapper;
import com.pay.pay.service.mapper.PayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /*****更新分账记录*****/
    @BatchDataSource
    public void updateRecordSuccessOrFail(List<PayOrderDivisionRecord> records, Byte state, String channelBatchOrderId, String channelRespResult){

        if(records == null || records.isEmpty()){
            return ;
        }

        List<Long> recordIds = new ArrayList<>();
        records.stream().forEach(r -> recordIds.add(r.getRecordId()));

        PayOrderDivisionRecord updateRecord = new PayOrderDivisionRecord();
        updateRecord.setState(state);
        updateRecord.setChannelBatchOrderId(channelBatchOrderId);
        updateRecord.setChannelRespResult(channelRespResult);
        update(updateRecord, PayOrderDivisionRecord.gw().in(PayOrderDivisionRecord::getRecordId, recordIds).eq(PayOrderDivisionRecord::getState, PayOrderDivisionRecord.STATE_WAIT));

    }
}
