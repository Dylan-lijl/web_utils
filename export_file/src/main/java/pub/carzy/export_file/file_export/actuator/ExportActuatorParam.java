package pub.carzy.export_file.file_export.actuator;

import lombok.Data;

/**
 * @author admin
 */
@Data
public class ExportActuatorParam {
    /**
     * 文件路径
     */
    private String commonFilePath;
    /**
     * 前缀
     */
    private String prefix;
}
