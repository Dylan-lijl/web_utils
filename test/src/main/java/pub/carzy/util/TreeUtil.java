package pub.carzy.util;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dylan Li
 * @date 2018-12-15
 */
@Data
@SuppressWarnings("unchecked")
public class TreeUtil<T> {

    public static <T> List<T> buildTree(List<T> list, Class<T> tClass) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        List<T> trees = new ArrayList<>();
        try {
            //查看是否有注解
            Field[] fields = tClass.getDeclaredFields();
            Field idField = null;
            Field pIdField = null;
            Field childField = null;
            for (Field field : fields) {
                Role role = field.getAnnotation(Role.class);
                if (role == null) {
                    continue;
                }
                //判断类型
                RoleType type = role.value();
                switch (type) {
                    case ID:
                        idField = field;
                        break;
                    case PARENT:
                        pIdField = field;
                        break;
                    case CHILD:
                        childField = field;
                        break;
                    default:
                        break;
                }
            }
            //如果没有注解则根据名称来
            Method idGetMethod = tClass.getMethod(getGetMethod(idField.getName()));
            Method parentIdGetMethod = tClass.getMethod(getGetMethod(pIdField.getName()));
            Method childGetMethod = tClass.getMethod(getGetMethod(childField.getName()));
            Method childSetMethod = tClass.getMethod(getSetMethod(childField.getName()), childGetMethod.getReturnType());
            for (T t : list) {
                //获取当前元素的主键
                Object mainPId = parentIdGetMethod.invoke(t, (Object[]) null);
                Object mainId = idGetMethod.invoke(t, (Object[]) null);
                //查找顶级元素
                if (StringUtils.isEmpty(mainPId)) {
                    trees.add(t);
                }

                for (T it : list) {
                    //获取元素的pid
                    Object itPId = parentIdGetMethod.invoke(it, (Object[]) null);
                    //如果pid等于id的话
                    if (!StringUtils.isEmpty(itPId) && !StringUtils.isEmpty(mainId) && ((Number) itPId).doubleValue() == ((Number) mainId).doubleValue()) {
                        Object invoke = childGetMethod.invoke(t, (Object[]) null);
                        if (invoke == null) {
                            invoke = new ArrayList<T>();
                            childSetMethod.invoke(t, list);
                        }
                        if (invoke instanceof List) {
                            List invoke1 = (List) invoke;
                            if (!invoke1.contains(it)) {
                                invoke1.add(it);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trees;
    }

    public static <T> List<T> buildTree(List<T> list, Class<T> tClass, String idStr, String pIdStr, String childrenStr) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        try {
            List<T> trees = new ArrayList<>();
            //获取泛型类
            Type superclass = list.getClass().getClass().getGenericSuperclass();
            if (superclass == null) {
                System.out.println("建树失败,获取不到对应的泛型类型");
                return list;
            }
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            if (parameterizedType == null) {
                System.out.println("建树失败,获取不到对应的泛型类型");
                return list;
            }
            Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
            //如果没有注解则根据名称来
            Method idGetMethod = clazz.getMethod(getGetMethod(idStr));
            Method parentIdGetMethod = clazz.getMethod(getGetMethod(pIdStr));
            Method childGetMethod = clazz.getMethod(getGetMethod(childrenStr));
            Method childSetMethod = clazz.getMethod(getSetMethod(childrenStr), childGetMethod.getReturnType());
            for (T t : list) {
                //获取当前元素的主键
                Object mainPId = parentIdGetMethod.invoke(t, (Object[]) null);
                Object mainId = idGetMethod.invoke(t, (Object[]) null);
                //查找顶级元素
                if (StringUtils.isEmpty(mainPId)) {
                    trees.add(t);
                }
                for (T it : list) {
                    //获取元素的pid
                    Object itPId = parentIdGetMethod.invoke(it, (Object[]) null);
                    //如果pid等于id的话
                    if (!StringUtils.isEmpty(itPId) && !StringUtils.isEmpty(mainId) && ((Number) itPId).doubleValue() == ((Number) mainId).doubleValue()) {
                        Object invoke = childGetMethod.invoke(t, (Object[]) null);
                        if (invoke == null) {
                            invoke = new ArrayList<T>();
                            childSetMethod.invoke(t, list);
                        }
                        if (invoke != null && invoke instanceof List) {
                            List invoke1 = (List) invoke;
                            if (!invoke1.contains(it)) {
                                invoke1.add(it);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> buildTree(List<T> list) {
        if (list == null || list.size() <= 1) {
            return list;
        }
        Class tClass = list.get(0).getClass();
        List<T> trees = new ArrayList<>();
        trees.addAll(list);
        list.clear();
        try {
            //查看是否有注解
            Field[] fields = tClass.getDeclaredFields();
            Field idField = null;
            Field pIdField = null;
            Field childField = null;
            for (Field field : fields) {
                Role role = field.getAnnotation(Role.class);
                if (role == null) {
                    continue;
                }
                //判断类型
                RoleType type = role.value();
                switch (type) {
                    case ID:
                        idField = field;
                        break;
                    case PARENT:
                        pIdField = field;
                        break;
                    case CHILD:
                        childField = field;
                        break;
                    default:
                        break;
                }
            }
            //
            if (idField == null||pIdField==null||childField==null){
                throw new RuntimeException("建树异常,缺少注解");
            }
            Method idGetMethod = tClass.getDeclaredMethod(getGetMethod(idField.getName()));
            Method parentIdGetMethod = tClass.getDeclaredMethod(getGetMethod(pIdField.getName()));
            Method childGetMethod = tClass.getDeclaredMethod(getGetMethod(childField.getName()));
            Method childSetMethod = tClass.getDeclaredMethod(getSetMethod(childField.getName()), childGetMethod.getReturnType());
            for (T t : trees) {
                //获取当前元素的主键
                Object mainPId = parentIdGetMethod.invoke(t, (Object[]) null);
                Object mainId = idGetMethod.invoke(t, (Object[]) null);
                //查找顶级元素 mainPId空或者等于0
                if (StringUtils.isEmpty(mainPId)||"0".equals(mainPId.toString())) {
                    list.add(t);
                }
                for (T item : trees) {
                    if (item == t){
                        continue;
                    }
                    //获取元素的pid
                    Object itemPId = parentIdGetMethod.invoke(item, (Object[]) null);
                    //如果pid等于id的话
                    if (!StringUtils.isEmpty(itemPId) && !StringUtils.isEmpty(mainId) && itemPId.toString().equals (mainId.toString())) {
                        Object invoke = childGetMethod.invoke(t, (Object[]) null);
                        if (invoke == null) {
                            invoke = new ArrayList<T>();
                            childSetMethod.invoke(t, invoke);
                        }
                        if (invoke instanceof List) {
                            List invoke1 = (List) invoke;
                            if (!invoke1.contains(item)) {
                                invoke1.add(item);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    public Object getParamsStr(T t, String str) {
        try {
            Method method = t.getClass().getMethod("get" + str, (Class<?>[]) null);
            return method.invoke(t, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setChildren(T t, List list, String str) {
        try {
            Method method = t.getClass().getMethod("set" + str, List.class);
            method.invoke(t, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSetMethod(String fieldName) {
        if (fieldName == null || "".equals(fieldName)) {
            return fieldName;
        }
        String s = (fieldName.charAt(0) + "").toUpperCase();
        return "set" + s + fieldName.substring(1, fieldName.length());
    }

    public static String getGetMethod(String fieldName) {
        if (fieldName == null || "".equals(fieldName)) {
            return fieldName;
        }
        String s = (fieldName.charAt(0) + "").toUpperCase();
        return "get" + s + fieldName.substring(1, fieldName.length());
    }

    /**
     * 属性角色
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Role {
        RoleType value();
    }

    public enum RoleType {
        //id 父级主键 子级
        ID, PARENT, CHILD
    }
}
