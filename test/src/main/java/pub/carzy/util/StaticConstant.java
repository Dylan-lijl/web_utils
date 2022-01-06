package pub.carzy.util;

/**
 * 业务常量
 *
 * @author admin
 */
public class StaticConstant {
    /**
     * 已删除
     */
    public static final int DELETED = 1;
    /**
     * 未删除状态
     */
    public static final int NOT_DELETE = 0;
    /**
     * 已禁用
     */
    public static final int DISABLED = 0;
    /**
     * 已启用
     */
    public static final int ENABLED = 1;
    /**
     * 隐藏
     */
    public static final int HIDDEN = 1;
    /**
     * 默认空id
     */
    public static final long DEFAULT_EMPTY_ID = 0L;
    /**
     * 显示
     */
    public static final int SHOW = 0;
    /**
     * 不是公有
     */
    public static final int NOT_PUBLICLY_OWNED = 0;
    /**
     * 用户
     */
    public static final int ROLE_USER = 1;
    /**
     * 用户组
     */
    public static final int ROLE_USER_GROUP = 2;
    /**
     * 是公有
     */
    public static final int PUBLICLY_OWNED = 1;
    /**
     * 登录的缓存key
     */
    public static final String LOGIN_KEY = "login_";
    /**
     * id
     */
    public static final String LOGIN_KEY_ID = "login_id_";
    /**
     *登录后的token缓存key
     */
    public static final String LOGIN_TOKEN_KEY = "login_token_";
    /**
     * 过期时间
     */
    public static final String LOGIN_TOKEN_KEY_TIME = "login_token_time_";
    /**
     * 用户id
     */
    public static final String LOGIN_TOKEN_ID_KEY = "login_token_key_id_";
    /**
     * 用户菜单权限缓存
     */
    public static final String USER_MENU_PERMISSION_CACHE = "user_menu_permission_cache_";
    /**
     * 用户资源权限缓存
     */
    public static final String USER_RESOURCE_PERMISSION_CACHE = "user_resource_permission_cache_";
    /**
     * 厂商详情缓存
     */
    public static final String VENDOR_INFO_KEY = "vendor_info_";
    /**
     * vlan全部标识
     */
    public static final String CONTENT_VLAN_ALL = "全部";
    /**
     * 通用
     */
    public static final int UNIVERSAL = 1;
    /**
     * 不普遍
     */
    public static final int NOT_UNIVERSAL = 0;
}
