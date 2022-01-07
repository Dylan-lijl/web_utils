package pub.carzy.export_file.file_export.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Data
public class ExportRequestParam implements Serializable {
    @ApiModelProperty("文件名称")
    private String filename;
    @ApiModelProperty("文件类型")
    private Integer fileType;
    @ApiModelProperty(value = "导出的标题", required = true)
    @NotNull(message = "缺少导出标题字段内容")
    @Valid
    private List<ExportTitle> titles;
    @ApiModelProperty("标题样式")
    private Map<String,Object> titleStyle;
    @ApiModelProperty("内容样式")
    private Map<String,Object> valueStyle;

    public void setTitles(List<ExportTitle> titles) {
        if (titles != null && titles.size() > 1) {
            titles.sort(Comparator.comparingInt(ExportTitle::getSort));
        }
        this.titles = titles;
    }
}
