package pub.carzy.export_config.high;

import org.springframework.stereotype.Component;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * @author admin
 */
// @Component
public class SpringValueConvertor implements ExportFileValueConvertor {
    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType()==8;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        return value+"-spring";
    }
}
