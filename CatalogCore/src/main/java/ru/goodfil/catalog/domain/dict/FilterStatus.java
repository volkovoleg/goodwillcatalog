package ru.goodfil.catalog.domain.dict;

/**
 * Простая вариация битовой маски, для хранения статусов фильтра
 */
public class FilterStatus {

    private final static int DEPENDING = 0;

    private final static int DELIVERING = 1;

    private final static int NOT_DELIVERING = 2;

    public static String asString(int value)
    {
        switch (value)
        {
            case FilterStatus.DEPENDING: return "В ПРОИЗВОДСТВЕ";
            case FilterStatus.DELIVERING: return "ПОСТАВЛЯЕТСЯ";
            case FilterStatus.NOT_DELIVERING: return "НЕ ПОСТАВЛЯЕТСЯ";
        }
        return "";
    }

    public static int getKey(String value){
        if(value.equals("В ПРОИЗВОДСТВЕ")) return 0;
        if(value.equals("НЕ ПОСТАВЛЯЕТСЯ")) return 2;
        else return 1;
    }
}
