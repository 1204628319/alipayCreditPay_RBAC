package com.greatwall.jhgx.service.impl;

import com.greatwall.component.ccyl.common.service.impl.SuperServiceImpl;
import com.greatwall.jhgx.domain.PayOrder;
import com.greatwall.jhgx.domain.UploadImage;
import com.greatwall.jhgx.mapper.PayOrderMapper;
import com.greatwall.jhgx.mapper.UploadImageMapper;
import com.greatwall.jhgx.service.PayOrderService;
import com.greatwall.jhgx.service.UploadImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadImageServiceImpl extends SuperServiceImpl<UploadImageMapper, UploadImage> implements UploadImageService {

    @Override
    public List<PayOrder> selectOrder(String authCode) {
        return baseMapper.selectOrder(authCode);
    }
}
