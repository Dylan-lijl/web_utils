package pub.carzy.export_file.file_export.actuator;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.exce.ExportNotSupportedException;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.MetaInfo;
import pub.carzy.export_file.file_export.MetaInfoField;
import pub.carzy.export_file.file_export.entity.ExportTitle;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.template.FileWriteFactory;
import pub.carzy.export_file.util.ObjectUtils;

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
    protected ExportActuatorParam actuatorParam;
    protected Object data;
    protected ProceedingJoinPoint point;
    protected static final Map<Class<?>, MetaInfo> infoMap = new HashMap<>();
    protected FileWriter writer;

    /**
     * 转换器
     */
    private ExportActuatorConfig config;

    public AbstractExportActuator(Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam, ExportActuatorConfig config) {
        this.data = data;
        this.point = point;
        this.actuatorParam = actuatorParam;
        this.config = config;
        //匹配到对应的文件处理器并实例化
        findWriter();
    }

    /**
     * 默认实现直接返回file对象
     *
     * @return file对象
     */
    @Override
    public Object getObject() {
        return writer.getFile();
    }

    /**
     * 找不到就抛异常
     */
    protected void findWriter() {
        for (FileWriteFactory factory : config.getFactories()) {
            if (factory.match(actuatorParam.getParam().getFileType())) {
                this.writer = factory.createWriter(actuatorParam);
                return;
            }
        }
        throw new ExportNotSupportedException();
    }

    /**
     * 如果 param里面含有titles并且长度大于0,则使用里面的,否则使用对象的
     *
     * @return 标题
     */
    @Override
    public List<ExportTitle> getTitles() {
        return actuatorParam.getParam().getTitles() == null || actuatorParam.getParam().getTitles().size() <= 0 ?
                doGetTitles() : actuatorParam.getParam().getTitles();
    }

    /**
     * 获取标题
     *
     * @return 标题
     */
    protected List<ExportTitle> doGetTitles() {
        return new ArrayList<>();
    }

    @Override
    public void close() throws IOException {
        doClose();
    }

    /**
     * 关闭
     */
    protected void doClose() throws IOException {
        if (this.writer != null) {
            this.writer.close();
        }
    }

    @Override
    public void createFile() {
        writer.createFile();
    }

    @Override
    public void writeTitles(List<ExportTitle> titles) {
        //转成list
        List<String> title = transformTitles(titles);
        writer.writeLine(title, this.actuatorParam.getParam().getTitleStyle());
    }

    protected List<String> transformTitles(List<ExportTitle> titles) {
        List<String> list = new ArrayList<>(titles.size());
        for (ExportTitle exportTitle : titles) {
            list.add(exportTitle.getTitle());
        }
        return list;
    }

    @Override
    public void writeContent() {
        List<List<Object>> values = transformContent();
        if (values != null && values.size() > 0) {
            writer.writeMany(values, this.actuatorParam.getParam().getValueStyle());
        }
    }

    /**
     * 提供默认转换方法,是转换对象成一条数据,这个按需重写
     *
     * @return 内容
     */
    protected List<List<Object>> transformContent() {
        if (data == null) {
            return null;
        }
        List<List<Object>> list = new ArrayList<>();
        List<Object> line = new ArrayList<>();
        list.add(line);
        parseAndPutObjectValue(line, data);
        return list;
    }

    /**
     * 解析并将值放入list
     *
     * @param line
     * @param obj
     */
    protected void parseAndPutObjectValue(List<Object> line, Object obj) {
        if (obj == null) {
            return;
        }
        MetaInfo metaInfo = getMetaInfo(obj.getClass());
        List<MetaInfoField> fields = metaInfo.getFields();
        for (ExportTitle title : actuatorParam.getParam().getTitles()) {
            for (MetaInfoField field : fields) {
                if (field.getName().equals(title.getName())) {
                    Object value = field.getCallback().getValue(obj);
                    line.add(transformValue(title, value));
                    break;
                }
            }
        }
    }

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
        for (ExportFileValueConvertor convertor : config.getConvertors()) {
            if (convertor.match(format)) {
                Object val = convertor.formatValue(format, value);
                return val == null ? "" : val;
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
