package ru.goodfil.catalog.web;

import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.web.utils.PageBean;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TabsBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@Managed
@ManagedBean
public class TabsBean extends PageBean {
    private String selectedTab;

    public String getSelectedTab() {
        return selectedTab;
    }

    @Logged
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }
}
