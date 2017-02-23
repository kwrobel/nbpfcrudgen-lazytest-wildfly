package onetomanypoc.uibean;

import onetomanypoc.entity.ProductCode;
import onetomanypoc.datalayer.ProductCodeFacade;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.inject.Inject;

@Named(value = "productCodeController")
@ViewAccessScoped
public class ProductCodeController extends AbstractController<ProductCode> {

    @Inject
    private ProductController productListController;

    public ProductCodeController() {
        // Inform the Abstract parent controller of the concrete ProductCode Entity
        super(ProductCode.class);
    }

    public boolean getIsProductListEmpty() {
        ProductCodeFacade ejbFacade = (ProductCodeFacade) this.getFacade();
        ProductCode entity = this.getSelected();
        if (entity != null) {
            return ejbFacade.isProductListEmpty(entity);
        } else {
            return false;
        }
    }

    /**
     * Passes collection of Product entities that are retrieved from
     * ProductCode?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Product page
     */
    public String navigateProductList() {
        ProductCode selected = this.getSelected();

        if (selected != null) {
            ProductCodeFacade ejbFacade = (ProductCodeFacade) this.getFacade();
            productListController.setItems(ejbFacade.findProductList(selected));
            productListController.setLazyItems(ejbFacade.findProductList(selected));
        }
        return "/app/product/index?faces-redirect=true";
    }

}
