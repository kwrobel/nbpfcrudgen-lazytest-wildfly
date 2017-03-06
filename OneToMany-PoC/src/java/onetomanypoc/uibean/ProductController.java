package onetomanypoc.uibean;

import onetomanypoc.entity.Product;
import onetomanypoc.datalayer.ProductFacade;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "productController")
@ViewAccessScoped
public class ProductController extends AbstractController<Product> {

    @Inject
    private PurchaseOrderController purchaseOrderListController;
    @Inject
    private ManufacturerController manufacturerIdController;
    @Inject
    private ProductCodeController productCodeController;

    // Flags to indicate if child collections are empty
    private boolean isPurchaseOrderListEmpty;

    public ProductController() {
        // Inform the Abstract parent controller of the concrete Product Entity
        super(Product.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        manufacturerIdController.setSelected(null);
        productCodeController.setSelected(null);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsPurchaseOrderListEmpty();
    }

    public boolean getIsPurchaseOrderListEmpty() {
        return this.isPurchaseOrderListEmpty;
    }

    private void setIsPurchaseOrderListEmpty() {
        Product selected = this.getSelected();
        if (selected != null) {
            ProductFacade ejbFacade = (ProductFacade) this.getFacade();
            this.isPurchaseOrderListEmpty = ejbFacade.isPurchaseOrderListEmpty(selected);
        } else {
            this.isPurchaseOrderListEmpty = true;
        }
    }

    /**
     * Passes collection of PurchaseOrder entities that are retrieved from
     * Product?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for PurchaseOrder page
     */
    public String navigatePurchaseOrderList() {
        Product selected = this.getSelected();
        if (selected != null) {
            ProductFacade ejbFacade = (ProductFacade) this.getFacade();
            purchaseOrderListController.setItems(ejbFacade.findPurchaseOrderList(selected));
            purchaseOrderListController.setLazyItems(ejbFacade.findPurchaseOrderList(selected));
        }
        return "/app/purchaseOrder/index?faces-redirect=true";
    }

    /**
     * Sets the "selected" attribute of the Manufacturer controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareManufacturerId(ActionEvent event) {
        Product selected = this.getSelected();
        if (selected != null && manufacturerIdController.getSelected() == null) {
            manufacturerIdController.setSelected(selected.getManufacturerId());
        }
    }

    /**
     * Sets the "selected" attribute of the ProductCode controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProductCode(ActionEvent event) {
        Product selected = this.getSelected();
        if (selected != null && productCodeController.getSelected() == null) {
            productCodeController.setSelected(selected.getProductCode());
        }
    }

}
