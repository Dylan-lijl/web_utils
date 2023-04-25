package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.util.ObjectUtils;

/**
 * 数组转成字符串
 *
 * @author admin
 * @version 1.0
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
            String reg = convertor.getValue();
            String placeholder = convertor.getExtMap().get("placeholder");
            String separator = convertor.getExtMap().get("separator");
            String[] split = ((String) value).split(ObjectUtils.isEmpty(reg) ? "" : reg);
            for (String line : split) {
                builder.append(placeholder == null ? line : line.replace(placeholder, reg));
                if (separator != null) {
                    builder.append(separator);
                } else {
                    builder.append("\n");
                }
            }
            if (separator!=null&&builder.length()>separator.length()){
                int length = separator.length();
                CharSequence subSequence = builder.subSequence(builder.length() - length - 1, builder.length());
                if (subSequence.equals(separator)){
                    return builder.substring(builder.length() - length - 1);
                }
            }else if (builder.length()<=0){
                return builder.toString();
            }else{
                //去除最后一个回车
                return builder.charAt(builder.length() - 1) == '\n' ? builder.substring(0, builder.length() - 1) : builder.toString();
            }
        }
        return value;
    }
}
