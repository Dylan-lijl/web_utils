package pub.carzy.export_file.file_export.suppers;

import org.aspectj.lang.ProceedingJoinPoint;
import pub.carzy.export_file.exce.SystemErrorException;
import pub.carzy.export_file.file_export.ExportFiler;
import pub.carzy.export_file.file_export.actuator.ExportActuator;
import pub.carzy.export_file.file_export.actuator.ExportActuatorParam;
import pub.carzy.export_file.file_export.entity.ExportRequestParam;
import pub.carzy.export_file.file_export.entity.ExportTitle;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象处理
 *
 * @author admin
 */
public abstract class AbstractExportFiler implements ExportFiler {
    /**
     * 环境
     */
    private final Map<String,Object> envs = new HashMap<>();

    @Override
    public void setEnvs(Map<String, Object> envs) {
        synchronized (this){
            this.envs.putAll(envs);
        }
    }

    @Override
    public Map<String, Object> getEnvs() {
        Map<String,Object> envs = new HashMap<>(this.envs.size());
        envs.putAll(this.envs);
        return envs;
    }

    @Override
    public boolean match(ExportRequestParam param, Object data) {
        return true;
    }

    @Override
    public String export(ExportRequestParam param, Object data, ProceedingJoinPoint point) {
        ExportActuatorParam actuatorParam = new ExportActuatorParam();
        actuatorParam.setCommonFilePath((String) envs.get("common-file-path"));
        actuatorParam.setPrefix((String) envs.get("prefix"));
        //定义几个模板方法
        ExportActuator actuator = buildExportActuator(param, data, point,actuatorParam);
        // 2.获取标题,进行排序
        List<ExportTitle> titles = actuator.getTitles();
        if (titles != null && titles.size() > 0) {
            //排序,正序排序
            titles.sort(Comparator.comparingInt(ExportTitle::getSort));
        }
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
            return actuator.getFilepath();
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
     * @param param
     * @param data
     * @param point
     * @param actuatorParam
     * @return
     */
    protected abstract ExportActuator buildExportActuator(ExportRequestParam param, Object data, ProceedingJoinPoint point, ExportActuatorParam actuatorParam);

    @Override
    public int getOrder() {
        return 0;
    }
}
