package onetomanypoc.uibean;

import onetomanypoc.entity.Manufacturer;
import onetomanypoc.datalayer.ManufacturerFacade;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.inject.Inject;

@Named(value = "manufacturerController")
@ViewAccessScoped
public class ManufacturerController extends AbstractController<Manufacturer> {

    @Inject
    private ProductController productListController;

    // Flags to indicate if child collections are empty
    private boolean isProductListEmpty;

    public ManufacturerController() {
        // Inform the Abstract parent controller of the concrete Manufacturer Entity
        super(Manufacturer.class);
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
        Manufacturer selected = this.getSelected();
        if (selected != null) {
            ManufacturerFacade ejbFacade = (ManufacturerFacade) this.getFacade();
            this.isProductListEmpty = ejbFacade.isProductListEmpty(selected);
        } else {
            this.isProductListEmpty = true;
        }
    }

    /**
     * Passes collection of Product entities that are retrieved from
     * Manufacturer?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Product page
     */
    public String navigateProductList() {
        Manufacturer selected = this.getSelected();
        if (selected != null) {
            ManufacturerFacade ejbFacade = (ManufacturerFacade) this.getFacade();
            productListController.setItems(ejbFacade.findProductList(selected));
            productListController.setLazyItems(ejbFacade.findProductList(selected));
        }
        return "/app/product/index?faces-redirect=true";
    }

}
