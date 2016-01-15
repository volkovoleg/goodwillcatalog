package ru.goodfil.catalog.web.utils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SessionMonitorListener.java 174 2013-12-19 07:30:39Z chezxxx@gmail.com $
 */
public class SessionMonitorListener implements HttpSessionListener {

    private static int totalActiveSessions;

    public static synchronized int getTotalActiveSession(){
        return totalActiveSessions;
    }

    private synchronized  void incrementSessionNumber(){
        totalActiveSessions++;
    }

    private synchronized  void decrementSessionNumber(){
        totalActiveSessions--;
    }
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        incrementSessionNumber();
        System.out.println("Session created");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        decrementSessionNumber();
        System.out.println("Session destoyed");
    }
}
