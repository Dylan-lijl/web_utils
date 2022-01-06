package pub.carzy.export_file.file_export.actuator.writers;

import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
public class TxtFileWriter extends AbstractFileWriter {
    private PrintWriter writer;

    public TxtFileWriter(ExportActuatorParam param) {
        super(param);
    }

    @Override
    protected String getFilename() {
        return param.getPrefix() + (param.getParam().getFilename().endsWith(".txt") ?
                param.getParam().getFilename() : param.getParam().getFilename() + ".txt");
    }

    @Override
    protected void createdFile() throws IOException {
        super.createdFile();
        writer = new PrintWriter(file);
    }

    @Override
    public <T> void writeLine(List<T> line, Map<String,Object> configs) {
        for (int i = 0; i < line.size(); i++) {
            Object value = line.get(i);
            writer.print(value.toString());
            if (i < line.size() - 1) {
                writer.print('\t');
            }
        }
        writer.print('\n');
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}
