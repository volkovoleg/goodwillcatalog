package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.annotations.web.PageAction;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.Unique;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.web.utils.JSFComboboxModel;
import ru.goodfil.catalog.web.utils.PageBean;

import javax.faces.model.SelectItem;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SearchSizeBean.java 179 2014-07-18 12:31:54Z chezxxx@gmail.com $
 */
@Managed
@ManagedBean
public class SearchSizeBean extends PageBean {

    @NotNull
    @Inject
    private CarsService carsService;

    @NotNull
    @Inject
    private FiltersService filtersService;

    private JSFComboboxModel filterFormsCombo;
    private List<FilterForm> filterForms;
    private List<FilterType> filterTypes;
    
    private String imageFilterForm;
    private boolean imageExist=false;

    private final List<Filter> filters = new ArrayList<Filter>();

    private final List<String> paramsExtSuggestionValues = new ArrayList<String>() {{
        add("10");
        add("20");
        add("30");
        add("40");
        add("50");
    }};

    private String gParam;

    private String noSpamText = "";

    public SearchSizeBean() {
        init();
    }

    public List<SelectItem> getAllGParams(){
        List<SelectItem> items=new ArrayList<SelectItem>();
        List<String> gList=filtersService.getAllGParams();
        items.add(new SelectItem(null,"[не выбрано]"));
        for(String item:gList){
            if(!item.equals("")){
            items.add(new SelectItem(item));
            }
        }
        return items;
    }

