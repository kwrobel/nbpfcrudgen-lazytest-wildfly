package onetomanypoc.uibean;

import onetomanypoc.entity.DiscountCode;
import onetomanypoc.datalayer.DiscountCodeFacade;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.inject.Inject;

@Named(value = "discountCodeController")
@ViewAccessScoped
public class DiscountCodeController extends AbstractController<DiscountCode> {

    @Inject
    private CustomerController customerListController;

    public DiscountCodeController() {
        // Inform the Abstract parent controller of the concrete DiscountCode Entity
        super(DiscountCode.class);
    }

    public boolean getIsCustomerListEmpty() {
        DiscountCodeFacade ejbFacade = (DiscountCodeFacade) this.getFacade();
        DiscountCode entity = this.getSelected();
        if (entity != null) {
            return ejbFacade.isCustomerListEmpty(entity);
        } else {
            return false;
        }
    }

    /**
     * Passes collection of Customer entities that are retrieved from
     * DiscountCode?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Customer page
     */
    public String navigateCustomerList() {
        DiscountCode selected = this.getSelected();

        if (selected != null) {
            DiscountCodeFacade ejbFacade = (DiscountCodeFacade) this.getFacade();
            customerListController.setItems(ejbFacade.findCustomerList(selected));
            customerListController.setLazyItems(ejbFacade.findCustomerList(selected));
        }
        return "/app/customer/index?faces-redirect=true";
    }

}
