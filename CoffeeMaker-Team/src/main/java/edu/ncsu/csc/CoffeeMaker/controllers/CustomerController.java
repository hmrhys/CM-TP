package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Controller to manage basic abilities for Customer roles
 *
 * Modified from iTrust2
 *
 * @author Kai Presler-Marshall
 */

@Controller
public class CustomerController {

    /**
     * Returns the customer for the given model
     *
     * @param model
     *            model to check
     * @return role
     */
    @RequestMapping ( value = { "/customerindex.html", "/customerindex" } )
    @PreAuthorize ( "hasRole('CUSTOMER')" )
    public String index ( final Model model ) {
        return Role.CUSTOMER.getLanding();
    }

    /**
     * View order status
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/customer-orderstatus.html", "/customer-orderstatus" } )
    @PreAuthorize ( "hasRole('CUSTOMER')" )
    public String viewOrderStatus ( final Model model ) {
        return "customer-orderstatus";
    }

    /**
     * View all orders
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/customer-vieworders.html", "/customer-vieworders" } )
    @PreAuthorize ( "hasRole('CUSTOMER')" )
    public String viewOrders ( final Model model ) {
        return "customer-vieworders";
    }

    /**
     * Place an order
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/customer-placeorder.html", "/customer-placeorder" } )
    @PreAuthorize ( "hasRole('CUSTOMER')" )
    public String placeOrder ( final Model model ) {
        return "customer-placeorder";
    }

}
