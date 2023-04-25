package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.util.ObjectUtils;

/**
 * 字符串前缀拼接
 *
 * @author admin
 * @version 1.0
 */
public class SpliceExportFileValueConvertor implements ExportFileValueConvertor {

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.SPLICE;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        if (value != null) {
            String type = convertor.getExtMap().get("type");
            return ObjectUtils.isBlank(type) || "1".equals(type) ? convertor.getValue() + value : value + convertor.getValue();
        }
        return value;
    }
}
