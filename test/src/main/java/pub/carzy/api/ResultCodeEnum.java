package pub.carzy.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举了一些常用API操作码
 *
 * @author macro
 * @date 2019/4/19
 */
public enum ResultCodeEnum implements CodeEnum {
    //内容
    SUCCESS(200, "成功"),
    SUCCESS_CREATE(201, "新增成功"),
    SUCCESS_UPDATE(202, "更新成功"),
    SUCCESS_DELETE(203, "删除成功"),
    SUCCESS_EXPORT(204, "生成导出文件成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或登录已经过期"),
    NAME_ALREADY_USED(501, "名称已被占用"),
    ADD_FAILED(900, "新增失败"),
    MODIFY_FAILED(901, "更新失败"),
    DELETE_FAILED(902, "删除失败"),
    NONE_DETAILS(903, "数据不存在"),
    DATA_CITED(904, "数据已被其他数据引用"),
    PARENT_NOT_FOUND(905, "引用的父级数据不存在"),
    PARENT_INCOMPATIBLE(906, "引用的父级数据不符合"),
    NOT_SUPPORTED_EXPORT(907, "当前请求不支持导出文件功能"),
    EXPORT_FILE_EMPTY(908, "导出文件失败:无对应文件的类型处理器"),
    PATH_ALREADY_USED(909, "资源路径已被占用"),
    UNAUTHORIZED_ACCESS(910, "非法访问"),
    CLONE_FAILED(911, "克隆失败"),
    UNREALIZED(912, "未实现"),
    ERROR_USER_NAME_OR_PASSWORD(801, "用户名或密码错误"),
    USER_IS_DISABLED(802, "用户已被禁用"),
    USER_NOT_FOUND(803, "用户不存在或未登录"),
    ILLEGALITY_UPDATE_SELF_PASSWORD(804, "不能通过该方式修改自己的密码"),
    FILE_NOT_FOUND(805, "文件不存在"),
    ORIGINAL_PASSWORD_IS_INCORRECT(806, "原密码不正确"),
    SYSTEM_EXCEPTION(-200, "系统异常"),
    APPLICATION_EXCEPTION(-100, "业务异常,通常是直接抛出普通异常并未指定code"),
    FORBIDDEN(403, "拒绝访问:无权限访问");
    /**
     * code
     */
    private final long code;
    /**
     * 消息
     */
    private final String message;
    /**
     * 缓存
     */
    public static final Map<String, ResultCodeEnum> CACHE = new HashMap<>();

    ResultCodeEnum(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultCodeEnum findByCode(String code) {
        if (CACHE.size() <= 0) {
            synchronized (CACHE) {
                if (CACHE.size() <= 0) {
                    for (ResultCodeEnum resultCode : ResultCodeEnum.values()) {
                        CACHE.put(resultCode.getCode() + "", resultCode);
                    }
                }
            }
        }
        return CACHE.get(code);
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
