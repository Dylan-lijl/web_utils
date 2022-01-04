package pub.carzy.export_file.file_export.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 映射对象
 * @author admin
 */
@Data
public class ExportTitle implements Serializable {
    @ApiModelProperty(value = "字段名称",required = true)
    @NotNull(message = "缺少字段名称")
    private String name;
    @ApiModelProperty(value = "标题",required = true)
    @NotNull(message = "缺少标题")
    private String title;
    @ApiModelProperty("排序")
    private Integer sort=Integer.MAX_VALUE;
    @ApiModelProperty("转换器,如果没有就直接注入值")
    @Valid
    private ExportValueFormat convertor;
}
