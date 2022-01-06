package pub.carzy.export_config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import pub.carzy.api.CommonPage;
import pub.carzy.api.CommonResult;
import pub.carzy.api.ResultCodeEnum;
import pub.carzy.export_file.file_export.actuator.AbstractExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.suppers.AbstractExportFiler;
import pub.carzy.util.ExceptionHandler;
import pub.carzy.util.ExportType;
import pub.carzy.util.PageRequest;

import java.util.*;

/**
 * @author admin
 */
@Component
public class CommonPageExport extends AbstractExportFiler {
    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return super.match(param, data)
                && data instanceof CommonResult
                && ((CommonResult<?>) data).getData() != null
                && ((CommonResult<?>) data).getData() instanceof CommonPage;
    }

    /**
     * 保证顺序在CommonExport前面
     * @return 排序
     */
    @Override
    public int getOrder() {
        return super.getOrder() - ExportType.COMMON - ExportType.COMMON_TO_PAGE;
    }

    @Override
    protected ExportActuator createExportActuator(Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam) {
        return new AbstractExportActuator(data, point, actuatorParam, getActuatorConfig()) {

            @Override
            protected List<List<Object>> transformContent() {
                List<List<Object>> list = new ArrayList<>();
                CommonResult<?> result = (CommonResult<?>) data;
                Object data = result.getData();
                if (data == null) {
                    return null;
                }
                CommonPage<?> page = (CommonPage<?>) data;
                List<?> objects = page.getList();
                //获取目标方法的参数
                Object[] args = point.getArgs();
                //如果有数据则递归查询数据
                while (objects != null && objects.size() > 0) {
                    for (Object obj : objects) {
                        List<Object> line = new ArrayList<>();
                        parseAndPutObjectValue(line, obj);
                        if (line.size() > 0) {
                            list.add(line);
                        }
                    }
                    //修改方法参数中分页对象的num
                    for (Object arg : args) {
                        if (arg instanceof PageRequest) {
                            PageRequest request = (PageRequest) arg;
                            request.setPageNum(request.getPageNum() + 1);
                        }
                    }
                    try {
                        //查询数据
                        CommonResult<?> proceed = (CommonResult<?>) point.proceed(args);
                        CommonPage<?> commonPage = (CommonPage<?>) proceed.getData();
                        objects = commonPage.getList();
                    } catch (Throwable e) {
                        ExceptionHandler.throwException(ResultCodeEnum.SYSTEM_EXCEPTION);
                    }
                }
                return list.size() > 0 ? list : null;
            }
        };
    }
}
