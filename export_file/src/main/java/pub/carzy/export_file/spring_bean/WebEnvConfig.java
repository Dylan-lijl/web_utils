package pub.carzy.export_file.spring_bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author admin
 */
@ConfigurationProperties("web")
@Component
@Data
public class WebEnvConfig {
    private Map<String,String> export;
}
