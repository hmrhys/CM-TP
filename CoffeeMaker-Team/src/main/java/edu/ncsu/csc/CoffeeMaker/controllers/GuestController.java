package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Controller to manage basic abilities for Guest roles
 *
 * Modified from iTrust2
 *
 * @author Kai Presler-Marshall
 */

@Controller
public class GuestController {

    /**
     * Returns the guest for the given model
     *
     * @param model
     *            model to check
     * @return role
     */
    @RequestMapping ( value = { "/guestindex.html", "/guestindex" } )
    @PreAuthorize ( "hasRole('GUEST')" )
    public String index ( final Model model ) {
        return Role.GUEST.getLanding();
    }

    // COME BACK TO

    /**
     * View order status
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/guest-orderstatus.html",
            "/guest-orderstatus" } )
    @PreAuthorize ( "hasRole('GUEST')" )
    public String viewOrderStatus ( final Model model ) {
        return "guest-orderstatus";
    }

    /**
     * View all orders
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/guest-vieworders.html", "/guest-vieworders" } )
    @PreAuthorize ( "hasRole('GUEST')" )
    public String viewOrders ( final Model model ) {
        return "guest-vieworders";
    }

    /**
     * Place an order
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/guest-placeorder.html", "/guest-placeorder" } )
    @PreAuthorize ( "hasRole('GUEST') and !hasRole('CUSTOMER')" )
    public String placeOrder ( final Model model ) {
        return "guest-placeorder";
    }

}
