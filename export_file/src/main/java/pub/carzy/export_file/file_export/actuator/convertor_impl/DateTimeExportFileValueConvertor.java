package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间类型转换: yyyy-MM-dd HH:mm:ss
 *
 * @author admin
 * @version 1.0
 */
public class DateTimeExportFileValueConvertor implements ExportFileValueConvertor {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间转换
     */
    private final SimpleDateFormat FORMATTER = new SimpleDateFormat(DEFAULT_PATTERN);

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.DATETIME;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        String pattern = convertor.getExtMap().get("pattern");
        if (pattern!=null){
            pattern = pattern.trim();
        }
        //如果等于默认值或者没有值就使用静态的格式化器
        if (ObjectUtils.isBlank(pattern) ||pattern.equals(DEFAULT_PATTERN)) {
            if (value instanceof Date) {
                return FORMATTER.format(value);
            }
        }else{
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            if (value instanceof Date) {
                return format.format(value);
            }
        }
        return value;
    }
}
