package pub.carzy.export_file.template;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.file_export.ExportFiler;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 */
public class DefaultExportAopCallback implements ExportAopCallback {

    @Resource
    private List<ExportFiler> filers;

    @Override
    public ExportRequestParam getExportParam(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof ExportRequestParam) {
                return (ExportRequestParam) arg;
            }
        }
        return null;
    }

    /**
     * 子类实现
     *
     * @param args 方法参数
     */
    @Override
    public void updatePageSize(Object[] args) {
        //nothing
    }

    @Override
    public Object responseResult(ExportRequestParam exportRequestParam, ProceedingJoinPoint point, Object obj) {
        return obj;
    }

}
