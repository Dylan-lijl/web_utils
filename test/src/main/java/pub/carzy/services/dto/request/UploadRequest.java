package pub.carzy.services.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 */
@Data
public class UploadRequest {
    @ApiModelProperty(value = "文件",required = true)
    @NotNull(message = "缺少文件")
    private MultipartFile file;
}
