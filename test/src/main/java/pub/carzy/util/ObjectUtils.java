package pub.carzy.util;


import jdk.nashorn.internal.ir.annotations.Ignore;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对象操作类型
 */
public class ObjectUtils {
    /**
     * 忽略的方法
     */
    public static final String[] ignoreMethod;
    /**
     * 忽略的字段
     */
    public static final String[] ignoreField;
    /**
     * 标准基础对象
     */
    public static final Class[] simpleClasses;
    /**
     * 使用valueOf方法解析
     */
    public static final Class[] valueOfClasses;
    /**
     * 通过构造方法
     */
    public static final Class[] constructionClasses;
    public static final Class[] ignoreAnnotations;
    /**
     * sql语句
     */
    public static final String COUNT_STRING;

    static {
        Method[] methods = Object.class.getDeclaredMethods();
        ignoreMethod = new String[methods.length];
        for (int i = 0; i < methods.length; i++) {
            ignoreMethod[i] = methods[i].getName();
        }
        ignoreField = new String[]{};
        simpleClasses = new Class[]{Byte.class, Short.class, Integer.class, Long.class, Double.class, Float.class, Character.class, String.class, BigInteger.class, BigDecimal.class, Date.class};
        valueOfClasses = new Class[]{Short.class, Integer.class, Long.class, Double.class, Float.class, Boolean.class};
        constructionClasses = new Class[]{BigInteger.class, BigDecimal.class};
        ignoreAnnotations = new Class[]{Target.class, Retention.class, Documented.class};
        COUNT_STRING = "SELECT count(1) FROM ";
    }

    /**
     * 判断是否空
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (isNull(object)) {
            return true;
        }
        if (object instanceof Collection) {
            return ((Collection) object).size() <= 0;
        }
        if (object instanceof Map) {
            return ((Map) object).size() <= 0;
        }
        return "".equals(object.toString());
    }

    /**
     * 非空
     *
     * @param object
     * @return
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 判断是否为null
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 给对象注入值,如果未空则使用默认值
     * @param value 注入值
     * @param defaultValue 默认值
     * @param consumer 实例对象函数引用
     * @param <O> 传入的类型以及参数类型
     */
    public static <O> void setValueOrDefault(O value, O defaultValue, Consumer<O> consumer) {
        setValueOrDefault(value, defaultValue, consumer, e -> null);
    }

    /**
     * 给对象注入值,如果未空则使用默认值
     * @param value 注入值
     * @param defaultValue 默认值
     * @param consumer 实例对象函数引用
     * @param function 捕获执行异常,如果返回null就吃掉
     * @param <O> 传入的类型以及参数类型
     */
    public static <O> void setValueOrDefault(O value, O defaultValue, Consumer<O> consumer, Function<Exception, ? extends RuntimeException> function) {
        try {
            if (isEmpty(value)) {
                consumer.accept(defaultValue);
            } else {
                consumer.accept(value);
            }
        } catch (Exception e) {
            RuntimeException runtimeException = function.apply(e);
            if (runtimeException != null) {
                throw runtimeException;
            }
        }
    }


    /**
     * 非空
     *
     * @param object
     * @return
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * 判断是否是空白字符串
     *
     * @param object
     * @return
     */
    public static boolean isBlank(Object object) {
        if (isEmpty(object)) {
            return true;
        }
        return "".equals(object.toString().trim());
    }

    /**
     * 非空
     *
     * @param object
     * @return
     */
    public static boolean isNotBlank(Object object) {
        return !isBlank(object);
    }

    /**
     * 获取所有方法
     *
     * @param clazz
     * @return
     */
    public static void getAllMethods(Class clazz, Set<Method> allMethod) {
        if (isSimpleType(clazz)) {
            return;
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //过滤忽略的方法
            boolean isIgnore = false;
            for (String s : ignoreMethod) {
                if (method.getName().equals(s)) {
                    isIgnore = true;
                    break;
                }
            }
            if (isIgnore) {
                continue;
            }
            //过滤一些方法
            if (method.getAnnotation(Ignore.class) != null) {
                continue;
            }
            allMethod.add(method);
        }
        //查询父类方法
        Class superclass = clazz.getSuperclass();
        if (superclass != Object.class && superclass.getAnnotation(Ignore.class) == null) {
            getAllMethods(superclass, allMethod);
        }
    }

    /**
     * 是否是基础包装类型等等
     *
     * @param type1
     * @return
     */
    public static boolean isSimpleType(Class<?> type1) {
        boolean isSimple = false;
        for (Class c : simpleClasses) {
            if (type1 == c) {
                isSimple = true;
                break;
            }
        }
        return isSimple;
    }

    /**
     * 是否是集合或map
     *
     * @param type1
     * @return
     */
    public static boolean isMapOrCollection(Class<?> type1) {
        if (Collection.class.isAssignableFrom(type1)) {
            return true;
        } else {
            return Map.class.isAssignableFrom(type1);
        }
    }

