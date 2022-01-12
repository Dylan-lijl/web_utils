package pub.carzy.export_file.file_export.actuator;

import pub.carzy.export_file.file_export.entity.ExportTitle;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * 执行器
 * @author admin
 * @version 1.0
 */
public interface ExportActuator extends Closeable {
    /**
     * 获取标题
     * @return 标题
     */
    List<ExportTitle> getTitles();

    /**
     * 创建文件
     */
    void createFile();

    /**
     * 写标题
     * @param titles 标题
     */
    void writeTitles(List<ExportTitle> titles);

    /**
     * 写内容方法
     */
    void writeContent();

    /**
     * 获取对象
     * @return 对象
     */
    Object getObject();
}
