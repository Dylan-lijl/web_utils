package pub.carzy.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageRequest extends BasicRequest {
    private Integer pageNum = 1;
    private Integer pageSize = Integer.MAX_VALUE;

    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 使用pageNum
     * @return
     */
    @Deprecated
    public Integer getPageIndex() {
        return (pageNum - 1) * getPageSize();
    }
}
