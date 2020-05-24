package com.greatwall.jhgx.service;

import com.greatwall.component.ccyl.common.service.ISuperService;
import com.greatwall.jhgx.domain.PayOrder;

import java.util.List;

public interface PayOrderService extends ISuperService<PayOrder> {
    List<PayOrder> selectOrder(String authCode);
}
