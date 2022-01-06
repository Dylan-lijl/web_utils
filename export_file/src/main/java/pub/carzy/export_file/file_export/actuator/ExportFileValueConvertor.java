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
     * @param convertor
     * @return
     */
    boolean match(ExportValueFormat convertor);

    /**
     * 转换值
     *
     * @param convertor
     * @param value
     * @return
     */
    Object formatValue(ExportValueFormat convertor, Object value);

    /**
     * 默认实现
     * @return
     */
    @Override
    default int getOrder() {
        return 0;
    }
}
