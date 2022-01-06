package pub.carzy.export_config.high;

import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

/**
 * @author admin
 */
public class SPIFileValueConvertor implements ExportFileValueConvertor {

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == 6;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        return value+"-spi";
    }
}
