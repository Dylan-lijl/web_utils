package pub.carzy.services.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author admin
 */
@Data
public class TestResponse {
    private Integer helpKeywordId;
    @ApiModelProperty(value = "名称")
    private String name;
}
