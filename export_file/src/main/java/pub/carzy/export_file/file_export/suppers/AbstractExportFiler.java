package pub.carzy.export_file.file_export.suppers;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.ExportFiler;
import pub.carzy.export_file.file_export.actuator.ExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorConfig;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.entity.ExportTitle;
import pub.carzy.export_file.spring_bean.WebEnvConfig;
import pub.carzy.export_file.template.ExportConvertorMerger;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 抽象处理
 *
 * @author admin
 */
public abstract class AbstractExportFiler implements ExportFiler {

    /**
     * 环境变量,由于扩展性问题,使用map进行接收
     */
    @Resource
    private WebEnvConfig webEnvConfig;

    @Resource
    private ExportConvertorMerger merger;

    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return true;
    }

    @Override
    public Object export(ExportRequestParam param, Object data, ProceedingJoinPoint point) {
        ExportActuatorParam actuatorParam = new ExportActuatorParam();
        actuatorParam.setCommonFilePath(webEnvConfig.getExport().get("common-file-path") == null ? "" : webEnvConfig.getExport().get("common-file-path"));
        actuatorParam.setPrefix(webEnvConfig.getExport().get("prefix") == null ? "" : webEnvConfig.getExport().get("prefix"));
        actuatorParam.setParam(param);
        //新对象来规避并发问题
        ExportActuator actuator = createExportActuator(data, point, actuatorParam);
        // 2.获取标题,进行排序
        List<ExportTitle> titles = actuator.getTitles();
        // 3.创建文件
        actuator.createFile();
        try {
            // 4.写标题
            actuator.writeTitles(titles);
            // 5.写内容
            if (data != null) {
                actuator.writeContent();
            }
            // 6.获取文件路径
            return actuator.getObject();
        } finally {
            //调用close方法
            try {
                actuator.close();
            } catch (IOException e) {
                throw new SystemErrorException();
            }
        }
    }

    /**
     * 获取导出执行器
     *
     * @param data 数据
     * @param point 方法点
     * @param actuatorParam 执行封装对象
     * @return 执行器
     */
    protected abstract ExportActuator createExportActuator(Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam);

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 获取配置信息
     *
     * @return 配置信息
     */
    protected ExportActuatorConfig getActuatorConfig() {
        ExportActuatorConfig config = new ExportActuatorConfig();
        config.setFactories(merger.getFactories());
        config.setConvertors(merger.getConvertors());
        return config;
    }
}
