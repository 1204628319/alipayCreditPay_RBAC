package com.greatwall.jhgx.constants;

/**
 * Security 权限常量
 * @author TianLei
 */
public interface DefaultSecurityConstants {
    /**
     * 用户信息分隔符
     */
    String USER_SPLIT = ":";

    /**
     * 用户信息头
     */
    String USER_HEADER = "x-user-header";

    /**
     * 生成验证码url
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/imageCode";

    /**
     * 刷新token
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 默认生成图形验证码宽度
     */
    String DEFAULT_IMAGE_WIDTH = "150";

    /**
     * 默认生成图像验证码高度
     */
    String DEFAULT_IMAGE_HEIGHT = "53";

    /**
     * 默认生成图形验证码长度
     */
    String DEFAULT_IMAGE_LENGTH = "4";

    /**
     * 默认生成图形验证码过期时间
     */
    int DEFAULT_IMAGE_EXPIRE = 2 * 60;

    /**
     * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
     */
    String DEFAULT_COLOR_FONT = "blue";

    /**
     * 图片边框
     */
    String DEFAULT_IMAGE_BORDER = "no";

    /**
     * 默认图片间隔
     */
    String DEFAULT_CHAR_SPACE = "5";

    /**
     * 默认保存code的前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";

    /**
     * 设备ID编号
     */
    String DEVICE_ID_HEADER_KEY = "deviceId";

    /**
     * 验证码文字大小
     */
    String DEFAULT_IMAGE_FONT_SIZE = "45";

    /**
     * portal公共前缀
     */
    String PORTAL_PREFIX = "portal:";

    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    String CACHE_CLIENT_KEY = "oauth_client_details";


    /**
     * 国密SM4 ECM加密模式秘钥
     **/
    String SM4_ENCRYPT_ECB_KEY = "pFVKp/9okHLHYZlA/dF43w==";

    /**
     * 默认保存token的前缀
     */
    String DEFAULT_TOKEN_KEY = "tokenId";

    /**
     * 缓存微信union_id
     */
    String CACHE_WECHAT_UNION_ID = "cache_wechat_union_id_%s";

    /**
     * 缓存微信open_id
     */
    String CACHE_WECHAT_OPEN_ID = "cache_wechat_open_id_%s";

    /**
     * 扫码登录类型
     */
    String SCAN_LOGIN_TYPE = "scan";


}
