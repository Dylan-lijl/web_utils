package pub.carzy.export_file.template;

import org.springframework.beans.factory.InitializingBean;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;

import javax.annotation.Resource;
import java.util.*;

/**
 * 合并,将spring的和SPI的进行整合
 * @author admin
 * @version 1.0
 */
public class ExportConvertorMerger implements InitializingBean {
    /**
     * 文件创建工厂
     */
    @Resource
    protected List<FileWriteFactory> factories;

    @Resource
    protected List<ExportFileValueConvertor> convertors;

    public List<ExportFileValueConvertor> getConvertors() {
        return new ArrayList<>(convertors);
    }

    public List<FileWriteFactory> getFactories() {
        return new ArrayList<>(factories);
    }

    /**
     * 子类实现
     */
    protected void loadByCustomer() {
        //
    }

    /**
     * 加载SPI
     */
    protected void loadBySPI() {
        ClassLoader classLoader = FileWriteFactory.class.getClassLoader();
        synchronized (this) {
            ServiceLoader<FileWriteFactory> loader = ServiceLoader.load(FileWriteFactory.class,classLoader);
            //添加
            for (FileWriteFactory factory : loader) {
                this.factories.add(factory);
            }
            ServiceLoader<ExportFileValueConvertor> convertorServiceLoader = ServiceLoader.load(ExportFileValueConvertor.class,classLoader);
            for (ExportFileValueConvertor convertor : convertorServiceLoader) {
                this.convertors.add(convertor);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadByCustomer();
        loadBySPI();
    }
}