    private static BigDecimal tryParseNumber(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        try {
            s = s.replace(',', '.');
            return new BigDecimal(s).setScale(5, BigDecimal.ROUND_HALF_UP);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean isGInInterval(String filterGParam, String chosedFilterGParam){
        if(filterGParam.equals(chosedFilterGParam)){
            return true;
        }else {
            return false;
        }
    }

    private static boolean isInInterval(BigDecimal a, BigDecimal[] ad) {
        if (ad == null || ad.length != 2 || ad[0] == null || ad[1] == null) {
            return true;
        }

        if (a != null && a.compareTo(ad[0]) >= 0 && a.compareTo(ad[1]) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private BigDecimal[] getEdges(String s) {
        BigDecimal value = tryParseNumber(paramsValues.get(s));

        if (value != null) {
            BigDecimal i = tryParseNumber(paramsExtValues.get(s));
            if (i == null) {

            } else {
                return new BigDecimal[]{
                        value.subtract(i).setScale(5, BigDecimal.ROUND_HALF_UP),
                        value.add(i).setScale(5, BigDecimal.ROUND_HALF_UP)
                };
            }
        }

        return null;
    }

    @PageAction
    @Logged
    public void searchAction() throws IOException {
        if (filterFormsCombo.getSelectedValue() == null) {
            //  Выберите форму фильтра
            return;
        }

        if(checkValueForSpam(noSpamText)) {
            return;
        }

        List<Filter> allFilters = filterFiltersByGoodwillBrand(filtersService.getFiltersByFilterFormId(filterFormsCombo.getSelectedValueAsLong()));

        BigDecimal[] ad = getEdges("a");
        BigDecimal[] bd = getEdges("b");
        BigDecimal[] cd = getEdges("c");
        BigDecimal[] dd = getEdges("d");
        BigDecimal[] ed = getEdges("e");
        BigDecimal[] fd = getEdges("f");
     //   BigDecimal[] gd = getEdges("g");
        BigDecimal[] hd = getEdges("h");

        filters.clear();
        if (ad == null && bd == null && cd == null && dd == null && ed == null && fd == null && gParam.equals("") && hd == null) {
            //  Укажите параметры поиска
            return;
        }
        for (Filter filter : allFilters) {
            if (filter.getDisabled()) {
                continue;
            }

            BigDecimal a = tryParseNumber(filter.getaParam());
            BigDecimal b = tryParseNumber(filter.getbParam());
            BigDecimal c = tryParseNumber(filter.getcParam());
            BigDecimal d = tryParseNumber(filter.getdParam());
            BigDecimal e = tryParseNumber(filter.geteParam());
            BigDecimal f = tryParseNumber(filter.getfParam());
           // BigDecimal g = tryParseNumber(filter.getgParam());
            BigDecimal h = tryParseNumber(filter.gethParam());

            if (isInInterval(a, ad) &&
                    isInInterval(b, bd) &&
                    isInInterval(c, cd) &&
                    isInInterval(d, dd) &&
                    isInInterval(e, ed) &&
                    isInInterval(f, fd) &&
                   // isInInterval(g, gd) &&
                    isGInInterval(gParam,filter.getgParam()) &&
                    isInInterval(h, hd)) {
                filters.add(filter);
            }

           System.out.println(isInInterval(a, ad));
                   System.out.println(isInInterval(b, bd));
                           System.out.println(isInInterval(c, cd));
                                   System.out.println(isInInterval(d, dd));
                                           System.out.println(isInInterval(e, ed));
                                                   System.out.println(isInInterval(f, fd) );
                    // isInInterval(g, gd) &&
                                                           System.out.println("g param"+isGInInterval(gParam,filter.getgParam()));
                                                                   System.out.println(isInInterval(h, hd));
               /**
            if (isInInterval(a, ad) ||
                    isInInterval(b, bd) ||
                    isInInterval(c, cd) ||
                    isInInterval(d, dd) ||
                    isInInterval(e, ed) ||
                    isInInterval(f, fd) ||
                    isInInterval(g, gd) ||
                    isInInterval(h, hd)) {
                filters.add(filter);
        }     */
        }
    }

    @Init
    @Logged
    @ValidateAfter
    public void init() {
        if (System.getProperty("disable.search.by.size") != null && System.getProperty("disable.search.by.size").equals("1")) {
          visiblePage=false;
        } else {
          visiblePage=true;
        }
        filterTypes = carsService.getFilterTypes();
        filterForms = carsService.getFilterForms();

        final List<FilterFormModel> filterFormModelList = new ArrayList<FilterFormModel>();
        for (FilterForm filterForm : filterForms) {
            for (FilterType filterType : filterTypes) {
                if (filterForm.getFilterTypeCode().equals(filterType.getCode())) {
                    filterFormModelList.add(
                            new FilterFormModel(
                                    filterType.getId(),
                                    filterForm.getId(),
                                    filterType.getName(),
                                    filterForm.getName()));
                }
            }
        }

        Collections.sort(filterFormModelList, new Comparator<FilterFormModel>() {
            @Override
            public int compare(FilterFormModel o1, FilterFormModel o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        filterFormsCombo = new JSFComboboxModel();
        filterFormsCombo.setItems(asSelectItems(filterFormModelList),true);
        gParam = "";
    }

    private final Map<String, String> paramsValues = new HashMap<String, String>();
    private final Map<String, String> paramsExtValues = new HashMap<String, String>() {{
        put("a", "10");
        put("b", "10");
        put("c", "10");
        put("d", "10");
        put("e", "10");
        put("f", "10");
       // put("g", "10");
        put("h", "10");

    }};

    public List<SelectItem> getParamsExtSelectItems() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (String s : paramsExtSuggestionValues) {
            result.add(new SelectItem(new Long(s), s));
        }
        return result;
    }

    public Map<String, Boolean> getAvailableParameters() {
        Long selectedFilterTypeId = filterFormsCombo.getSelectedValueAsLong();
        if (selectedFilterTypeId == null) {
            return new HashMap<String, Boolean>() {{
                put("a", false);
                put("b", false);
                put("c", false);
                put("d", false);
                put("e", false);
                put("f", false);
                put("g", false);
                put("h", false);
            }};
        } else {
            for (final FilterForm filterForm : filterForms) {
                if (filterForm.getId().equals(selectedFilterTypeId)) {
                    return new HashMap<String, Boolean>() {{
                        put("a", filterForm.getaParam());
                        put("b", filterForm.getbParam());
                        put("c", filterForm.getcParam());
                        put("d", filterForm.getdParam());
                        put("e", filterForm.geteParam());
                        put("f", filterForm.getfParam());
                        put("g", filterForm.getgParam());
                        put("h", filterForm.gethParam());
                    }};
                }
            }
        }

        return null;
    }

    private List<Filter> filterFiltersByGoodwillBrand(List<Filter> filters) {
        List<Filter> result = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (filter.getOnSite()) {
                result.add(filter);
            }
        }
        return result;
    }

    /**
     * Возвращает путь к изображению текущей форме фильтра.
     * Если имя изображения указано без расшрения - тогда подразумевается jpg.
     */
    public void addImagePathAndRefresh() {
        refreshMetrics();
        filters.clear();
        Long filterFormId = filterFormsCombo.getSelectedValueAsLong();
        if (filterFormId != null) {
            FilterForm filterForm = carsService.getFilterFormById(filterFormId);
            String image = filterForm.getImage();
            if (image.equals("")) {
                imageExist = false;
                return;
            }
            if (image.endsWith(".jpg") ||
                    image.endsWith(".gif") ||
                    image.endsWith(".png")) {
                imageFilterForm = image.trim();
            } else {
                imageFilterForm = image + ".jpg";
            }
            imageExist = true;
        }
        if (filterFormId == null) {
            imageExist = false;
        }
    }

    void refreshMetrics() {
        if (paramsValues.containsKey("a")) {
            paramsValues.put("a", null);
        }
        if (paramsValues.containsKey("b")) {
            paramsValues.put("b", null);
        }
        if (paramsValues.containsKey("c")) {
            paramsValues.put("c", null);
        }
        if (paramsValues.containsKey("d")) {
            paramsValues.put("d", null);
        }
        if (paramsValues.containsKey("e")) {
            paramsValues.put("e", null);
        }
        if (paramsValues.containsKey("f")) {
            paramsValues.put("f", null);
        }
        if (paramsValues.containsKey("g")) {
            paramsValues.put("g", null);
        }
        if (paramsValues.containsKey("h")) {
            paramsValues.put("h", null);
        }
        if (paramsExtValues.containsKey("a")) {
            paramsExtValues.put("a", "10");
        }
        if (paramsExtValues.containsKey("b")) {
            paramsExtValues.put("b", "10");
        }
        if (paramsExtValues.containsKey("c")) {
            paramsExtValues.put("c", "10");
        }
        if (paramsExtValues.containsKey("d")) {
            paramsExtValues.put("d", "10");
        }
        if (paramsExtValues.containsKey("e")) {
            paramsExtValues.put("e", "10");
        }
        if (paramsExtValues.containsKey("f")) {
            paramsExtValues.put("f", "10");
        }
        /** Тип поля теперь будет изменен
        if (paramsExtValues.containsKey("g")) {
            paramsExtValues.put("g", "10");
        }
         */
        if (paramsExtValues.containsKey("h")) {
            paramsExtValues.put("h", "10");
        }
    }

    public boolean isImageExist() {
        return imageExist;
    }

    public void setImageExist(boolean imageExist) {
        this.imageExist = imageExist;
    }

    public String getImageFilterForm() {
        return imageFilterForm;
    }

    public void setImageFilterForm(String imageFilterForm) {
        this.imageFilterForm = imageFilterForm;
    }

    public Boolean visiblePage=true;

    public Boolean getVisiblePage() {
        return visiblePage;
    }

    public FiltersService getFiltersService() {
        return filtersService;
    }

    public void setFiltersService(FiltersService filtersService) {
        this.filtersService = filtersService;
    }

    public JSFComboboxModel getFilterFormsCombo() {
        return i18n(filterFormsCombo);
    }

    public Map<String, String> getParamsValues() {
        return paramsValues;
    }

    public Map<String, String> getParamsExtValues() {
        return paramsExtValues;
    }

    public List<String> getParamsExtSuggestionValues() {
        return paramsExtSuggestionValues;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    @Managed
    public static class FilterFormModel implements Unique {
        @NotNull
        private Long filterTypeId;

        @NotNull
        private Long filterFormId;

        @NotBlank
        private String filterTypeName;

        @NotBlank
        private String filterFormName;

        public FilterFormModel(@NotNull Long filterTypeId, @NotNull Long filterFormId, @NotNull @NotBlank String filterTypeName, @NotNull @NotBlank String filterFormName) {
            this.filterTypeId = filterTypeId;
            this.filterFormId = filterFormId;
            this.filterTypeName = filterTypeName;
            this.filterFormName = filterFormName;
        }

        @Override
        public Long getId() {
            return filterFormId;
        }

        @Override
        public String toString() {
            return filterTypeName + " (" + filterFormName + ")";
        }

        public Long getFilterTypeId() {
            return filterTypeId;
        }

        public void setFilterTypeId(@NotNull Long filterTypeId) {
            this.filterTypeId = filterTypeId;
        }

        public Long getFilterFormId() {
            return filterFormId;
        }

        public void setFilterFormId(@NotNull Long filterFormId) {
            this.filterFormId = filterFormId;
        }

        public String getFilterTypeName() {
            return filterTypeName;
        }

        public void setFilterTypeName(@NotNull @NotBlank String filterTypeName) {
            this.filterTypeName = filterTypeName;
        }

        public String getFilterFormName() {
            return filterFormName;
        }

        public void setFilterFormName(@NotNull @NotBlank String filterFormName) {
            this.filterFormName = filterFormName;
        }
    }

    public String getgParam() {
        return gParam;
    }

    public void setgParam(String gParam) {
        this.gParam = gParam;
    }

    public String getNoSpamText() {
        return noSpamText;
    }

    public void setNoSpamText(String noSpamText) {
        this.noSpamText = noSpamText;
    }
}
