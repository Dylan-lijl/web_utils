package pub.carzy.util;


import pub.carzy.api.ResultCodeEnum;
/**
 * @author admin
 */
public class ExceptionHandler {
    public static void throwException(String s) {
        throw new ApplicationException(s);
    }
    public static void throwException(Long s) {
        throw new ApplicationException(s.toString());
    }
    public static void throwException(ResultCodeEnum code) {
        throwException(code.getCode());
    }

    public static RuntimeException newException(String s) {
        return new ApplicationException(s);
    }
    public static RuntimeException newException(Long s) {
        return new ApplicationException(s.toString());
    }
}
