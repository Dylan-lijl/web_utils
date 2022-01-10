package pub.carzy.export_file.file_export.actuator;


import org.springframework.core.Ordered;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * 定义转换器接口
 *
 * @author admin
 */
public interface ExportFileValueConvertor extends Ordered {
    /**
     * 匹配
     *
     * @param convertor 转换器值
     * @return 是否能够转换
     */
    boolean match(ExportValueFormat convertor);

    /**
     * 转换值
     *
     * @param convertor 转换器值
     * @param value 内容
     * @return 转换值
     */
    Object formatValue(ExportValueFormat convertor, Object value);

    /**
     * 默认实现
     * @return 排序数字
     */
    @Override
    default int getOrder() {
        return 0;
    }
}
