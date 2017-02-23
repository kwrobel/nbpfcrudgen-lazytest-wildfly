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

    public boolean getIsPurchaseOrderListEmpty() {
        CustomerFacade ejbFacade = (CustomerFacade) this.getFacade();
        Customer entity = this.getSelected();
        if (entity != null) {
            return ejbFacade.isPurchaseOrderListEmpty(entity);
        } else {
            return false;
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
        if (this.getSelected() != null && discountCodeController.getSelected() == null) {
            discountCodeController.setSelected(this.getSelected().getDiscountCode());
        }
    }

    /**
     * Sets the "selected" attribute of the MicroMarket controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareZip(ActionEvent event) {
        if (this.getSelected() != null && zipController.getSelected() == null) {
            zipController.setSelected(this.getSelected().getZip());
        }
    }
}
