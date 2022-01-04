package pub.carzy.export_file.file_export.actuator.file_impl.txt;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.MetaInfo;
import pub.carzy.export_file.file_export.MetaInfoField;
import pub.carzy.export_file.file_export.actuator.AbstractExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.entity.ExportTitle;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

/**
 * csv集合类型
 *
 * @author admin
 */
public class TxtCollectionExportActuator extends AbstractExportActuator {

    private PrintWriter writer;

    public TxtCollectionExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        super(param, data, point, actuatorParam);
    }

    @Override
    protected void doClose() {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    @Override
    public void createFile() {
        file = new File(actuatorParam.getCommonFilePath(), actuatorParam.getPrefix() + (param.getFilename().endsWith(".txt") ? param.getFilename() : param.getFilename() + ".txt"));
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            writer = new PrintWriter(file);
        } catch (IOException e) {
            throw new SystemErrorException();
        }

    }

    @Override
    public void writeTitles(List<ExportTitle> titles) {
        this.titles = titles;
        //创建标题栏
        for (int i = 0; i < titles.size(); i++) {
            ExportTitle title = titles.get(i);
            writer.print(title.getTitle());
            if (i<titles.size()-1){
                writer.print('\t');
            }
        }
        writer.print('\n');
    }

    @Override
    public void writeContent() {
        Collection collection = (Collection) this.data;
        MetaInfo info = getMetaInfo(collection.stream().findFirst().get().getClass());
        for (Object object : collection) {
            for (int i = 0; i < titles.size(); i++) {
                ExportTitle title = titles.get(i);
                for (MetaInfoField field : info.getFields()) {
                    //写数据
                    if (field.getName().equals(title.getName())) {
                        Object value = field.getCallback().getValue(object);
                        if (value == null) {
                            writer.write("");
                        } else {
                            writer.write(transformValue(title, value).toString());
                        }
                        if (i<titles.size()-1){
                            writer.print('\t');
                        }
                        break;
                    }
                }
            }
            writer.print('\n');
        }
    }
}
