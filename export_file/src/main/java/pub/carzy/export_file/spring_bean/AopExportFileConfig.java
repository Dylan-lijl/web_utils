package pub.carzy.export_file.spring_bean;

import cn.hutool.core.collection.ConcurrentHashSet;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pub.carzy.export_file.exce.ExportFileEmptyException;
import pub.carzy.export_file.exce.ExportNotSupportedException;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.ExportFiler;
import pub.carzy.export_file.file_export.ExportMethod;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.template.ExportAopCallback;
import pub.carzy.export_file.util.ExceptionMessage;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拦截aop
 *
 * @author admin
 * @version 1.0
 */
@Component
@Aspect
@Import({ExportBeans.class,WebEnvConfig.class})
public class AopExportFileConfig {

    /**
     * 可以导出的方法
     */
    private final Map<Method, ExportMethod> exportMethods = new ConcurrentHashMap<>();
    /**
     * 不能导出的方法
     */
    private final Set<Method> skipMethods = new ConcurrentHashSet<>();

    @Resource
    private List<ExportFiler> filers;

    /**
     * 回调
     */
    @Resource
    private ExportAopCallback callback;

    /**
     * 切所有的额controller
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void webPointcut() {
    }

    /**
     * 获取注解
     *
     * @param method 方法
     * @return 注解
     */
    private ExportMethod getMethodAnnotation(Method method) {
        if (exportMethods.containsKey(method)) {
            return exportMethods.get(method);
        }
        //进行解析
        synchronized (exportMethods) {
            if (exportMethods.containsKey(method)) {
                return exportMethods.get(method);
            }
            ExportMethod exportMethod = method.getAnnotation(ExportMethod.class);
            //放入不能导出的方法里面去
            if (exportMethod == null) {
                skipMethods.add(method);
                //抛异常
                throw new ExportNotSupportedException(ExceptionMessage.EXPORT_NOT_SUPPORTED);
            }
            exportMethods.put(method, exportMethod);
            return exportMethod;
        }
    }

    /**
     * 获取方法
     *
     * @param joinPoint 注入点
     * @return 原始方法
     */
    private Method getTargetMethod(JoinPoint joinPoint) {
        Signature s = joinPoint.getSignature();
        if (!(s instanceof MethodSignature)) {
            throw new SystemErrorException(ExceptionMessage.SYSTEM_EXCEPTION);
        }
        MethodSignature signature = (MethodSignature) s;
        return signature.getMethod();
    }

    @Around(value = "webPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        //获取导出对象
        ExportRequestParam exportRequestParam = callback.getExportParam(args);
        if (exportRequestParam == null) {
            return point.proceed();
        }
        Method method = getTargetMethod(point);
        if (skipMethods.contains(method)) {
            throw new ExportNotSupportedException(ExceptionMessage.EXPORT_NOT_SUPPORTED);
        }
        //解析参数并将属性封装
        ExportMethod exportMethod = getMethodAnnotation(method);
        if (ObjectUtils.isEmpty(exportRequestParam.getFilename())) {
            exportRequestParam.setFilename(exportMethod.filename());
        }
        if (exportRequestParam.getFileType() == null) {
            exportRequestParam.setFileType(exportMethod.fileType());
        }
        //更改默认条数
        callback.updatePageSize(args);
        //执行目标方法获取返回的数据
        Object data = point.proceed(args);
        Object exportResult = null;
        for (ExportFiler filer : filers) {
            //匹配到对应的处理器就执行完并跳出循环
            if (filer.match(exportRequestParam, data)) {
                exportResult = filer.export(exportRequestParam, data, point);
                break;
            }
        }
        if (ObjectUtils.isEmpty(exportResult)) {
            throw new ExportFileEmptyException();
        }
        return callback.responseResult(exportRequestParam, point, exportResult);
    }

}
