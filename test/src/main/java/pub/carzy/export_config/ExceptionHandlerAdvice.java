package pub.carzy.export_config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pub.carzy.api.CommonResult;
import pub.carzy.api.ResultCodeEnum;
import pub.carzy.export_file.exce.ExportFileEmptyException;
import pub.carzy.export_file.exce.ExportNotSupportedException;
import pub.carzy.export_file.exce.SystemErrorException;

/**
 * @author admin
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({ExportFileEmptyException.class, SystemErrorException.class})
    @ResponseBody
    private CommonResult<Object> exportException() {
        return new CommonResult<>(ResultCodeEnum.SYSTEM_EXCEPTION.getCode(), ResultCodeEnum.SYSTEM_EXCEPTION.getMessage(), null);
    }

    @ExceptionHandler(ExportNotSupportedException.class)
    @ResponseBody
    private CommonResult<Object> exportNotSupportedException() {
        return new CommonResult<>(ResultCodeEnum.FILE_NOT_SUPPORTED.getCode(), ResultCodeEnum.FILE_NOT_SUPPORTED.getMessage(), null);
    }
}
