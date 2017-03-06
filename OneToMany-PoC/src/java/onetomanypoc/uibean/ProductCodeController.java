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

    // Flags to indicate if child collections are empty
    private boolean isProductListEmpty;

    public ProductCodeController() {
        // Inform the Abstract parent controller of the concrete ProductCode Entity
        super(ProductCode.class);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsProductListEmpty();
    }

    public boolean getIsProductListEmpty() {
        return this.isProductListEmpty;
    }

    private void setIsProductListEmpty() {
        ProductCode selected = this.getSelected();
        if (selected != null) {
            ProductCodeFacade ejbFacade = (ProductCodeFacade) this.getFacade();
            this.isProductListEmpty = ejbFacade.isProductListEmpty(selected);
        } else {
            this.isProductListEmpty = true;
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
