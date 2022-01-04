package pub.carzy.export_file.spring_bean.default_export;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import pub.carzy.export_file.file_export.ExportFileStatic;
import pub.carzy.export_file.file_export.actuator.ExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.file_impl.excel.ExcelCollectionExportActuator;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.suppers.CollectionExportFiler;

/**
 * excel
 *
 * @author admin
 */
public class ExcelCollectionExportFiler extends CollectionExportFiler {

    @Override
    public int getOrder() {
        return super.getOrder() + ExportFileStatic.EXCEL;
    }

    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return param.getFileType().compareTo(ExportFileStatic.EXCEL) == 0 && super.match(param, data);
    }

    @Override
    protected ExportActuator buildExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        return new ExcelCollectionExportActuator(param,data,point,actuatorParam);
    }
}
