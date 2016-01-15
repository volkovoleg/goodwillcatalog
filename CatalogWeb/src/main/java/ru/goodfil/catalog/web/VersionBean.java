package ru.goodfil.catalog.web;

import ru.goodfil.catalog.utils.Version;
import ru.goodfil.catalog.web.utils.SessionMonitorListener;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: VersionBean.java 173 2013-11-14 13:52:52Z chezxxx@gmail.com $
 */
public class VersionBean {
    public String getVersion() {
        return Version.VERSION;
    }

    public String getBuildDate() {
        return Version.BUILD_TIMESTAMP;
    }
    
    public int getAS(){
        return SessionMonitorListener.getTotalActiveSession();
    }
}
