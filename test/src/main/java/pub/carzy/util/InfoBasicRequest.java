package pub.carzy.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 详情基础请求类
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InfoBasicRequest extends BasicRequest {
    @ApiModelProperty(value = "主键",required = true)
    @NotNull(message = "缺少id")
    private Long id;
}
