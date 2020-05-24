package com.greatwall.jhgx.domain;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greatwall.component.ccyl.common.model.SuperEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 后台用户信息表
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_SYS_USER")
public class TSysUser extends SuperEntity {

    /**
     * 登录账号
     */
    @TableField(condition = SqlCondition.LIKE)
    @Length(min = 6, max = 32, message = "账号长度在6到32之间")
    private String userName;

    /**
     * 微信UnionId
     */
    private String wxUnionId;

    /**
     * 微信OpenId
     */
    private String wxOpenId;

    /**
     * 用户名
     */
    @TableField(condition = SqlCondition.LIKE)
    @NotBlank(message = "用户名不能为空")
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话
     */
    @TableField(condition = SqlCondition.LIKE)
    @NotBlank(message = "手机号码不能为空")
    private String userPhone;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 性别
     */
    private Integer userSex;

    /**
     * 最后一次登陆时间
     */
    private Date lastLoginTime;

    /**
     * 登录次数
     */
    private Long loginCount;

    /**
     * 是否删除（1是0否）
     */
    @TableLogic
    private Boolean isDel = false;

    /**
     * 是否启用（1是2否）
     */
    @NotNull(message = "请设置是否开启")
    private Boolean enabled = true;

    /**
     * 服务渠道编码（当用户为渠道用户或system时，该字段有值）
     */
    private String isvCode;

    @TableField(exist = false)
    private List<TSysRole> roles;

    /**
     * 解绑
     */
    @TableField(exist = false)
    private boolean doUntying;

    /**
     * 机构名称
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String orgName;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private Long roleId;

    @TableField(exist = false)
    private Boolean isChecked = false;
}
