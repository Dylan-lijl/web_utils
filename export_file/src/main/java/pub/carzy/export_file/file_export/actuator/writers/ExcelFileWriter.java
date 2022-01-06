package pub.carzy.export_file.file_export.actuator.writers;

import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
public class ExcelFileWriter extends AbstractFileWriter {
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet xssfSheet;
    private int rowIndex;

    public ExcelFileWriter(ExportActuatorParam param) {
        super(param);
    }

    @Override
    protected String getFilename() {
        return param.getPrefix() + (param.getParam().getFilename().endsWith(".xlsx") ?
                param.getParam().getFilename() : param.getParam().getFilename() + ".xlsx");
    }

    @Override
    public <T> void writeLine(List<T> line, Map<String,Object> configs) {
        XSSFRow row = xssfSheet.createRow(rowIndex++);
        for (int i = 0; i < line.size(); i++) {
            Object value = line.get(i);
            XSSFCell cell = row.createCell(i);
            if (value == null) {
                cell.setCellValue("");
                continue;
            }
            if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
            } else if (value instanceof Calendar) {
                cell.setCellValue((Calendar) value);
            } else if (value instanceof RichTextString) {
                cell.setCellValue((RichTextString) value);
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    @Override
    public void flush() throws IOException {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            xssfWorkbook.write(stream);
            stream.flush();
        } catch (IOException e) {
            throw new SystemErrorException();
        }
    }

    @Override
    protected void createdFile() throws IOException {
        super.createdFile();
        //创建工作簿
        xssfWorkbook = new XSSFWorkbook();
        //创建单元表
        xssfSheet = xssfWorkbook.createSheet();
    }

    @Override
    public void close() throws IOException {
        if (xssfWorkbook != null) {
            flush();
            try {
                xssfWorkbook.close();
            } catch (IOException e) {
                throw new SystemErrorException();
            }
        }
    }
}
