package onetomanypoc.uibean;

import onetomanypoc.entity.Customer;
import onetomanypoc.datalayer.CustomerFacade;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "customerController")
@ViewAccessScoped
public class CustomerController extends AbstractController<Customer> {

    @Inject
    private PurchaseOrderController purchaseOrderListController;
    @Inject
    private DiscountCodeController discountCodeController;
    @Inject
    private MicroMarketController zipController;

    // Flags to indicate if child collections are empty
    private boolean isPurchaseOrderListEmpty;

    public CustomerController() {
        // Inform the Abstract parent controller of the concrete Customer Entity
        super(Customer.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        discountCodeController.setSelected(null);
        zipController.setSelected(null);
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
        Customer selected = this.getSelected();
        if (selected != null) {
            CustomerFacade ejbFacade = (CustomerFacade) this.getFacade();
            this.isPurchaseOrderListEmpty = ejbFacade.isPurchaseOrderListEmpty(selected);
        } else {
            this.isPurchaseOrderListEmpty = true;
        }
    }

    /**
     * Passes collection of PurchaseOrder entities that are retrieved from
     * Customer?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for PurchaseOrder page
     */
    public String navigatePurchaseOrderList() {
        Customer selected = this.getSelected();
        if (selected != null) {
            CustomerFacade ejbFacade = (CustomerFacade) this.getFacade();
            purchaseOrderListController.setItems(ejbFacade.findPurchaseOrderList(selected));
            purchaseOrderListController.setLazyItems(ejbFacade.findPurchaseOrderList(selected));
        }
        return "/app/purchaseOrder/index?faces-redirect=true";
    }

    /**
     * Sets the "selected" attribute of the DiscountCode controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDiscountCode(ActionEvent event) {
        Customer selected = this.getSelected();
        if (selected != null && discountCodeController.getSelected() == null) {
            discountCodeController.setSelected(selected.getDiscountCode());
        }
    }

    /**
     * Sets the "selected" attribute of the MicroMarket controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareZip(ActionEvent event) {
        Customer selected = this.getSelected();
        if (selected != null && zipController.getSelected() == null) {
            zipController.setSelected(selected.getZip());
        }
    }

}
