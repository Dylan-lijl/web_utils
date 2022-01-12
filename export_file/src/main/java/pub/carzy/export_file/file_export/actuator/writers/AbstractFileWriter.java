package pub.carzy.export_file.file_export.actuator.writers;

import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @version 1.0
 */
public abstract class AbstractFileWriter implements FileWriter {
    /**
     * 文件绝对路径
     */
    protected ExportActuatorParam param;

    protected File file;

    public ExportActuatorParam getParam() {
        return param;
    }

    public void setParam(ExportActuatorParam param) {
        this.param = param;
    }

    public AbstractFileWriter(ExportActuatorParam param) {
        this.param = param;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public <T> void writeMany(List<List<T>> many, Map<String, Object> configs) {
        for (List<T> list : many) {
            writeLine(list, configs);
        }
    }

    @Override
    public void createFile() {
        file = new File(param.getCommonFilePath(), getFilename());
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            createdFile();
        } catch (IOException e) {
            throw new SystemErrorException();
        }
    }

    /**
     * 创建文件完成
     */
    protected void createdFile() throws IOException {
    }

    /**
     * 获取文件名称
     *
     * @return 文件名称
     */
    protected abstract String getFilename();
}
