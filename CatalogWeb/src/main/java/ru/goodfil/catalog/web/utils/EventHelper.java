package ru.goodfil.catalog.web.utils;

import org.apache.commons.beanutils.MethodUtils;
import ru.goodfil.catalog.annotations.web.Init;

import javax.faces.context.FacesContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: EventHelper.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class EventHelper {
    private static final Logger logger = Logger.getLogger(EventHelper.class.getName());

    public static void init(String beanName, Object... args) {
        Object obj = FacesUtils.getManagedBean(beanName);

        if (obj == null) {
            logger.warning("Skipping event " + beanName + "#init(" + args.length + " args)");
        }

        String methodName = null;
        Class klass = obj.getClass();
        Method[] methods = klass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Init.class)) {
                methodName = method.getName();
            }
        }

        if (methodName == null) {
            logger.warning("Skipping event " + beanName + "#init(" + args.length + " args)");
        }

        try {
            MethodUtils.invokeMethod(obj, methodName, args);
        } catch (NoSuchMethodException e) {
            getExceptionInfo(e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            getExceptionInfo(e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            getExceptionInfo(e);
            throw new RuntimeException(e);
        }
    }

    public static void getExceptionInfo(Exception e){
        for (StackTraceElement st : e.getStackTrace())
        {
            System.out.println("Class: " + st.getClassName() + " Method : "
                    +  st.getMethodName() + " line : " + st.getLineNumber());
        }
    }
}
