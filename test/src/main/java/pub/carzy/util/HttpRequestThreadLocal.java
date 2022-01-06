package pub.carzy.util;


import pub.carzy.api.ResultCodeEnum;

/**
 * http请求的线程变量
 *
 * @author admin
 */
public class HttpRequestThreadLocal {
    /**
     * 线程变量
     */
    private static final ThreadLocal<Object> ATTRIBUTE = new ThreadLocal<>();

    /**
     * 获取里面的数据
     *
     * @return 获取绑定对象
     */
    public static Object getAttribute() {
        return getAttribute(Object.class);
    }

    /**
     * 获取里面的数据
     *
     * @param c 需要校验的class
     * @return 对象
     */
    public static Object getAttribute(Class<?> c) {
        if (ATTRIBUTE.get() == null || !c.isAssignableFrom(ATTRIBUTE.get().getClass())) {
            ExceptionHandler.throwException(ResultCodeEnum.SYSTEM_EXCEPTION.getCode());
            return null;
        }
        return ATTRIBUTE.get();
    }

    /**
     * 设置变量
     *
     * @param o
     */
    public static void setAttribute(Object o) {
        ATTRIBUTE.set(o);
    }

    /**
     * 清除
     */
    public static void remove() {
        ATTRIBUTE.remove();
    }

}
