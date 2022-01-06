package pub.carzy.services.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("help_keyword")
@ApiModel(value="", description="")
public class HelpKeyword implements Serializable {
    private static final long serialVersionUID=1L;
    @TableId(value = "help_keyword_id", type = IdType.NONE)
    private Integer helpKeywordId;
    @ApiModelProperty(value = "名称")
    private String name;
}