    /**
     * 获取所有字段
     *
     * @param fields
     */
    public static void getAllFields(Class clazz, Set<Field> fields) {
        if (isSimpleType(clazz)) {
            return;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            //过滤忽略的方法
            boolean isIgnore = false;
            for (String s : ignoreField) {
                if (field.getName().equals(s)) {
                    isIgnore = true;
                    break;
                }
            }
            if (isIgnore) {
                continue;
            }
            //过滤一些方法
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            fields.add(field);
        }
        //查询父类方法
        Class superclass = clazz.getSuperclass();
        if (superclass != null && superclass != Object.class && superclass.getAnnotation(Ignore.class) == null) {
            getAllFields(superclass, fields);
        }
    }

    /**
     * 将第一个字母大写
     *
     * @param string
     * @return
     */
    public static String uppercaseFirst(String string) {
        if (string == null) {
            return null;
        }
        if ("".equals(string)) {
            return "";
        }
        if (string.length() <= 1) {
            return string.toUpperCase();
        }
        char[] chars = string.toCharArray();
        //首字母大写
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }

    /**
     * 获取常用
     *
     * @param type
     * @return
     */
    public static Class getCommonMapOrCollection(Class<?> type) {
        if (type == List.class) {
            return ArrayList.class;
        } else if (type == Map.class) {
            return LinkedHashMap.class;
        } else if (type == Set.class) {
            return LinkedHashSet.class;
        }
        return type;
    }

