package pub.carzy.export_file.file_export.actuator;


import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * 定义转换器接口
 * @author admin
 */
public interface ExportFileValueConvertor {
    /**
     * 匹配
     * @param convertor
     * @return
     */
    boolean match(ExportValueFormat convertor);

    /**
     * 转换值
     * @param convertor
     * @param value
     * @return
     */
    Object formatValue(ExportValueFormat convertor, Object value);
}
