package pub.carzy.export_file.file_export;

import lombok.Data;

/**
 * @author admin
 */
@Data
public class MetaInfoField {
    private String name;
    private MetaInfoGetValueCallback callback;

    public interface MetaInfoGetValueCallback {
        /**
         * 获取方法
         *
         * @return
         */
        Object getValue(Object target);
    }
}
