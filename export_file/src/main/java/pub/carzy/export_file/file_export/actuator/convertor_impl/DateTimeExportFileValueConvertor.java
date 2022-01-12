package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间类型转换: yyyy-MM-dd HH:mm:ss
 *
 * @author admin
 * @version 1.0
 */
public class DateTimeExportFileValueConvertor implements ExportFileValueConvertor {
    /**
     * 时间转换
     */
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.DATETIME;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        if (value instanceof Date){
            return formatter.format(value);
        }
        return value;
    }
}
