package pub.carzy.export_file.spring_bean.default_export;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import pub.carzy.export_file.file_export.ExportFileStatic;
import pub.carzy.export_file.file_export.actuator.ExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.file_impl.csv.CsvCollectionExportActuator;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.suppers.CollectionExportFiler;

/**
 * csv
 *
 * @author admin
 */
public class CsvCollectionExportFiler extends CollectionExportFiler {

    @Override
    public int getOrder() {
        return super.getOrder() - ExportFileStatic.CSV;
    }

    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return param.getFileType().compareTo(ExportFileStatic.CSV) == 0 && super.match(param, data);
    }

    @Override
    protected ExportActuator buildExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        return new CsvCollectionExportActuator(param,data,point,actuatorParam);
    }
}
