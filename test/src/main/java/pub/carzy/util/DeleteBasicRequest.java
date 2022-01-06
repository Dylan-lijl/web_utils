package pub.carzy.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 删除基础请求类
 * @author admin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteBasicRequest extends BasicRequest{
    @ApiModelProperty(value = "删除的id",required = true)
    @NotNull(message = "缺少要删除的id")
    @NotEmpty(message = "缺少要删除的id")
    private List<Long> ids;

    /**
     * 是否是单个id
     * @return 是否是单个id
     */
    public boolean isOnly(){
        return ids.size()==1;
    }

    /**
     * 获取id
     * @return 获取id
     */
    public Long getId(){
        return ids.get(0);
    }
}
