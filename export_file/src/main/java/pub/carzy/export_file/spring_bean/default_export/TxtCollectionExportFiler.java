package pub.carzy.export_file.spring_bean.default_export;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.file_export.ExportFileStatic;
import pub.carzy.export_file.file_export.actuator.ExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.file_impl.txt.TxtCollectionExportActuator;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.suppers.CollectionExportFiler;

/**
 * excel
 *
 * @author admin
 */
public class TxtCollectionExportFiler extends CollectionExportFiler {

    @Override
    public int getOrder() {
        return super.getOrder() + ExportFileStatic.TXT;
    }

    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return param.getFileType().compareTo(ExportFileStatic.TXT) == 0 && super.match(param, data);
    }

    @Override
    protected ExportActuator buildExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        return new TxtCollectionExportActuator(param,data,point,actuatorParam);
    }
}
