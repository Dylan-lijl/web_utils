package pub.carzy.export_file;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.reflect.FieldAccessor;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @SpringBootTest
class ExportFileApplicationTests {

    public static final StringBuilder str = new StringBuilder("2222");
    private final Integer num = 100;

    @Test
    void contextLoads() {
    }

    @Override
    public String toString() {
        return "ExportFileApplicationTests{" +
                "num=" + num +",str="+str+
                '}';
    }

    @Test
    void testUpdateFinalValue(){
        ExportFileApplicationTests tests = new ExportFileApplicationTests();
        System.out.println(tests);
        Class<?> clazz = ExportFileApplicationTests.class;
        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        fieldList.addAll(Arrays.asList(clazz.getFields()));
        for (Field field : fieldList) {
            try {
                if ("str".equals(field.getName())) {
                    StringBuilder builder = new StringBuilder("3333");
                    field.set(null, builder);
                } else {
                    field.set(tests, Integer.valueOf(200));
                }
            } catch (IllegalAccessException e) {
                System.out.println("反射修改失败:"+e.getMessage());
            }
            int pre = field.getModifiers();
            try {
                field.setAccessible(true);
                Field modifiers = field.getClass().getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.set(field, field.getModifiers() & ~Modifier.FINAL);
                Field fieldAccessor = field.getClass().getDeclaredField("fieldAccessor");
                fieldAccessor.setAccessible(true);
                fieldAccessor.set(field,null);
                Field root = field.getClass().getDeclaredField("root");
                root.setAccessible(true);
                root.set(field,null);
                if ("str".equals(field.getName())) {
                    StringBuilder builder = new StringBuilder("4444");
                    field.set(null, builder);
                } else {
                    field.set(tests, Integer.valueOf(300));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("字段修饰符,pre:"+pre+",after:"+field.getModifiers());
        }
        System.out.println(tests);
    }
}
