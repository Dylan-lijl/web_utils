package pub.carzy.export_file.file_export.actuator.convertor_impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;
import pub.carzy.export_file.util.ObjectUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 解析json ,依赖于阿里的fastjson
 * type=6
 *
 * @author admin
 */
public class JsonExportFileValueConvertor implements ExportFileValueConvertor {
    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.JSON;
    }

    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        String type = convertor.getExtMap().get("type");
        if (value instanceof String && ObjectUtils.isNotBlank(value)) {
            if (ObjectUtils.isBlank(type) || "yaml".equalsIgnoreCase(type) || "yml".equalsIgnoreCase(type)) {
                try {
                    JSONObject jsonObject = JSON.parseObject(value.toString());
                    Map<String, Object> map = (Map<String, Object>) JSONObject.toJavaObject(jsonObject, Map.class);
                    DumperOptions options = new DumperOptions();
                    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                    return new Yaml(options).dump(map);
                } catch (Exception e) {
                    return null;
                }
            } else if ("properties".equalsIgnoreCase(type)) {
                try {
                    Properties properties = convertJsonToProperties(JSON.parseObject(value.toString()));
                    return properties.toString();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return value;
    }

    Properties convertJsonToProperties(JSONObject json) {
        Properties props = new Properties();
        Iterator<String> iterator = json.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                Properties nestedProps = convertJsonToProperties((JSONObject) value);
                for (String nestedKey : nestedProps.stringPropertyNames()) {
                    String fullKey = key + "." + nestedKey;
                    props.setProperty(fullKey, nestedProps.getProperty(nestedKey));
                }
            } else if (value instanceof Object[]) {
                Object[] array = (Object[]) value;
                for (int i = 0; i < array.length; i++) {
                    if (array[i] instanceof JSONObject) {
                        Properties nestedProps = convertJsonToProperties((JSONObject) array[i]);
                        for (String nestedKey : nestedProps.stringPropertyNames()) {
                            String fullKey = key + "[" + i + "]." + nestedKey;
                            props.setProperty(fullKey, nestedProps.getProperty(nestedKey));
                        }
                    } else {
                        String fullKey = key + "[" + i + "]";
                        props.setProperty(fullKey, array[i].toString());
                    }
                }
            } else {
                props.setProperty(key, value.toString());
            }
        }
        return props;
    }
}
