package de.hzg.wpi.tango.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This TANGO service will register itself in the TANGO DB (provided env.TANGO_HOST) if not yet defined.
 *
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 4/9/17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SelfRegisteringTangoService {
    String host() default "127.0.0.1";

    String port() default "10000";

    String db() default "sys/database/2";

    /**
     * @return domain for this service
     */
    String domain() default "development";

    /**
     * @return family for this service
     */
    String family() default "custom";

    /**
     * @return member for this service
     */
    String member() default "0";
}
