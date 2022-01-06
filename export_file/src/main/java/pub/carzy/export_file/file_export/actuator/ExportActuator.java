package pub.carzy.export_file.file_export.actuator;

import pub.carzy.export_file.file_export.entity.ExportTitle;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * 执行器
 * @author admin
 */
public interface ExportActuator extends Closeable {
    /**
     * 获取文件
     * @return
     */
    List<ExportTitle> getTitles();

    /**
     * 创建文件
     */
    void createFile();

    /**
     * 写标题
     * @param titles
     */
    void writeTitles(List<ExportTitle> titles);

    /**
     * 写内容方法
     */
    void writeContent();

    /**
     * 对象
     */
    Object getObject();
}
