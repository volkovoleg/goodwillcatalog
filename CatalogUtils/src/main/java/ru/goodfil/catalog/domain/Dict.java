package ru.goodfil.catalog.domain;

/**
 * Интерфейс определяет методы, которые должны быть у сущности-справочника.
 * @author sazonovkirill@gmail.com
 * @version $Id: Dict.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public interface Dict {
    public Long getId();
    public void setId(Long id);

    public String getCode();
    public void setCode(String code);

    public String getName();
    public void setName(String name);
}