    /**
     * 获取基本对象数据
     *
     * @param type
     * @param o
     * @return
     */
    public static Object getSimpleValue(Class type, Object o, Annotation[] annotations) {
        if (type == String.class) {
            return o.toString();
        }
        Object value = null;
        boolean skip = false;
        for (Class clazz : valueOfClasses) {
            if (type == clazz) {
                try {
                    Method valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                    value = valueOf.invoke(null, o.toString());
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    ExceptionHandler.throwException("参数类型格式错误!");
                }
                skip = true;
                break;
            }
        }
        if (skip) {
            return value;
        }
        for (Class clazz : constructionClasses) {
            if (clazz == type) {
                try {
                    Constructor constructor = clazz.getConstructor(String.class);
                    value = constructor.newInstance(o.toString());
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    ExceptionHandler.throwException("参数类型格式错误!");
                }
                skip = true;
                break;
            }
        }
        if (skip) {
            return value;
        }

        //其他类型
        if (type == Date.class) {
            String pattern = null;
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof JsonFormat) {
                        JsonFormat format = (JsonFormat) annotation;
                        pattern = ("".equals(format.pattern())) ? format.value() : format.pattern();
                        break;
                    }
                }
            }
            if (pattern != null && !"".equals(pattern)) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                try {
                    value = sdf.parse(o.toString());
                } catch (ParseException e) {
                    ExceptionHandler.throwException("解析日期类型错误,pattern:" + pattern + ",value:" + o.toString());
                }
            } else {
                value = DateFormat.getDateTimeInstance().format(o.toString());
            }

        }
        return value;
    }

    /**
     * 获取注解
     *
     * @param clazz           源类
     * @param annotationClass 注解
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnnotationByClass(Class clazz, Class<T> annotationClass) {
        //获取clazz所有的注解
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationClass) {
                return (T) annotation;
            }
        }
        //遍历获取父级的注解
        for (Annotation annotation : annotations) {
            boolean skip = false;
            for (Class ignoreClass : ignoreAnnotations) {
                if (annotation.annotationType() == ignoreClass) {
                    skip = true;
                    break;
                }
            }
            if (skip) {
                continue;
            }
            //递归获取
            T byClass = getAnnotationByClass(annotation.annotationType(), annotationClass);
            if (byClass != null) {
                return byClass;
            }
        }
        Class superclass = clazz.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return getAnnotationByClass(superclass, annotationClass);
    }

    /**
     * 判断是否匹配
     *
     * @param aClass
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> boolean matchClass(Class<?> aClass, Class<T> clazz) {
        if (aClass == null) {
            return false;
        }
        if (aClass == clazz) {
            return true;
        }
        if (aClass == Object.class) {
            return false;
        }
        Class<?>[] interfaces = aClass.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            if (interfaceClass == clazz) {
                return true;
            }
            if (matchClass(interfaceClass, clazz)) {
                return true;
            }
        }
        return matchClass(aClass.getSuperclass(), clazz);
    }

    /**
     * 转换数据
     *
     * @param type
     * @param data
     * @param <T>
     * @return
     */
    public static <T> T transform(Class<T> type, Object data) {
        T value = null;
        if (type == String.class) {
            return (T) data.toString();
        }
        for (Class clazz : valueOfClasses) {
            if (type == clazz) {
                try {
                    Method valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                    value = (T) valueOf.invoke(null, data.toString());
                    if (value != null) {
                        return value;
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    ExceptionHandler.throwException("参数类型格式错误!");
                }
                break;
            }
        }
        for (Class clazz : constructionClasses) {
            if (clazz == type) {
                try {
                    Constructor constructor = clazz.getConstructor(String.class);
                    value = (T) constructor.newInstance(data.toString());
                    if (value != null) {
                        return value;
                    }
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    ExceptionHandler.throwException("参数类型格式错误!");
                }
                break;
            }
        }
        if (type == Date.class) {
            //先按照 yyyy-MM-dd HH:mm:ss 来解析
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                value = (T) sdf.parse(data.toString());
            } catch (ParseException e) {
                value = (T) DateFormat.getDateTimeInstance().format(data.toString());
            }
        }
        return value;
    }

    /**
     * 获取map或list
     *
     * @param type
     * @param o
     * @param annotations
     * @return
     */
    public static <T> T getCollectionOrMapValue(Class<T> type, Object o, Annotation[] annotations) {
        T obj = null;
        if (Collection.class.isAssignableFrom(type)) {
            Collection collection = null;
            if (List.class == type) {
                collection = new ArrayList();
            } else if (Set.class == type) {
                collection = new LinkedHashSet();
            } else {
                if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
                    try {
                        collection = (Collection) type.getConstructor().newInstance();
                    } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                        ExceptionHandler.throwException("实例化失败!");
                    }
                }
            }
            if (collection == null) {
                return null;
            }
            if (o instanceof Collection) {
                collection.addAll((Collection) o);
            } else {
                collection.add(o);
            }
            obj = (T) collection;
        } else if (Map.class == type) {
            Map map = new LinkedHashMap();
            if (o instanceof Map) {
                map.putAll((Map) o);
            }
            obj = (T) map;
        } else if (Map.class.isAssignableFrom(type)) {
            if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
                try {
                    Map map = (Map) type.getConstructor().newInstance();
                    if (o instanceof Map) {
                        map.putAll((Map) o);
                    }
                    obj = (T) map;
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    ExceptionHandler.throwException("实例化失败!");
                }
            }
        }
        return obj;
    }

    /**
     * 浅复制属性
     *
     * @param source
     * @param tClass
     * @param <T>
     * @return
     */
    public static final <T> T copyProperties(Object source, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            Set<Field> srcFields = new HashSet<>();
            Set<Field> destFields = new HashSet<>();
            getAllFields(source.getClass(), srcFields);
            getAllFields(tClass, destFields);
            Set<Method> srcMethods = new HashSet<>();
            Set<Method> destMethods = new HashSet<>();
            getAllMethods(source.getClass(), srcMethods);
            getAllMethods(tClass, destMethods);
            //过滤只要get方法
            srcMethods = srcMethods.stream().filter(method -> method.getName().startsWith("get")).collect(Collectors.toSet());
            //过滤只要set方法
            destMethods = destMethods.stream().filter(method -> method.getName().startsWith("set")).collect(Collectors.toSet());
            //双重遍历 如果名称和类型都相等并且获取get set方法不为空则进行赋值
            for (Field destField : destFields) {
                String destName = destField.getName();
                Class<?> destType = destField.getType();
                for (Field srcField : srcFields) {
                    String srcName = srcField.getName();
                    Class<?> srcType = srcField.getType();
                    if (destName.equals(srcName) && destType == srcType) {
                        String getMethodName = "get" + uppercaseFirst(srcName);
                        String setMethodName = "set" + uppercaseFirst(destName);
                        Method destMethod = null;
                        Method srcMethod = null;
                        //获取dest的set方法
                        for (Method method : destMethods) {
                            if (method.getName().equals(setMethodName)) {
                                destMethod = method;
                                break;
                            }
                        }
                        //获取src的get方法
                        for (Method method : srcMethods) {
                            if (method.getName().equals(getMethodName)) {
                                srcMethod = method;
                                break;
                            }
                        }
                        //没有方法则直接使用暴力方式获取和植入
                        Object value = null;
                        if (srcMethod != null) {
                            value = srcMethod.invoke(source);
                        } else {
                            srcField.setAccessible(true);
                            value = srcField.get(source);
                        }
                        if (value != null) {
                            if (destMethod != null) {
                                destMethod.invoke(t, value);
                            } else {
                                destField.setAccessible(true);
                                destField.set(t, value);
                            }
                        }
                        break;
                    }
                }
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException("创建实例失败!");
        }
    }

    /**
     * 拼接字符串
     *
     * @param collection
     * @param separator
     * @return
     */
    public static <T> String spliceSeparator(Collection<T> collection, String separator) {
        if (collection == null || collection.size() <= 0) {
            return null;
        }
        Iterator<T> iterator = collection.iterator();
        //遍历拼接
        if (collection.size() == 1) {
            return iterator.next().toString();
        }
        if (collection.size() == 2) {
            return iterator.next().toString() + separator + iterator.next();
        }
        int i = 0;
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(iterator.next().toString());
            if (i < collection.size() - 1) {
                builder.append(separator);
            }
            i++;
        }
        return builder.toString();
    }
}
