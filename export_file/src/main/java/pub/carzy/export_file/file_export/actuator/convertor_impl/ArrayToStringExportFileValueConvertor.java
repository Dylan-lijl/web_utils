package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * 数组转成字符串
 *
 * @author admin
 */
public class ArrayToStringExportFileValueConvertor implements ExportFileValueConvertor {
    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.ARRAY_TO_STRING;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        if (value instanceof String) {
            StringBuilder builder = new StringBuilder();
            String[] split = ((String) value).split("\\|");
            for (String line : split) {
                builder.append(line.replace("~123~", "|")).append("\n");
            }
            //去除最后一个回车
            return builder.length() > 0 && builder.charAt(builder.length() - 1) == '\n' ? builder.toString().substring(0, builder.length() - 1) : builder.toString();
        }
        return null;
    }
}
