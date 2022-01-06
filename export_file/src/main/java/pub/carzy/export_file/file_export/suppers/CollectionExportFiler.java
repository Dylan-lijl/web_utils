package pub.carzy.export_file.file_export.suppers;

import pub.carzy.export_file.file_export.ExportFileStatic;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

import java.util.Collection;

/**
 * 集合文件导出,抽象类
 *
 * @author admin
 */
public abstract class CollectionExportFiler extends AbstractExportFiler {


    @Override
    public int getOrder() {
        return super.getOrder() - ExportFileStatic.COLLECTION_ORDER;
    }

    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return data instanceof Collection && super.match(param, data);
    }
}
