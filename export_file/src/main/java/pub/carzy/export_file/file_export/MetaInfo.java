package pub.carzy.export_file.file_export;

import lombok.Data;

import java.util.List;

/**
 * class的字段信息
 * @author admin
 * @version 1.0
 */
@Data
public class MetaInfo {
    private List<MetaInfoField> fields;
}
