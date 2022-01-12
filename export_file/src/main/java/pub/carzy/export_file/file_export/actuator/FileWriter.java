package pub.carzy.export_file.file_export.actuator;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文件操作
 *
 * @author admin
 * @version 1.0
 */
public interface FileWriter extends Closeable {
    /**
     * 写一行
     *
     * @param line    行数据
     * @param configs 一些配置或样式等等(File
     */
    <T> void writeLine(List<T> line, Map<String,Object> configs);

    /**
     * 写出多个
     *
     * @param many    多条
     * @param configs 样式或其他数据
     */
    <T> void writeMany(List<List<T>> many, Map<String,Object> configs);

    /**
     * 获取文件
     * @return 文件
     */
    File getFile();

    /**
     * 创建文件
     */
    void createFile();

    /**
     * 刷新缓存区到文件中
     * @throws IOException io异常
     */
    void flush() throws IOException;
}
