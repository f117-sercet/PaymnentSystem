package com.pay.payMbg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.payMbg.service.mapper.MchDivisionReceiverGroupMapper;
import org.springframework.stereotype.Service;
import  com.pay.pay.core.entity.*;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2022/8/5 17:42
 */
@Service
public class MchDivisionReceiverGroupService extends ServiceImpl<MchDivisionReceiverGroupMapper, MchDivisionReceiverGroup> {

    /** 根据ID和商户号查询 **/
    public MchDivisionReceiverGroup findByIdAndMchNo(Long groupId, String mchNo){
        return getOne(MchDivisionReceiverGroup.gw().eq(MchDivisionReceiverGroup::getReceiverGroupId, groupId).eq(MchDivisionReceiverGroup::getMchNo, mchNo));
    }


}
