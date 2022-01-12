package pub.carzy.export_file.spring_bean;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

/**
 * 修改aop的切点表达式
 *
 * @author admin
 * @version 1.0
 */
public class AopExpressionBeanPostProcess implements BeanFactoryPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            String property = context.getEnvironment().getProperty("web.export.aop-expression");
            if (property == null||"".equals(property)){
                return;
            }
            Method aroundMethod = AopExportFileConfig.class.getDeclaredMethod("webPointcut");
            Pointcut pointcut = aroundMethod.getAnnotation(Pointcut.class);
            if (Objects.isNull(pointcut)) {
                return;
            }
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(pointcut);
            // 获取 AnnotationInvocationHandler 的 memberValues 字段
            Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
            declaredField.setAccessible(true);
            Map<String, Object> valMap = (Map<String, Object>) declaredField.get(invocationHandler);
            valMap.put("value", property);
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
