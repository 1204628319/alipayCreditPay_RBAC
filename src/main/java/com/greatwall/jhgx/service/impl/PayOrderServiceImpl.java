package com.greatwall.jhgx.service.impl;

import com.greatwall.component.ccyl.common.service.impl.SuperServiceImpl;
import com.greatwall.jhgx.domain.PayOrder;
import com.greatwall.jhgx.mapper.PayOrderMapper;
import com.greatwall.jhgx.service.PayOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayOrderServiceImpl extends SuperServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    @Override
    public List<PayOrder> selectOrder(String authCode) {
        return baseMapper.selectOrder(authCode);
    }
}
