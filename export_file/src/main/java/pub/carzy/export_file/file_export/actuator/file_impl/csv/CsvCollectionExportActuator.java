package pub.carzy.export_file.file_export.actuator.file_impl.csv;

import cn.hutool.core.text.csv.CsvWriter;
import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.MetaInfo;
import pub.carzy.export_file.file_export.MetaInfoField;
import pub.carzy.export_file.file_export.actuator.AbstractExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.entity.ExportTitle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * csv集合类型
 *
 * @author admin
 */
public class CsvCollectionExportActuator extends AbstractExportActuator {

    private CsvWriter csvWriter;

    public CsvCollectionExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        super(param, data, point, actuatorParam);
    }

    @Override
    protected void doClose() {
        if (csvWriter != null) {
            csvWriter.flush();
            csvWriter.close();
        }
    }

    @Override
    public void createFile() {
        file = new File(actuatorParam.getCommonFilePath(), actuatorParam.getPrefix() + (param.getFilename().endsWith(".csv") ? param.getFilename() : param.getFilename() + ".csv"));
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            csvWriter = new CsvWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new SystemErrorException();
        }

    }

    @Override
    public void writeTitles(List<ExportTitle> titles) {
        this.titles = titles;
        String[] t = new String[titles.size()];
        for (int i = 0, titlesSize = titles.size(); i < titlesSize; i++) {
            ExportTitle title = titles.get(i);
            t[i] = title.getTitle();
        }
        csvWriter.write(t);
    }

    @Override
    public void writeContent() {
        Collection collection = (Collection) this.data;
        MetaInfo info = getMetaInfo(collection.stream().findFirst().get().getClass());
        for (Object object: collection){
            String[] values = new String[titles.size()];
            for (int i = 0; i < titles.size(); i++) {
                ExportTitle title = titles.get(i);
                for (MetaInfoField field : info.getFields()) {
                    //写数据
                    if (field.getName().equals(title.getName())){
                        Object value = field.getCallback().getValue(object);
                        if (value == null){
                            values[i] = "";
                        }else{
                            values[i] = transformValue(title,value).toString();
                        }
                    }
                }
            }
            csvWriter.write(values);
        }
    }
}
