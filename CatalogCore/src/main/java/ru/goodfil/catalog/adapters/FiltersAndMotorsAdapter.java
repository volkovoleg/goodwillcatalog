package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.FiltersAndMotors;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.HibernateTemplateWithoutTransaction;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FiltersAndMotorsAdapter.java 178 2014-07-10 13:25:32Z chezxxx@gmail.com $
 */
@Managed
public class FiltersAndMotorsAdapter extends TableAdapter<FiltersAndMotors> {
    @Inject
    public FiltersAndMotorsAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(FiltersAndMotors.class, sessionProvider, validationProvider);
    }

    public List<FiltersAndMotors> getByMotorId(@NotNull final Long motorId) {
        return getByCriteria(Restrictions.eq("motorId", motorId));
    }

    public List<FiltersAndMotors> getByMotorIds(@NotNull @NotEmpty final Set<Long> motorIds) {
        return getByCriteria(Restrictions.in("motorId", motorIds));
    }

    public List<FiltersAndMotors> getByFilterId(@NotNull final Long filterId) {
        return getByCriteria(Restrictions.eq("filterId", filterId));
    }

    public List<FiltersAndMotors> getByFilterIds(@NotNull @NotEmpty final Set<Long> filterIds) {
        return getByCriteria(Restrictions.in("filterId", filterIds));
    }

    public void deleteByFilterId(@NotNull final Long filterId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from FiltersAndMotors o where o.filterId = :filterId");
                q.setLong("filterId", filterId);
                q.executeUpdate();
                return null;
            }
        }.execute();
    }

    public void deleteByMotorId(@NotNull final Long motorId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from FiltersAndMotors o where o.motorId = :motorId");
                q.setLong("motorId", motorId);
                q.executeUpdate();
                return null;
            }
        }.execute();
    }

    public void deleteById(@NotNull final Long id) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                Query q = session.createQuery("delete from FiltersAndMotors o where o.id = :id");
                q.setLong("id", id);
                q.executeUpdate();
                return null;
            }
        }.execute();

    }

    public int getCountByFilterIdAndMotorId(@NotNull final Long filterId, @NotNull final Long motorId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        Object result =
        new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("select count(*) from FiltersAndMotors o where o.filterId = :filterId and o.motorId = :motorId");
                q.setLong("filterId", filterId);
                q.setLong("motorId", motorId);
                return q.uniqueResult();
            }
        }.execute();

        return new Integer(result.toString());
    }

    public int getCountByFilterIdAndMotorsIds(@NotNull final Long filterId, @NotNull final Set<Long> motorsIds) {
        if (motorsIds.size() == 0) {
            return 0;
        }

        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        Object result =
        new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("select count(*) from FiltersAndMotors o where o.filterId = :filterId and o.motorId in (:motorsIds)");
                q.setLong("filterId", filterId);
                q.setParameterList("motorsIds", motorsIds);
                return q.uniqueResult();
            }
        }.execute();

        return new Integer(result.toString());
    }

    public FiltersAndMotors getFilterAndMotor(@NotNull final Long filterId, @NotNull final Long motorId){
     List<FiltersAndMotors> filtersAndMotorses =getByCriteria(Restrictions.eq("filterId", filterId),Restrictions.eq("motorId", motorId));
        if (filtersAndMotorses.size() > 1){
            for(int i = 1; i < filtersAndMotorses.size(); i++){
                deleteById(filtersAndMotorses.get(i).getId());
            }
            return filtersAndMotorses.get(0);
        }
       // Assert.isTrue(filtersAndMotorses.size() < 2);
        if (filtersAndMotorses.size() == 1) {
            return filtersAndMotorses.get(0);
        } else {
            return null;
        }
    }

    public void updateFiltersAndMotors(@NotNull final FiltersAndMotors filtersAndMotors) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);
        Integer updateCount = 0;
        updateCount = (Integer) new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                Integer result = session.createQuery("update FiltersAndMotors o set o.filterComment = :filter_comment where id = :filers_and_motors_id")
                        .setParameter("filter_comment", filtersAndMotors.getFilterComment())
                        .setParameter("filers_and_motors_id", filtersAndMotors.getId())
                        .executeUpdate();

                return result;
            }
        }.execute();
    }
}
