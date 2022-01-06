package pub.carzy.services.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.util.BasicRequest;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExportRequest extends BasicRequest {
    @ApiModelProperty(value = "路径",required = true)
    @NotNull(message = "缺少路径")
    private String uri;
    @ApiModelProperty(value = "方法",required = true)
    @NotNull(message = "缺少方法")
    private String method;
    @ApiModelProperty(value = "参数",required = true)
    @NotNull(message = "缺少参数")
    private Map<String,Object> params;
}
