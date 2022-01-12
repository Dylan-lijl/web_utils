package pub.carzy.export_file.spring_bean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import pub.carzy.export_file.file_export.ExportFileStatic;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.actuator.FileWriter;
import pub.carzy.export_file.file_export.actuator.writers.CsvFileWriter;
import pub.carzy.export_file.file_export.actuator.writers.ExcelFileWriter;
import pub.carzy.export_file.file_export.actuator.writers.TxtFileWriter;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.template.DefaultExportAopCallback;
import pub.carzy.export_file.template.ExportConvertorMerger;
import pub.carzy.export_file.template.ExportAopCallback;
import pub.carzy.export_file.template.FileWriteFactory;

/**
 * @author admin
 * @version 1.0
 */
@Import(AopExpressionBeanPostProcess.class)
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
    @ConditionalOnMissingBean(ExportConvertorMerger.class)
    ExportConvertorMerger exportActuatorMerger() {
        return new ExportConvertorMerger();
    }

    @Bean
    FileWriteFactory csvFileWriteFactory() {
        return new FileWriteFactory() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public boolean match(Integer type) {
                return type == ExportFileStatic.CSV;
            }

            @Override
            public FileWriter createWriter(ExportActuatorParam param) {
                return new CsvFileWriter(param);
            }
        };
    }

    @Bean
    FileWriteFactory txtFileWriteFactory() {
        return new FileWriteFactory() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public boolean match(Integer type) {
                return type == ExportFileStatic.TXT;
            }

            @Override
            public FileWriter createWriter(ExportActuatorParam param) {
                return new TxtFileWriter(param);
            }
        };
    }

    @Bean
    FileWriteFactory excelFileWriteFactory() {
        return new FileWriteFactory() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public boolean match(Integer type) {
                return type == ExportFileStatic.EXCEL;
            }

            @Override
            public FileWriter createWriter(ExportActuatorParam param) {
                return new ExcelFileWriter(param);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ExportFileValueConvertor.class)
    ExportFileValueConvertor defaultExportFileValueConvertor() {
        return new ExportFileValueConvertor(){

            @Override
            public boolean match(ExportValueFormat convertor) {
                return false;
            }

            @Override
            public Object formatValue(ExportValueFormat convertor, Object value) {
                return null;
            }
        };
    }
}
