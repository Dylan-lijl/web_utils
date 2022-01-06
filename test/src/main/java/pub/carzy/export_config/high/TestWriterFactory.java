package pub.carzy.export_config.high;

import org.springframework.stereotype.Component;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.FileWriter;
import pub.carzy.export_file.template.FileWriteFactory;

/**
 * @author admin
 */
@Component
public class TestWriterFactory implements FileWriteFactory {
    @Override
    public boolean match(Integer type) {
        return type == 4;
    }

    @Override
    public FileWriter createWriter(ExportActuatorParam param) {
        return new TestWriter(param);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
