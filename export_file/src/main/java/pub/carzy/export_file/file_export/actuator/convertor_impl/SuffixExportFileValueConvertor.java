package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * 字符串后缀拼接
 *
 * @version 1.0
 * @author admin
 */
public class SuffixExportFileValueConvertor implements ExportFileValueConvertor {

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.SUFFIX;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        return value.toString() + convertor.getValue();
    }
}
