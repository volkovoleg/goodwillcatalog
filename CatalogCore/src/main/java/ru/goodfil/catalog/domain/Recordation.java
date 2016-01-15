package ru.goodfil.catalog.domain;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.aspects.ValidationAspect;

@Entity
@Managed
public class Recordation implements Unique, MannStatus
{

    @Id
    @GeneratedValue(generator="CustomGenerator")
    @GenericGenerator(name="CustomGenerator", strategy="ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;
    private String deleteobject;
    private Calendar currentDate;
    private String ip;

    public static Recordation create(@NotNull @NotBlank String deleteobject)
    {
        Recordation recordation = new Recordation();
        recordation.setDeleteobject(deleteobject);
        recordation.setCurrentDate(Calendar.getInstance());
        try {
            recordation.setIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return recordation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeleteobject() {
        return this.deleteobject;
    }

    public void setDeleteobject(String deleteobject) {
        this.deleteobject = deleteobject;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Calendar getCurrentDate() {
        return this.currentDate;
    }

    public void setCurrentDate(Calendar currentDate) {
        this.currentDate = currentDate;
    }

    public void setMannStatus(Integer mannStatus)
    {
    }

    public Long getId()
    {
        return this.id;
    }


}
