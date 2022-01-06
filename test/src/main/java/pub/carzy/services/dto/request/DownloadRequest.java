package pub.carzy.services.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pub.carzy.util.BasicRequest;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DownloadRequest extends BasicRequest {
    @ApiModelProperty(value = "路径",required = true)
    @NotNull(message = "缺少路径")
    private String path;
}
