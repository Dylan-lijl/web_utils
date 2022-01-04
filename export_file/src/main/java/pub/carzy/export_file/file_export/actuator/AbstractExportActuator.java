package pub.carzy.export_file.file_export.actuator;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.file_export.MetaInfo;
import pub.carzy.export_file.file_export.MetaInfoField;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.entity.ExportTitle;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 抽象处理器
 *
 * @author admin
 */
public abstract class AbstractExportActuator implements ExportActuator {
    protected ExportRequestParam param;
    protected ExportActuatorParam actuatorParam;
    protected Object data;
    protected ProceedingJoinPoint point;
    protected File file;
    protected static final Map<Class<?>, MetaInfo> infoMap = new HashMap<>();
    protected List<ExportTitle> titles;
    /**
     * 转换器
     */
    private static ServiceLoader<ExportFileValueConvertor> convertors;

    static {
        convertors = ServiceLoader.load(ExportFileValueConvertor.class);
    }

    public AbstractExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        this.param = param;
        this.data = data;
        this.point = point;
        this.actuatorParam = actuatorParam;
    }

    /**
     * 如果 param里面含有titles并且长度大于0,则使用里面的,否则使用对象的
     *
     * @return
     */
    @Override
    public List<ExportTitle> getTitles() {
        return param.getTitles() == null || param.getTitles().size() <= 0 ? doGetTitles() : param.getTitles();
    }

    /**
     * 获取标题
     *
     * @return
     */
    protected List<ExportTitle> doGetTitles() {
        return new ArrayList<>();
    }

    @Override
    public String getFilepath() {
        return file == null ? null : file.getName();
    }

    @Override
    public void close() throws IOException {
        doClose();
    }

    /**
     * 关闭
     */
    protected abstract void doClose();

    /**
     * @param title
     * @param value
     * @return 值(不可能返回null)
     */
    protected Object transformValue(ExportTitle title, Object value) {
        ExportValueFormat format = title.getConvertor();
        if (format == null) {
            return value;
        }
        for (ExportFileValueConvertor convertor : convertors) {
            if (convertor.match(format)){
                return convertor.formatValue(format,value);
            }
        }
        return value;
    }

    protected MetaInfo getMetaInfo(Class<?> aClass) {
        if (infoMap.get(aClass) == null) {
            synchronized (infoMap) {
                if (infoMap.get(aClass) == null) {
                    //分析class
                    MetaInfo info = new MetaInfo();
                    info.setFields(new ArrayList<>());
                    Set<Field> fieldSet = new HashSet<>();
                    ObjectUtils.getAllFields(aClass, fieldSet);
                    for (Field field : fieldSet) {
                        MetaInfoField infoField = new MetaInfoField();
                        infoField.setName(field.getName());
                        try {
                            //使用get方法获取数据
                            Method method = aClass.getMethod("get" + ObjectUtils.uppercaseFirst(field.getName()));
                            infoField.setCallback(target -> {
                                try {
                                    return method.invoke(target);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    return null;
                                }
                            });
                        } catch (NoSuchMethodException e) {
                            //直接使用字段获取数据
                            infoField.setCallback(target -> {
                                field.setAccessible(true);
                                try {
                                    return field.get(target);
                                } catch (IllegalAccessException ex) {
                                    return null;
                                }
                            });
                        }
                        info.getFields().add(infoField);
                    }
                    //添加到缓存
                    infoMap.put(aClass, info);
                }
            }
        }
        return infoMap.get(aClass);
    }

}
