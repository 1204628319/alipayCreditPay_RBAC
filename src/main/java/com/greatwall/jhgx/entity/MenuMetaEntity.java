package com.greatwall.jhgx.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jie
 * @date 2018-12-20
 */
@Data
@AllArgsConstructor
public class MenuMetaEntity implements Serializable {

    private String title;

    private String icon;
}
