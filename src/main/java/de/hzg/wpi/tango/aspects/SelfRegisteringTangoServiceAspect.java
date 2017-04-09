package de.hzg.wpi.tango.aspects;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceProxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tango.client.ez.proxy.NoSuchCommandException;
import org.tango.client.ez.proxy.TangoProxies;
import org.tango.client.ez.proxy.TangoProxy;
import org.tango.client.ez.proxy.TangoProxyException;
import org.tango.client.ez.util.TangoUtils;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 4/9/17
 */
@Aspect
public class SelfRegisteringTangoServiceAspect {
    public static final String TANGO_HOST = "TANGO_HOST";
    private final Logger logger = LoggerFactory.getLogger(SelfRegisteringTangoServiceAspect.class);

    @Pointcut("within(@SelfRegisteringTangoService *)")
    public void annotated() {
    }

    @Before("execution(void *.main(..)) && annotated()")
    public void doRegister(final JoinPoint point) {
        final Class<?> type = point.getStaticPart().getSignature().getDeclaringType();


        try {
            doRegisterInternal(type.getSimpleName(), type.getAnnotation(SelfRegisteringTangoService.class));
        } catch (NoSuchCommandException | TangoProxyException ex) {
            logger.warn("doRegister() has failed due to ", ex);
        } catch (DevFailed devFailed) {
            logger.warn("doRegister() has failed due to ", TangoUtils.convertDevFailedToException(devFailed));
        }
    }

    protected void doRegisterInternal(String simpleName, SelfRegisteringTangoService annot) throws TangoProxyException, DevFailed, NoSuchCommandException {
        TangoHost host = createTangoHost(annot.host(), annot.port());

        TangoProxy db = TangoProxies.newDeviceProxyWrapper(
                new DeviceProxy(annot.db(), host.host, host.port));

        String device = annot.domain() + "/" + annot.family() + "/" + annot.member();

        db.executeCommand("DbAddDevice", new String[]{simpleName + "/" + annot.domain(), device, simpleName});
    }

    private TangoHost createTangoHost(String host, String port) {
        String tangoHost = System.getProperty(TANGO_HOST, System.getenv(TANGO_HOST));
        if (tangoHost == null) {
            logger.debug("env.TANGO_HOST is empty. Setting TANGO_HOST={}:{}", host, port);
            TangoHost result = new TangoHost(host, port);
            System.setProperty(TANGO_HOST, result.toString());
            return result;
        }
        logger.info("env.TANGO_HOST={}", tangoHost);
        return new TangoHost(tangoHost);
    }

    private static class TangoHost {
        String value;
        String host;
        String port;

        public TangoHost(String value) {
            this.value = value;

            //TODO validate
            this.host = value.split(":")[0];
            this.port = value.split(":")[1];
        }

        public TangoHost(String host, String port) {
            this.value = host + ":" + port;

            this.host = host;
            this.port = port;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
