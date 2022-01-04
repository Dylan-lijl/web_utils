package pub.carzy.export_file.template;

import pub.carzy.export_file.file_export.entity.ExportRequestParam;

/**
 * @author admin
 */
public class DefaultExportAopCallback implements ExportAopCallback {

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
     * @param args 方法参数
     */
    @Override
    public void updatePageSize(Object[] args) {
        //nothing
    }

    @Override
    public Object responseResult(Object obj) {
        return null;
    }

}
