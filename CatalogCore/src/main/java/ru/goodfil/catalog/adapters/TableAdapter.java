package ru.goodfil.catalog.adapters;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.domain.Recordation;
import ru.goodfil.catalog.domain.Unique;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.HibernateTemplateWithoutTransaction;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TableAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public abstract class TableAdapter<T extends Unique> {
    private final Class<T> klass;
    protected final SessionProvider sessionProvider;
    private final ValidationProvider validationProvider;

    public TableAdapter(Class<T> klass, SessionProvider sessionProvider, ValidationProvider validationProvider) {
        this.klass = klass;
        this.sessionProvider = sessionProvider;
        this.validationProvider = validationProvider;
    }

    public T getById(@NotNull final Long id) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (T) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.get(klass, id);
            }
        }.execute();
    }

    public List<T> getAll() {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<T>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(klass)
                        .addOrder(Order.asc("name"))
                        .list();
            }
        }.execute();
    }

    public List<T> getAllSorted(final String field) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<T>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(klass)
                        .addOrder(Order.asc(field))
                        .list();
            }
        }.execute();
    }

    protected List<T> getByCriteria(@NotNull final Criterion... criterions) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<T>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                Criteria criteria = session.createCriteria(klass);

                for (Criterion criterion : criterions) {
                    criteria.add(criterion);
                }
                return criteria.list();
            }
        }.execute();

    }

    protected List<T> getByCriteria(@NotNull final Criterion criterion1, @NotNull final Order order1) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<T>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                Criteria criteria = session.createCriteria(klass);
                criteria.add(criterion1);
                criteria.addOrder(order1);

                return criteria.list();
            }
        }.execute();
    }

    protected List<T> getByCriteria(@NotNull final Criterion criterion1, @NotNull final Criterion criterion2, @NotNull final Order order1) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<T>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                Criteria criteria = session.createCriteria(klass);
                criteria.add(criterion1);
                criteria.add(criterion2);
                criteria.addOrder(order1);

                return criteria.list();
            }
        }.execute();
    }

    public void save(@NotNull @NotEmpty final Collection<T> objects) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        for (T object : objects) {
            validate(object);
        }

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                for (T object : objects) {
                    session.save(object);
                }
                return null;
            }
        }.execute();
    }

    public void save(@NotNull final T object) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        validate(object);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                session.save(object);
                return null;
            }
        }.execute();
    }

    public void merge(@NotNull final T object) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        validate(object);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                session.merge(object);
                return null;
            }
        }.execute();
    }

    public void merge(@NotNull @NotEmpty final Collection<T> objects) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        for (T object : objects) {
            validate(object);
        }

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                for (T object : objects) {
                    session.merge(object);
                }
                return null;
            }
        }.execute();
    }

    public void delete(@NotNull final Long id) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                Object item = session.get(klass, id);
                session.save(Recordation.create(item.toString()));
                session.delete(item);
                return null;
            }
        }.execute();
    }

    private void validate(@NotNull final T object) {
        Assert.notNull(validationProvider);

        validationProvider.validate(object);
    }
}
