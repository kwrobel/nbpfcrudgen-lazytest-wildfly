package onetomanypoc.uibean;

import onetomanypoc.entity.MicroMarket;
import onetomanypoc.datalayer.MicroMarketFacade;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import javax.inject.Inject;

@Named(value = "microMarketController")
@ViewAccessScoped
public class MicroMarketController extends AbstractController<MicroMarket> {

    @Inject
    private CustomerController customerListController;

    // Flags to indicate if child collections are empty
    private boolean isCustomerListEmpty;

    public MicroMarketController() {
        // Inform the Abstract parent controller of the concrete MicroMarket Entity
        super(MicroMarket.class);
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
        MicroMarket selected = this.getSelected();
        if (selected != null) {
            MicroMarketFacade ejbFacade = (MicroMarketFacade) this.getFacade();
            this.isCustomerListEmpty = ejbFacade.isCustomerListEmpty(selected);
        } else {
            this.isCustomerListEmpty = true;
        }
    }

    /**
     * Passes collection of Customer entities that are retrieved from
     * MicroMarket?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Customer page
     */
    public String navigateCustomerList() {
        MicroMarket selected = this.getSelected();
        if (selected != null) {
            MicroMarketFacade ejbFacade = (MicroMarketFacade) this.getFacade();
            customerListController.setItems(ejbFacade.findCustomerList(selected));
            customerListController.setLazyItems(ejbFacade.findCustomerList(selected));
        }
        return "/app/customer/index?faces-redirect=true";
    }

}
