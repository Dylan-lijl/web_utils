package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * 字符串前缀拼接
 *
 * @author admin
 */
public class PrefixExportFileValueConvertor implements ExportFileValueConvertor {

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.PREFIX;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        return convertor.getValue() + value.toString();
    }
}
