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

    public MicroMarketController() {
        // Inform the Abstract parent controller of the concrete MicroMarket Entity
        super(MicroMarket.class);
    }

    public boolean getIsCustomerListEmpty() {
        MicroMarketFacade ejbFacade = (MicroMarketFacade) this.getFacade();
        MicroMarket entity = this.getSelected();
        if (entity != null) {
            return ejbFacade.isCustomerListEmpty(entity);
        } else {
            return false;
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
