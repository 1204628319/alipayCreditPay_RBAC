package com.greatwall.jhgx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.greatwall.component.ccyl.common.model.PageRequest;
import com.greatwall.component.ccyl.common.service.impl.SuperServiceImpl;
import com.greatwall.component.ccyl.common.utils.PageQueryBuilder;
import com.greatwall.jhgx.domain.Member;
import com.greatwall.jhgx.domain.PayOrder;
import com.greatwall.jhgx.mapper.MemberMapper;
import com.greatwall.jhgx.service.MemberService;
import com.greatwall.jhgx.util.NumberUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl extends SuperServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public List<PayOrder> selectOrder(String authCode) {
        return baseMapper.selectOrder(authCode);
    }

    @Override
    public IPage<Member> memberStatistics(Member member, PageRequest pageRequest) {
        pageRequest.setDefaultDesc(false);
        IPage<Member> page = new PageQueryBuilder<Member>().getPage(pageRequest);

        List<Member> members = baseMapper.memberStatistics(member, page);
        for (Member vo : members) {
            vo.setNotPayFee(NumberUtil.amtConvert(vo.getNotPayFee()));
            vo.setPayingFee(NumberUtil.amtConvert(vo.getPayingFee()));
            vo.setPayedFee(NumberUtil.amtConvert(vo.getPayedFee()));
            vo.setFailFee(NumberUtil.amtConvert(vo.getFailFee()));
            vo.setAbnormalFee(NumberUtil.amtConvert(vo.getAbnormalFee()));
            vo.setTotalFee(NumberUtil.amtConvert(vo.getTotalFee()));
        }
        page.setRecords(members);
        return page;
    }
}
