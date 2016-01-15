package ru.goodfil.catalog.web.utils;

import ru.goodfil.catalog.utils.Assert;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FacesUtils.java 109 2012-12-22 08:01:20Z sazonovkirill $
 */
public class FacesUtils {
    public static <T> T getManagedBean(String beanName) {
        return (T) getApplication().getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), beanName);
    }

    protected static Application getApplication() {
        return FacesContext.getCurrentInstance().getApplication();
    }

    public static void redirect(String viewId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        try {
            response.sendRedirect(request.getContextPath() + viewId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void download(InputStream is) throws IOException {
        Assert.notNull(is);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        response.setContentType("application/vnd.ms-excel"); // Check http://www.w3schools.com/media/media_mimeref.asp for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
        response.setHeader("Content-disposition", "attachment; filename=\"catalog.xls\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            input = new BufferedInputStream(is);
            output = new BufferedOutputStream(response.getOutputStream());

            byte[] buffer = new byte[10240];
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
        } finally {
            try {
                if (output != null) output.close();
            } catch (Exception e) {}
            try {
                if (is != null) is.close();
            } catch (Exception e) {}
        }

        facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }
}
