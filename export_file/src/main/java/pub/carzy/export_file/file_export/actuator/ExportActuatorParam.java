package pub.carzy.export_file.file_export.actuator;

import lombok.Data;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

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
    /**
     * 请求参数
     */
    private ExportRequestParam param;
}
