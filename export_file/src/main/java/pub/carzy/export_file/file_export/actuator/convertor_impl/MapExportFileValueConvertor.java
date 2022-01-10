package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * @author admin
 */
public class MapExportFileValueConvertor implements ExportFileValueConvertor {
    @Override
    public boolean match(ExportValueFormat convertor){
        return convertor.getType() == ConvertorType.MAP;
    }

    /**
     * 从映射里面获取
     *
     * @param convertor 转换器
     * @param value 来源值
     * @return 转换值(映射的值,可能为null)
     */
    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        //没有map数据直接返回原值
        if (convertor.getValueMap() == null||convertor.getValueMap().size()<=0){
            return value;
        }
        //返回映射的值
        if (convertor.getValueMap() != null && convertor.getValueMap().get(value.toString()) != null) {
            return convertor.getValueMap().get(value.toString());
        }
        //返回默认值
        return convertor.getValueMap().get("default") == null ? value : convertor.getValueMap().get("default");
    }
}
