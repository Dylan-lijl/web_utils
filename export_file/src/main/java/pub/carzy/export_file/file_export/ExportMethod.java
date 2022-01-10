package pub.carzy.export_file.file_export;


import pub.carzy.export_file.util.FileType;

import java.lang.annotation.*;

/**
 * 导出方法
 * @author admin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportMethod {
    /**
     * 默认文件名称
     * @return 默认导出的文件名称
     */
    String filename() default "download";

    /**
     * 默认文件类型 txt
     * @return 默认导出的文件类型
     */
    int fileType() default FileType.TXT;
}
