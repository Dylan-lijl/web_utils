package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.util.ObjectUtils;

import java.util.Map;

/**
 * 字符串替换
 *
 * @author admin
 * @version 1.0
 */
public class ReplaceExportFileValueConvertor implements ExportFileValueConvertor {

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.REPLACE;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        Map<String, String> map = convertor.getValueMap();
        String src = map.get("src");
        String dest = map.get("dest");
        String type = convertor.getExtMap().get("type");
        if (!ObjectUtils.isBlank(src) && dest != null && value instanceof String) {
            return ObjectUtils.isBlank(type) || "1".equals(type) ? ((String) value).replace(src, dest) : ((String) value).replaceAll(src, dest);
        }
        return value;
    }
}
