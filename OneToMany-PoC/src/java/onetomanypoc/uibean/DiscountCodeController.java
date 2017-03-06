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

    // Flags to indicate if child collections are empty
    private boolean isCustomerListEmpty;

    public DiscountCodeController() {
        // Inform the Abstract parent controller of the concrete DiscountCode Entity
        super(DiscountCode.class);
    }

    /**
     * Set the "is[ChildCollection]Empty" property for OneToMany fields.
     */
    @Override
    protected void setChildrenEmptyFlags() {
        this.setIsCustomerListEmpty();
    }

    public boolean getIsCustomerListEmpty() {
        return this.isCustomerListEmpty;
    }

    private void setIsCustomerListEmpty() {
        DiscountCode selected = this.getSelected();
        if (selected != null) {
            DiscountCodeFacade ejbFacade = (DiscountCodeFacade) this.getFacade();
            this.isCustomerListEmpty = ejbFacade.isCustomerListEmpty(selected);
        } else {
            this.isCustomerListEmpty = true;
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
