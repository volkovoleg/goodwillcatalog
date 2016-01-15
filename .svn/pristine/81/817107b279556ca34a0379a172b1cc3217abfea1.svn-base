package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import org.apache.commons.collections.map.HashedMap;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Clear;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.annotations.web.PageAction;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.export.domain.oeexport.OesDocumentModel;
import ru.goodfil.catalog.export.service.ExportService;
import ru.goodfil.catalog.reporting.OesExporter;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.web.utils.FacesUtils;
import ru.goodfil.catalog.web.utils.FilePublisher;
import ru.goodfil.catalog.web.utils.PageBean;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
import java.util.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

/**
 * @author Vit
 */
@Managed
@ManagedBean
public class UnloadingByBrandBean extends PageBean {
    @NotNull
    @Inject
    private AnalogsService analogService;
    @NotNull
    @Inject
    private ExportService exportService;
    List<BrandConfirmModel> brandConfirmModelList1 = new ArrayList<BrandConfirmModel>();
    List<BrandConfirmModel> brandConfirmModelList2 = new ArrayList<BrandConfirmModel>();
    List<BrandConfirmModel> brandConfirmModelList3 = new ArrayList<BrandConfirmModel>();
    List<BrandConfirmModel> brandConfirmModelList4 = new ArrayList<BrandConfirmModel>();
    List<BrandConfirmModel> brandConfirmModelListAll = new ArrayList<BrandConfirmModel>();
    private boolean startedConfirm = false;

    public UnloadingByBrandBean() {
        init();
    }

    @Init
    @Logged
    @ValidateAfter
    public void init() {
        List<Brand> brandList = new ArrayList<Brand>();
        brandList.addAll(analogService.getBrandsForUnload());
        if (brandList.size() >= 4) {
            int all = brandList.size();
            int oneColumn = all / 4;
            int count = 1;
            for (Brand brand : brandList) {
                if (count <= oneColumn) {
                    brandConfirmModelList1.add(new BrandConfirmModel(brand, startedConfirm));
                } else if (count <= (oneColumn * 2)) {
                    brandConfirmModelList2.add(new BrandConfirmModel(brand, startedConfirm));
                } else if (count <= (oneColumn * 3)) {
                    brandConfirmModelList3.add(new BrandConfirmModel(brand, startedConfirm));
                } else if (count > (oneColumn * 3)) {
                    brandConfirmModelList4.add(new BrandConfirmModel(brand, startedConfirm));
                }
                count++;
            }
        }
    }

    @Managed
    public static class BrandConfirmModel {
        private Brand brand;
        private boolean used;

        public BrandConfirmModel(Brand brand, boolean used) {
            this.brand = brand;
            this.used = used;
        }

        public Brand getBrand() {
            return brand;
        }

        public void setBrand(Brand brand) {
            this.brand = brand;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }
    }


    public String create() {
        brandConfirmModelListAll.clear();
        putToAll(brandConfirmModelList1);
        putToAll(brandConfirmModelList2);
        putToAll(brandConfirmModelList3);
        putToAll(brandConfirmModelList4);
        System.gc();
        if (brandConfirmModelListAll.size() > 0) {
            List<Brand> brandsToUnload = new ArrayList<Brand>();
            for (BrandConfirmModel model : brandConfirmModelListAll) {
                if (model.isUsed()) {
                    brandsToUnload.add(model.getBrand());
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            System.out.println("Began at " + sdf.format(Calendar.getInstance().getTime()));
            if (brandsToUnload.size() > 0) {
                OesDocumentModel oesDocumentModel = exportService.getOesByBrands(brandsToUnload);
                if (oesDocumentModel.getRows().size() > 0) {
                    try {
                        File f = File.createTempFile("goodwill", "catalog");
                        OesExporter.exportToCross(oesDocumentModel, f);
                        System.out.println("Finished at " + sdf.format(Calendar.getInstance().getTime()));
                        exportFile(f);
                        //FacesUtils.download(new FileInputStream(f));
                        System.out.println("Downloaded at " + sdf.format(Calendar.getInstance().getTime()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }   else return null;
        }
        System.gc();
        return "done";
    }

    @PageAction
    public void selectAll() {
        brandConfirmModelListAll.clear();
        putToAll(brandConfirmModelList1);
        putToAll(brandConfirmModelList2);
        putToAll(brandConfirmModelList3);
        putToAll(brandConfirmModelList4);
        if (brandConfirmModelListAll.size() >= 4) {
            startedConfirm = !startedConfirm;
            for (BrandConfirmModel model : brandConfirmModelListAll) {
                model.setUsed(startedConfirm);
            }
        }
    }

    private FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    private void exportFile(File tempFile) throws IOException {
        String uid = UUID.randomUUID().toString();
        HttpServletRequest request = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();

        String link = FilePublisher.retrieveFile(request, tempFile.getAbsolutePath(), uid + ".xls");

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        response.setContentType("application/vnd.ms-excel"); // Check http://www.w3schools.com/media/media_mimeref.asp for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
        response.setHeader("Content-disposition", "attachment; filename=\"catalog.xls\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.
        response.sendRedirect(link);
    }

    private void putToAll(List<BrandConfirmModel> localBrandList) {
        brandConfirmModelListAll.addAll(localBrandList);
    }

    private void refresh(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        try {
            response.sendRedirect(request.getContextPath() + "/unloadingByBrand/unloadingByBrand.xhtml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       // facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }

    public AnalogsService getAnalogService() {
        return analogService;
    }

    public void setAnalogService(AnalogsService analogService) {
        this.analogService = analogService;
    }

    public List<BrandConfirmModel> getBrandConfirmModelList1() {
        return brandConfirmModelList1;
    }

    public void setBrandConfirmModelList1(List<BrandConfirmModel> brandConfirmModelList1) {
        this.brandConfirmModelList1 = brandConfirmModelList1;
    }

    public List<BrandConfirmModel> getBrandConfirmModelList2() {
        return brandConfirmModelList2;
    }

    public void setBrandConfirmModelList2(List<BrandConfirmModel> brandConfirmModelList2) {
        this.brandConfirmModelList2 = brandConfirmModelList2;
    }

    public List<BrandConfirmModel> getBrandConfirmModelList3() {
        return brandConfirmModelList3;
    }

    public void setBrandConfirmModelList3(List<BrandConfirmModel> brandConfirmModelList3) {
        this.brandConfirmModelList3 = brandConfirmModelList3;
    }

    public List<BrandConfirmModel> getBrandConfirmModelList4() {
        return brandConfirmModelList4;
    }

    public List<BrandConfirmModel> getBrandConfirmModelListAll() {
        return brandConfirmModelListAll;
    }

    public void setBrandConfirmModelListAll(List<BrandConfirmModel> brandConfirmModelListAll) {
        this.brandConfirmModelListAll = brandConfirmModelListAll;
    }

    public boolean isStartedConfirm() {
        return startedConfirm;
    }

    public void setStartedConfirm(boolean startedConfirm) {
        this.startedConfirm = startedConfirm;
    }
}
