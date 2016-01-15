package ru.goodfil.catalog.utils;

import org.hibernate.Session;

import javax.validation.constraints.NotNull;

public abstract class HibernateTemplateWithoutTransaction {
    private final Session session;

    protected HibernateTemplateWithoutTransaction(@NotNull Session session) {
        Assert.notNull(session);
        this.session = session;
    }

    protected abstract Object run(Session session);

    public Object execute() {
        Object result = null;
        try {
            result = run(session);
        } catch (Exception e) {
            for (StackTraceElement st : e.getStackTrace())
            {
                System.out.println("Class: " + st.getClassName() + " Method : "
                        +  st.getMethodName() + " line : " + st.getLineNumber());
            }
            throw new RuntimeException(e);
        } finally {
        }
        return result;
    }
}
