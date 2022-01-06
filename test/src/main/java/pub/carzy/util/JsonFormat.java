package pub.carzy.util;

import java.lang.annotation.*;

/**
 * @author admin
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonFormat {
    /**
     * 表达式
     * @see #pattern()
     * @return
     */
    String value() default "";

    /**
     * @see #value()
     * @return
     */
    String pattern() default "";
}
