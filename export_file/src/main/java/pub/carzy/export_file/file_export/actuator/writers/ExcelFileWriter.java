package pub.carzy.export_file.file_export.actuator.writers;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.*;
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
 * @version 1.0
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
    public <T> void writeLine(List<T> line, Map<String, Object> configs) {
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
            //设置样式
            if (configs != null) {
                //公共的样式,尽量自行扩展重写
                setCellStyle(cell, configs);
            }
        }
    }

    private void setCellStyle(XSSFCell cell, Map<String, Object> configs) {
        XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
        Font font = xssfWorkbook.createFont();
        for (Map.Entry<String, Object> config : configs.entrySet()) {
            //这里最好使用spi来加载处理
            if ("对齐方式".equals(config.getKey())) {
                if (config.getValue() instanceof String) {
                    if ("居中".equals(config.getValue())) {
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    } else if ("居右".equals(config.getValue())) {
                        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                    }
                }
            } else if ("字体".equals(config.getKey())){
                if (config.getValue() instanceof String){
                    font.setFontName((String) config.getValue());
                }
            } else if ("字体大小".equals(config.getKey())){
                if (config.getValue() instanceof Number){
                    font.setFontHeightInPoints(((Number) config.getValue()).shortValue());
                }
            }else if ("粗体".equals(config.getKey())){
                font.setBold(true);
            }else if ("斜体".equals(config.getKey())){
                font.setItalic(true);
            }else if ("删除线".equals(config.getKey())){
                font.setStrikeout(true);
            }else if ("下划线".equals(config.getKey())){
                if (config.getValue() instanceof Number) {
                    font.setUnderline(((Number) config.getValue()).byteValue());
                }
            }
        }
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
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
