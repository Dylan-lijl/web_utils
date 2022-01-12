package pub.carzy.export_file.template;

import org.springframework.core.Ordered;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.FileWriter;

/**
 * 创建writer工厂
 * @author admin
 * @version 1.0
 */
public interface FileWriteFactory extends Ordered {
    /**
     * 匹配
     *
     * @param type 类型
     * @return 是否匹配
     */
    boolean match(Integer type);

    /**
     * 创建对象
     * @param param 绝对路径
     * @return 对象
     */
    FileWriter createWriter(ExportActuatorParam param);
}
