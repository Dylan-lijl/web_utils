package pub.carzy.export_file.file_export;

import lombok.Data;

/**
 * 字段元数据以及回调
 * @author admin
 */
@Data
public class MetaInfoField {
    private String name;
    private MetaInfoGetValueCallback callback;

    public interface MetaInfoGetValueCallback {
        /**
         * 获取方法
         * @param target 目标对象
         * @return 转换后的值
         */
        Object getValue(Object target);
    }
}
