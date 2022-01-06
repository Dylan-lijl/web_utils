package pub.carzy.export_file.file_export.actuator.writers;

import com.opencsv.CSVWriter;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author admin
 */
public class CsvFileWriter extends AbstractFileWriter {

    private CSVWriter csvWriter;

    public CsvFileWriter(ExportActuatorParam param) {
        super(param);
    }

    @Override
    public <T> void writeLine(List<T> line, Map<String,Object> configs) {
        //转成string
        if (line.size() > 0) {
            List<String> list = line.stream().map(Object::toString).collect(Collectors.toList());
            csvWriter.writeNext(list.toArray(new String[]{}));
        }
    }

    @Override
    public void flush() throws IOException {
        csvWriter.flush();
    }

    @Override
    protected void createdFile() throws IOException {
        super.createdFile();
        csvWriter = new CSVWriter(new FileWriter(file));
    }

    @Override
    protected String getFilename() {
        return param.getPrefix() + (param.getParam().getFilename().endsWith(".csv") ?
                param.getParam().getFilename() : param.getParam().getFilename() + ".csv");
    }

    @Override
    public void close() throws IOException {
        if (csvWriter != null) {
            csvWriter.flush();
            csvWriter.close();
        }
    }
}
