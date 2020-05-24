package com.greatwall.jhgx.mapper;

import com.greatwall.jhgx.domain.PayOrder;
import com.greatwall.jhgx.domain.UploadImage;

import java.util.List;

public interface UploadImageMapper extends SuperMapper<UploadImage> {

    List<PayOrder> selectOrder(String authCode);
}
