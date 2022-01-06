package pub.carzy.util;


import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类的工具类
 *
 * @author admin
 */
public class BeanHandler {
    /**
     * 构造器缓存
     */
    private static final Map<Class<?>, Constructor<?>> constructorMap = new HashMap<>();

    /**
     * 复制对象
     *
     * @param src  源对象
     * @param dest 目标class
     * @param <T>  目标对象class
     * @return 目标对象
     */
    @NonNull
    public static <T> T copy(Object src, Class<T> dest) {
        //获取构造器
        Constructor<?> constructor = getConstructor(dest);
        //执行构造实例对象
        try {
            T t = (T)constructor.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(src,t);
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //实例化失败,抛异常

        }
        return null;
    }

    /**
     * 获取构造器
     * @param dest class
     * @return 构造器
     */
    private static Constructor<?> getConstructor(Class<?> dest) {
        Constructor<?> constructor = constructorMap.get(dest);
        if (constructor == null) {
            synchronized (constructorMap) {
                if (constructorMap.get(dest) == null) {
                    try {
                        constructorMap.put(dest, dest.getConstructor());
                    } catch (NoSuchMethodException e) {
                        //抛异常,没有无参构造方法

                    }
                }
            }
            constructor = constructorMap.get(dest);
        }
        return constructor;
    }

}
