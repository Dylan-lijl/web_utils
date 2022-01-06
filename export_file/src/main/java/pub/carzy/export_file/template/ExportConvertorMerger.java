package pub.carzy.export_file.template;

import org.springframework.beans.factory.InitializingBean;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author admin
 */
public class ExportConvertorMerger implements InitializingBean {
    /**
     * 文件创建工厂
     */
    @Resource
    protected List<FileWriteFactory> factories;

    @Resource
    protected List<ExportFileValueConvertor> convertors;

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
        synchronized (this) {
            ServiceLoader<FileWriteFactory> loader = ServiceLoader.load(FileWriteFactory.class);
            //添加
            for (FileWriteFactory factory : loader) {
                this.factories.add(factory);
            }
            ServiceLoader<ExportFileValueConvertor> convertorServiceLoader = ServiceLoader.load(ExportFileValueConvertor.class);
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
