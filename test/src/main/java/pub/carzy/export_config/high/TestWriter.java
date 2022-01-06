package pub.carzy.export_config.high;

import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.writers.TxtFileWriter;

/**
 * 自定义继承AbstractFileWriter,这里继承是为了测试而偷懒
 * 再定义一个工厂类来生成这个类
 * @author admin
 */
public class TestWriter extends TxtFileWriter {
    public TestWriter(ExportActuatorParam param) {
        super(param);
    }

    @Override
    protected String getFilename() {
        return "test-"+super.getFilename();
    }

}
