package pub.carzy.export_file;

import org.springframework.context.annotation.Import;
import pub.carzy.export_file.spring_bean.AopExportFileConfig;

import java.lang.annotation.*;

/**
 * @author admin
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AopExportFileConfig.class)
public @interface WebExportFileEnable {
}
