package pub.carzy.export_file.file_export.actuator.convertor_impl;

import pub.carzy.export_file.file_export.ConvertorType;
import pub.carzy.export_file.file_export.actuator.ExportFileValueConvertor;
import pub.carzy.export_file.file_export.entity.ExportValueFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 位运算匹配选项转换器
 *
 * @author admin
 */
public class BitwiseOperationConvertor implements ExportFileValueConvertor {

    @Override
    public boolean match(ExportValueFormat convertor) {
        return convertor.getType() == ConvertorType.BITWISE_OPERATION;
    }

    /**
     * 不是一个数字就返回
     * extMap可选参数:
     *   operation:运算符,如果没有将原值返回,可选值:
     *     [&,|,^,~,<<,>>,>>>]
     *   count: 次数,当操作符是<<,>>,>>>时表示位移几次,区间在[1-64],当不在这个区间将返回原值
     *   join: 连字符,默认空串(无符号拼接)
     * @param convertor 转换器
     * @param value     值
     * @return 组合的列表
     */
    @Override
    public Object formatValue(ExportValueFormat convertor, Object value) {
        if (!(value instanceof Number)) {
            return value;
        }
        long v = (Integer) value;
        Map<String, String> extMap = convertor.getExtMap();
        Map<String, String> valueMap = convertor.getValueMap();
        String operation = extMap.get("operation");
        if (!checkOperation(operation)) {
            return value;
        }
        List<String> arr = new ArrayList<>();
        boolean skip = false;
        loop:
        for (Map.Entry<String, String> line : valueMap.entrySet()) {
            String key = line.getKey();
            try {
                long k = Integer.parseInt(key);
                switch (operation) {
                    case "&":
                        if ((v & k) == k) {
                            arr.add(line.getValue());
                        }
                        break;
                    case "|":
                        if ((v | k) == k) {
                            arr.add(line.getValue());
                        }
                        break;
                    case "^":
                        if ((v ^ k) == k) {
                            arr.add(line.getValue());
                        }
                        break;
                    case "~":
                        if ((~v) == k) {
                            arr.add(line.getValue());
                        }
                        break;
                    default:
                        if ("<<".equals(operation) || ">>".equals(operation) || ">>>".equals(operation)) {
                            int count = 1;
                            try {
                                count = extMap.get("count") == null ? count : Integer.parseInt(extMap.get("count"));
                            } catch (NumberFormatException ignored) {

                            }
                            if (count > 0) {
                                //这里是因为java的long类型，最大的位移次数是63次。
                                count = Math.min(count, 64);
                                switch (operation) {
                                    case "<<":
                                        if ((v << count) == k) {
                                            arr.add(line.getValue());
                                        }
                                        break;
                                    case ">>":
                                        if ((v >> count) == k) {
                                            arr.add(line.getValue());
                                        }
                                        break;
                                    case ">>>":
                                        if ((v >>> count) == k) {
                                            arr.add(line.getValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                skip = true;
                                //跳出整个循环
                                break loop;
                            }
                        }
                        break;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return skip?value: arr.stream().collect(Collectors.joining(extMap.get("join") == null ? "" : extMap.get("join")));
    }

    public static final String[] OPTS = new String[]{"&", "|", "^", "~", "<<", ">>", ">>>"};

    /**
     * 校验是否是合法运算符
     *
     * @param operation 运算符
     * @return 是否合法
     */
    private boolean checkOperation(String operation) {
        if (operation == null) {
            return false;
        }
        return Arrays.asList(OPTS).contains(operation);
    }
}
