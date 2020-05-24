package com.greatwall.jhgx.constants;

import java.text.Collator;
import java.util.Comparator;

/**
 * 管理员常量
 *
 * @author TianLei
 */
public interface AdminConstants {
    /**
     * 删除
     */
    String STATUS_DEL = "1";

    /**
     * 总父级id
     */
    Long TOTAL_PID = 1L;

    /**
     * 未删除
     */
    String STATUS_NOT_DEL = "0";

    /**
     * 目录
     */
    Integer CATALOG = -1;

    /**
     * 菜单
     */
    Integer MENU = 1;

    /**
     * 树的最高层数
     */
    Integer TREE_MAX_DEEP = 5;

    /**
     * 权限
     */
    Integer PERMISSION = 2;

    /**
     * 删除标记
     */
    String DEL_FLAG = "is_del";

    /**
     * 超级管理员用户名
     */
    String ADMIN_USER_NAME = "admin";

    /**
     * 默认超级管理员用户密码
     **/
    String DEF_ADMIN_PASSWORD = "gwi123456";

    /**
     * 默认非超级管理员用户密码
     **/
    String DEF_USER_PASSWORD = "111111";

    /**
     * 默认上级ID
     */
    Long DEFAULT_PARENT_ID = 0L;

    /**
     * 超级管理员的用户id
     */
    Long ADMIN_USER_ID = 1L;

    /**
     * 超级管理员角色id
     */
    Long ADMIN_ROLE_ID = 1L;

    /**
     * 超级管理员名称
     */
    String ADMIN_NAME = "超级管理员";
    /**
     * 平台审核
     */
    String ADMIN_AUDIT = "平台";

    /**
     * pid的key值
     */
    String ADMIN_PID_KEY = "pid";

    /**
     * 后台管理系统首页路径
     */
    String ADMIN_MAIN_PAGE_PATH = "index";

    /**
     * 管理后台系统布局组件
     */
    String ADMIN_LAYOUT_COMPONENT = "Layout";

    /**
     * 不重定向路径
     */
    String DONT_REDIRECT_PATH = "noredirect";

    /**
     * ID键值
     */
    String ADMIN_ID_KEY = "id";

    /**
     * 标签键值
     */
    String ADMIN_LABEL_KEY = "label";

    /**
     * 子节点键值
     */
    String ADMIN_CHILDREN_KEY = "children";

    /**
     * 扩展参数
     */
    String ADMIN_EXTRA = "extra";

    /**
     * 文件路径分隔符
     */
    String FILE_SEPARATOR = "/";

    /**
     * 机构类型-管理员机构
     */
    int ORG_TYPE_ADMIN = 0;

    /**
     * 机构类型-业务机构
     */
    int ORG_TYPE_BUSINESS = 1;

    /**
     * 逗号分隔符
     */
    String COMMA_SEPARAT = ",";

    /**
     * 汉字逗号分隔符
     */
    String CHINA_COMMA_SEPARAT = "，";

    /**
     * 金额和笔数默认值
     */
    String AMT_AND_CNT_DEF_VALUE = "0";

    /**
     * 字典类型-消息提醒类型
     */
    String DICT_VALUE_NOTIFY_TYPE = "NOTICE_TYPE";

    /**
     * 消息提醒类型-退费审核
     */
    String NOTICE_TYPE_REFUND_AUDIT = "REFUND_AUDIT";

    /**
     * 消息提醒类型标题-退费申请
     */
    String NOTICE_TYPE_REFUND_APPLY_TITLE = "有新增退费待审核记录待处理";

    /**
     * 消息提醒类型-对账帐不平
     */
    String NOTICE_TYPE_RECONCILIATION_NOT_NORMAL = "RECONCILIATION_EXCEPTION";

    /**
     * 消息提醒类型标题-对账帐不平
     */
    String NOTICE_TYPE_RECONCILIATION_NOT_NORMAL_TITLE = "有新增账不平记录待处理";

    /**
     * 对账批次临时的redis key
     **/
    String BATCH_REDIS_KEY = "BATCH_REDIS_KEY_";

    /**
     * 已删除
     */
    int DELETED = 1;

    /**
     * 中文排序
     */
    Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    /**
     * 冲正提示key
     **/
    String OFFSET_HINT_KEY = "offsetHint";

    /**
     * 创建时间
     **/
    String CREATE_AT = "CREATE_AT";

    /**
     * 第一级分割符
     **/
    String FIRST_SPLIT = "@@@1111@@@";

    /**
     * 第二级分割符
     **/
    String SECOND_SPLIT = "##2222##";

    /**
     * 资源根目录
     **/
    String STATIC_RESOURCE_DIR = "static/";

    /**
     * 差错分析附件路径
     **/
    String IMAGE_RESOURCE_DIR = "upload/image/";

    /**
     * 统一上传附件根路径
     **/
    String UPLOAD_RESOURCE_DIR = "upload/";

    /**
     * 统一上传附件文件夹名称规则
     */
    String UPLOAD_DIR_FORMAT = "yyyyMMdd";

    /**
     * 上传文件后缀分割符
     */
    String FILE_SPLIT_SUFFIX = ".";

    /**
     * 竖线
     **/
    String VERTICAL_LINE = "|";

    /**
     * 登录账号
     */
    String USER_NAME = "USER_NAME";

    /**
     * 用户名
     */
    String NICK_NAME = "NICK_NAME";

    /**
     * ISV编码
     */
    String ISV_CODE = "ISV_CODE";

    /**
     * 交易渠道
     */
    String TRADE_CHANNEL = "TRADE_CHANNEL";

    /**
     * 账单日期
     */
    String BILL_DATE = "BILL_DATE";

    /**
     * 账单模板表主键
     */
    String TEMP_ID = "TEMP_ID";

    /**
     * 退费审核-审核不通过
     */
    int AUDIT_NO_PASS = 0;

    /**
     * 退费审核-审核通过
     */
    int AUDIT_PASS = 1;
    /**
     * 退费审核-审核驳回
     */
    int AUDIT_REJECT = 2;

    /**
     * system用户id
     */
    Long SYSTEM_USER_ID = 2L;

    /**
     * 账单数据分隔方式--分隔符
     */
    String SPLIT_CHAR = "分隔符";

    /**
     * 账单数据分隔方式--固定宽度
     */
    String FIX_WIDTH = "固定宽度";

    /**
     * 账单数据分隔方式--可变宽度
     */
    String VARIABLE_WIDTH = "可变宽度";

    /**
     * 审核通过
     */
    String REFUND_AUDIT_PASS = "REFUND_AUDIT_PASS";

    /**
     * 审核不通过
     */
    String REFUND_AUDIT_NOPASS = "REFUND_AUDIT_NOPASS";

    /**
     * 审核拒绝
     */
    String REFUND_AUDIT_REJECT = "REFUND_AUDIT_REJECT";
}
