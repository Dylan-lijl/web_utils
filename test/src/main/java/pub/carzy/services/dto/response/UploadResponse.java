package pub.carzy.services.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author admin
 */
@Data
public class UploadResponse{
    @ApiModelProperty("文件路径")
    private String path;
}
