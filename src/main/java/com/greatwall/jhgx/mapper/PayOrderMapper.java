package com.greatwall.jhgx.mapper;

import com.greatwall.jhgx.domain.PayOrder;

import java.util.List;

public interface PayOrderMapper extends SuperMapper<PayOrder> {

    List<PayOrder> selectOrder(String authCode);
}
