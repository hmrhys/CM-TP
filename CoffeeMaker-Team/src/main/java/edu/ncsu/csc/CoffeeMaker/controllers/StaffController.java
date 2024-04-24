package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Controller to manage basic abilities for Staff roles
 *
 * Modified from iTrust2
 *
 * @author Kai Presler-Marshall
 */

@Controller
public class StaffController {
    /**
     * Returns the staff for the given model
     *
     * @param model
     *            model to check
     * @return role
     */
    @RequestMapping ( value = { "/staff-index.html", "/staff-index" } )
    @PreAuthorize ( "hasRole('STAFF') and !hasRole('CUSTOMER')" )
    public String index ( final Model model ) {
        return Role.STAFF.getLanding();
    }

    /**
     * Add ingredients
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/staff-addingredients.html", "/staff-addingredients" } )
    @PreAuthorize ( "hasRole('STAFF')" )
    public String addIngredients ( final Model model ) {
        return "staff-addingredients";
    }

    /**
     * Add inventory
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/staff-addinventory.html", "/staff-addinventory" } )
    @PreAuthorize ( "hasRole('STAFF')" )
    public String addInventory ( final Model model ) {
        return "staff-addinventory";
    }

    /**
     * Add a recipe
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/staff-addrecipe.html", "/staff-addrecipe" } )
    @PreAuthorize ( "hasRole('STAFF')" )
    public String addRecipe ( final Model model ) {
        return "staff-addrecipe";
    }

    /**
     * Edit a recipe
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/staff-editrecipe.html", "/staff-editrecipe" } )
    @PreAuthorize ( "hasRole('STAFF')" )
    public String editRecipe ( final Model model ) {
        return "staff-editrecipe";
    }

    /**
     * Delete a recipe
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/staff-deleterecipe.html", "/staff-deleterecipe" } )
    @PreAuthorize ( "hasRole('STAFF')" )
    public String deleteRecipe ( final Model model ) {
        return "staff-deleterecipe";
    }

    /**
     * View and fulfill orders
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/staff-fulfillorders.html", "/staff-fulfillorders" } )
    @PreAuthorize ( "hasRole('STAFF')" )
    public String fulfillOrders ( final Model model ) {
        return "staff-fulfillorders";
    }

    /**
     * View all orders
     *
     * @param model
     *            data for front end
     * @return mapping
     */
    @RequestMapping ( value = { "/staff-allorders.html", "/staff-allorders" } )
    @PreAuthorize ( "hasRole('STAFF')" )
    public String allOrders ( final Model model ) {
        return "staff-allorders";
    }

}
