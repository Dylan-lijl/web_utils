package pub.carzy.export_file.spring_bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pub.carzy.export_file.file_export.ExportFiler;
import pub.carzy.export_file.spring_bean.default_export.CsvCollectionExportFiler;
import pub.carzy.export_file.spring_bean.default_export.ExcelCollectionExportFiler;
import pub.carzy.export_file.spring_bean.default_export.TxtCollectionExportFiler;
import pub.carzy.export_file.template.DefaultExportAopCallback;
import pub.carzy.export_file.template.ExportActuatorMerger;
import pub.carzy.export_file.template.ExportAopCallback;

/**
 * @author admin
 */
public class ExportBeans {
    /**
     * 默认回调
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ExportAopCallback.class)
    ExportAopCallback callback() {
        return new DefaultExportAopCallback();
    }

    @Bean
    @ConditionalOnMissingBean(ExportActuatorMerger.class)
    ExportActuatorMerger exportActuatorMerger() {
        return new ExportActuatorMerger();
    }
    @Bean
    ExportFiler csvCollectionExportFiler(){
        return new CsvCollectionExportFiler();
    }
    @Bean
    ExportFiler excelCollectionExportFiler(){
        return new ExcelCollectionExportFiler();
    }
    @Bean
    ExportFiler txtCollectionExportFiler(){
        return new TxtCollectionExportFiler();
    }
}
