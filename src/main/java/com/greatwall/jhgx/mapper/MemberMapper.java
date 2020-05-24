package com.greatwall.jhgx.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.greatwall.jhgx.domain.Member;
import com.greatwall.jhgx.domain.PayOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper extends SuperMapper<Member> {

    List<PayOrder> selectOrder(String authCode);

    List<Member> memberStatistics(@Param("md") Member member, IPage<Member> page);
}
