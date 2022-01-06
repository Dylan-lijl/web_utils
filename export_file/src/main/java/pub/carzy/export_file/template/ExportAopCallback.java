package pub.carzy.export_file.template;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

/**
 * @author admin
 */
public interface ExportAopCallback {
    /**
     * 获取导出参数,如果是null则直接正常执行方法,而不会进行导出逻辑处理
     * @param args 方法参数
     * @return 导出参数
     */
    ExportRequestParam getExportParam(Object[] args);

    /**
     * 该接口可能是一个分页导出方法,修改分页的页条数来优化
     * @param args 方法参数
     */
    void updatePageSize(Object[] args);

    /**
     * 返回封装结果
     *
     * @param exportRequestParam
     * @param obj 导出返回内容
     * @return 前端接收的实体内容
     */
    Object responseResult(ExportRequestParam exportRequestParam, ProceedingJoinPoint point, Object obj);
}
