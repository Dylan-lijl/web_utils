package pub.carzy.export_file.file_export.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 导出值转换器
 *
 * @author admin
 * @version 1.0
 */
@Data
public class ExportValueFormat implements Serializable {
    @ApiModelProperty(value = "转换类型: 1:映射类型,根据指定的值替换成指定的字符串,2:date类型,转成yyyy-MM-dd HH:dd:ss时间格式",required = true)
    @NotNull(message = "缺少转换类型")
    private Integer type;
    @ApiModelProperty("映射表内容,如果没有值就按原值输出")
    private Map<String, String> valueMap;
    @ApiModelProperty("单值")
    private String value;
    @ApiModelProperty("多值")
    private List<String> values;
}
