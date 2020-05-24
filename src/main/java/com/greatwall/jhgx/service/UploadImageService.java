package com.greatwall.jhgx.service;

import com.greatwall.component.ccyl.common.service.ISuperService;
import com.greatwall.jhgx.domain.PayOrder;
import com.greatwall.jhgx.domain.UploadImage;

import java.util.List;

public interface UploadImageService extends ISuperService<UploadImage> {
    List<PayOrder> selectOrder(String authCode);
}
