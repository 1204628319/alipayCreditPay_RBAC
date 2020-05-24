package com.greatwall.jhgx.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("upload_image")
public class UploadImage implements Serializable {
    @TableId
    private Long id;
    private String merchantId;
    private String certId;
    private String imageType;
    private String imageDefinition;
    private String image;
    private String createTime;

    @TableField(exist = false)
    private String imageDefinitionName;
}
