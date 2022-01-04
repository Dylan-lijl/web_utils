package pub.carzy.export_file.file_export.actuator.file_impl.excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.MetaInfo;
import pub.carzy.export_file.file_export.MetaInfoField;
import pub.carzy.export_file.file_export.actuator.AbstractExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.entity.ExportTitle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * csv集合类型
 *
 * @author admin
 */
public class ExcelCollectionExportActuator extends AbstractExportActuator {

    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet xssfSheet;
    private int rowIndex;

    public ExcelCollectionExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        super(param, data, point, actuatorParam);
    }

    @Override
    protected void doClose() {
        if (xssfWorkbook != null) {
            try {
                xssfWorkbook.close();
            } catch (IOException e) {
                throw new SystemErrorException();
            }
        }
    }

    @Override
    public void createFile() {
        file = new File(actuatorParam.getCommonFilePath(), actuatorParam.getPrefix() + (param.getFilename().endsWith(".xlsx") ? param.getFilename() : param.getFilename() + ".xlsx"));
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            //创建工作簿
            xssfWorkbook = new XSSFWorkbook();
            //创建单元表
            xssfSheet = xssfWorkbook.createSheet();
        } catch (IOException e) {
            throw new SystemErrorException();
        }

    }

    @Override
    public void writeTitles(List<ExportTitle> titles) {
        this.titles = titles;
        //创建标题栏
        XSSFRow row = xssfSheet.createRow(rowIndex++);
        for (int i = 0, titlesSize = titles.size(); i < titlesSize; i++) {
            ExportTitle title = titles.get(i);
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(title.getTitle());
        }
    }

    @Override
    public void writeContent() {
        Collection collection = (Collection) this.data;
        MetaInfo info = getMetaInfo(collection.stream().findFirst().get().getClass());
        for (Object object : collection) {
            XSSFRow row = xssfSheet.createRow(rowIndex++);
            for (int i = 0; i < titles.size(); i++) {
                ExportTitle title = titles.get(i);
                XSSFCell cell = row.createCell(i);
                for (MetaInfoField field : info.getFields()) {
                    //写数据
                    if (field.getName().equals(title.getName())) {
                        Object value = field.getCallback().getValue(object);
                        if (value == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(transformValue(title,value).toString());
                        }
                        break;
                    }
                }
            }
        }
        //写到文件里面去
        try (FileOutputStream stream = new FileOutputStream(file)) {
            xssfWorkbook.write(stream);
            stream.flush();
        } catch (IOException e) {
            throw new SystemErrorException();
        }
    }
}
