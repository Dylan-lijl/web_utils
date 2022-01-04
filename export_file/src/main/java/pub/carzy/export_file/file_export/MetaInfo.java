package pub.carzy.export_file.file_export;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 */
@Data
public class MetaInfo {
    private List<MetaInfoField> fields;
}
