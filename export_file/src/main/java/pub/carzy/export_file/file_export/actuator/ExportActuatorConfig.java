package pub.carzy.export_file.file_export.actuator;

import lombok.Data;
import pub.carzy.export_file.template.FileWriteFactory;

import java.util.List;

/**
 * 执行器配置
 * @author admin
 */
@Data
public class ExportActuatorConfig {
    private List<ExportFileValueConvertor> convertors;
    private List<FileWriteFactory> factories;
}
