package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Clear;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.annotations.web.PageAction;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.ListAsMap;
import ru.goodfil.catalog.web.utils.PageBean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CrossReferenceBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@Managed
@ManagedBean
public class CrossReferenceBean extends PageBean {
    @NotNull
    @Inject
    private CarsService carsService;

    @NotNull
    @Inject
    private FiltersService filtersService;

    @NotNull
    @Inject
    private AnalogsService analogsService;

    private String searchText;

    private final List<OeModel> oes = new ArrayList<OeModel>();

    private String noSpamText = "";

    private String brandName = "";

    private List<OeModel> wrongModel = new ArrayList();

    public CrossReferenceBean() {
        init();
    }

    @Init
    @Logged
    @ValidateAfter
    public void init() {
    }

    @Clear
    @Logged
    public void clear() {

    }

    @PageAction
    public void search() throws IOException {
        Assert.notNull(searchText);
/*        try {
            checkValueForSpam(noSpamtext);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (searchText == null || StringUtils.isBlank(searchText) || searchText.length() < 2) {
            //  Введите не менее двух символов
            return;
        }

        if(checkValueForSpam(noSpamText)) {
            return;
        }

        this.oes.clear();
        List<Brand> brands = analogsService.getBrands();
        Map<Long, Brand> brandsMap = ListAsMap.get(brands);


        List<Oe> oes = analogsService.searchOesByName(searchText);
        Set<Long> oesIds = new HashSet<Long>();
        for (Oe oe : oes) {
            oesIds.add(oe.getId());
        }

        if(oesIds.size() != 0){
            setBrandName(analogsService.getBrandById(oes.get(0).getBrandId()).getName());
        }
        Map<Long, List<Filter>> filtersMap = filtersService.getFiltersByOes(oesIds);
        for (Oe oe : oes) {
            Brand brand = brandsMap.get(oe.getBrandId());
            Assert.notNull(brand);
            Long brandId = brand.getId();
            String brandName = brand.getName();
            Assert.notNull(brandId);
            Assert.notNull(brandName);
            List<Filter> filters = filtersMap.get(oe.getId());
            if (filters != null) {
                for (Filter filter : filters) {
                    if (!filter.getOnSite()) {
                        continue;
                    }

                    OeModel oeModel = new OeModel();
                    oeModel.setOeId(oe.getId());
                    oeModel.setOnSite(filter.getOnSite());
                    oeModel.setOeName(oe.getName());
                    oeModel.setFilterName(filter.getName());
                    oeModel.setFilterId(filter.getId());
                    oeModel.setBrandId(brandId);
                    oeModel.setBrandName(brandName);
                    this.oes.add(oeModel);
                }
            }
        }
    }

    public List<OeModel> getOes() {
        return oes;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public CarsService getCarsService() {
        return carsService;
    }

    public void setCarsService(CarsService carsService) {
        this.carsService = carsService;
    }

    public FiltersService getFiltersService() {
        return filtersService;
    }

    public void setFiltersService(FiltersService filtersService) {
        this.filtersService = filtersService;
    }

    public String getNoSpamText() {
        return noSpamText;
    }

    public void setNoSpamText(String noSpamtext) {
        this.noSpamText = noSpamtext;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Managed
    public static class OeModel {
        private Long oeId;
        private String oeName;
        private Long brandId;
        private String brandName;
        private Long filterId;
        private String filterName;
        private Boolean onSite;

        public Long getOeId() {
            return oeId;
        }

        public void setOeId(@NotNull Long oeId) {
            this.oeId = oeId;
        }

        public String getOeName() {
            return oeName;
        }

        public void setOeName(String oeName) {
            this.oeName = oeName;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(@NotNull Long brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public Long getFilterId() {
            return filterId;
        }

        public void setFilterId(@NotNull Long filterId) {
            this.filterId = filterId;
        }

        public String getFilterName() {
            return filterName;
        }

        public void setFilterName(String filterName) {
            this.filterName = filterName;
        }

        public Boolean getOnSite() {
            return onSite;
        }

        public void setOnSite(Boolean onSite) {
            this.onSite = onSite;
        }
    }
}
