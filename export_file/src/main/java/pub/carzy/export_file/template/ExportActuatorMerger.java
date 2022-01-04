package pub.carzy.export_file.template;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import pub.carzy.export_file.file_export.actuator.ExportActuator;

import java.util.*;

/**
 * @author admin
 */
public class ExportActuatorMerger implements ApplicationRunner, ApplicationContextAware {
    /**
     * 执行器
     */
    private final List<ExportActuator> actuators = new ArrayList<>();

    private ApplicationContext applicationContext;

    /**
     * 子类实现
     */
    protected void loadByCustomer() {
        //
    }

    private void loadBySpring() {
        Map<String, ExportActuator> map = applicationContext.getBeansOfType(ExportActuator.class);
        this.actuators.addAll(map.values());
    }

    /**
     * 加载SPI
     */
    private void loadBySPI() {
        synchronized (this) {
            ServiceLoader<ExportActuator> loader = ServiceLoader.load(ExportActuator.class);
            //添加
            for (ExportActuator exportActuator : loader) {
                actuators.add(exportActuator);
            }
        }
    }

    /**
     * 容器启动完成后进行注入类型处理器
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadByCustomer();
        loadBySpring();
        loadBySPI();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
