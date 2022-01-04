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
     * 是否匹配
     *
     * @param param
     * @param data
     * @return
     */
    boolean match(ExportRequestParam param, Object data);

    /**
     * 导出
     *
     * @param param 导出文件信息
     * @param data  数据
     * @param point 连接点
     * @return 文件
     */
    String export(ExportRequestParam param, Object data, ProceedingJoinPoint point);

    /**
     * 设置环境参数
     * @param envs 环境参数
     */
    void setEnvs(Map<String, Object> envs);

    /**
     * 获取环境参数
     * @return 环境参数
     */
    Map<String, Object> getEnvs();
}
