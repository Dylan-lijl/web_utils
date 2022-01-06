package pub.carzy.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateByIdRequest extends BasicRequest{
    @ApiModelProperty(value = "主键",required = true)
    @NotNull(message = "缺少主键")
    private Long id;
}
