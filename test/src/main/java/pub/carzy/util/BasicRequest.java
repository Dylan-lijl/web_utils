package pub.carzy.util;

import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author admin
 */
@Data
public class BasicRequest implements Serializable {
    /**
     * 防止开启异步线程执行时问题
     */
    @Ignore
    private Object threadLocalObject;

    @ApiModelProperty("导出文件的参数")
    @Valid
    private ExportRequestParam _export;

    /**
     * 获取线程变量
     *
     * @return 线程变量
     */
    public Object getThreadLocalObject() {
        return this.getThreadLocalObject(false);
    }

    /**
     * 是否强制去拿
     * @param isForce 强制
     * @return
     */
    public Object getThreadLocalObject(boolean isForce) {
        if (!isForce&&threadLocalObject!=null){
            return threadLocalObject;
        }
        threadLocalObject = HttpRequestThreadLocal.getAttribute();
        return threadLocalObject;
    }
}
