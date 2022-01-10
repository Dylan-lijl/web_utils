package pub.carzy.export_file.file_export;


import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

import java.util.Map;

/**
 * 文件处理器
 *
 * @author admin
 */
public interface ExportFiler extends Ordered {
    /**
     * 匹配是否能够处理
     *
     * @param param 参数
     * @param data 方法执行结果
     * @return 是否能够处理
     */
    boolean match(ExportRequestParam param, Object data);

    /**
     * 执行导出
     *
     * @param param 导出文件信息
     * @param data  数据
     * @param point 连接点
     * @return 文件
     */
    Object export(ExportRequestParam param, Object data, ProceedingJoinPoint point);
}
