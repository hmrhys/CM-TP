package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index" } )
    public String index ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/recipe", "/recipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "recipe";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addinventory", "/addinventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "addinventory";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }

    /**
     * On a GET request to /addrecipe, the MakeCoffeeController will return
     * /src/main/resources/templates/addrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addrecipe", "/addrecipe.html" } )
    public String addRecipeForm ( final Model model ) {
        return "addrecipe";
    }

    /**
     * On a GET request to /addingredients, the MakeCoffeeController will return
     * /src/main/resources/templates/addingredients.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addingredients", "/addingredients.html" } )
    public String addIngredientsForm ( final Model model ) {
        return "addingredients";
    }

    /**
     * On a GET request to /login, the MakeCoffeeController will return
     * /src/main/resources/templates/login.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/login", "/login.html", "/" } )
    public String loginForm ( final Model model ) {
        return "login";
    }

    /**
     * On a GET request to /stafflogin, the MakeCoffeeController will return
     * /src/main/resources/templates/stafflogin.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/stafflogin", "/stafflogin.html" } )
    public String staffLoginForm ( final Model model ) {
        return "stafflogin";
    }

    /**
     * On a GET request to /customerlogin, the MakeCoffeeController will return
     * /src/main/resources/templates/customerlogin.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customerlogin", "/customerlogin.html" } )
    public String customerLoginForm ( final Model model ) {
        return "customerlogin";
    }

    /**
     * On a GET request to /createaccount, the MakeCoffeeController will return
     * /src/main/resources/templates/createaccount.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/createaccount", "/createaccount.html" } )
    public String createAccountForm ( final Model model ) {
        return "createaccount";
    }

    /**
     * On a GET request to /customerindex, the MakeCoffeeController will return
     * /src/main/resources/templates/customerindex.html.
     *
     * @param model
     *            underlying UI model
     * @param authentication
     *            the authentication object representing the user's security
     *            context
     * @return contents of the page
     */
    @GetMapping ( { "/customerindex", "/customerindex.html" } )
    public String customerIndexForm ( final Model model, final Authentication authentication ) {
        // Get the username
        final String username = authentication.getName();
        model.addAttribute( "username", username );
        return "customerindex";
    }

    /**
     * On a GET request to /staffindex, the MakeCoffeeController will return
     * /src/main/resources/templates/staffindex.html.
     *
     * @param model
     *            underlying UI model
     * @param authentication
     *            the authentication object representing the user's security
     *            context
     * @return contents of the page
     */
    @GetMapping ( { "/staffindex", "/staffindex.html" } )
    @PreAuthorize ( "hasRole('STAFF') and !hasRole('CUSTOMER')" )
    public String staffIndexForm ( final Model model, final Authentication authentication ) {
        // Get the username
        final String username = authentication.getName();
        model.addAttribute( "username", username );
        return "staffindex";
    }

    /**
     * On a GET request to /debug, the MakeCoffeeController will return
     * /src/main/resources/templates/debug.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/debug", "/debug.html" } )
    public String debugForm ( final Model model ) {
        return "debug";
    }

    /**
     * On a GET request to /addingredients, the MakeCoffeeController will return
     * /src/main/resources/templates/addingredients.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/workshop", "/workshop.html" } )
    public String workshopForm ( final Model model ) {
        return "workshop";
    }

    /**
     * On a GET request to /privacypolicyhome, the IndexController will return
     * /src/main/resources/templates/privacypolicyhome.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/privacypolicyhome", "/privacypolicyhome.html" } )
    public String privacypolicyhomeForm ( final Model model ) {
        return "privacypolicyhome";
    }

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "/index.html" } )
    public String indexForm ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /privacypolicy-customer, the IndexController will
     * return /src/main/resources/templates/privacypolicy-customer.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/privacypolicy-customer", "/privacypolicy-customer.html" } )
    public String privacypolicycustomerForm ( final Model model ) {
        return "privacypolicy-customer";
    }

    /**
     * On a GET request to /privacypolicy-staff, the IndexController will return
     * /src/main/resources/templates/privacypolicy-staff.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/privacypolicy-staff", "/privacypolicy-staff.html" } )
    public String privacypolicystaffForm ( final Model model ) {
        return "privacypolicy-staff";
    }

    /**
     * On a GET request to /privacypolicy-guest, the IndexController will return
     * /src/main/resources/templates/privacypolicy-guest.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/privacypolicy-guest", "/privacypolicy-guest.html" } )
    public String privacypolicyguestForm ( final Model model ) {
        return "privacypolicy-guest";
    }

    /**
     * On a GET request to /guestindex, the IndexController will return
     * /src/main/resources/templates/guestindex.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/guestindex", "/guestindex.html" } )
    public String guestindexForm ( final Model model ) {
        return "guestindex";
    }

    /**
     * On a GET request to /guest-placeorder, the IndexController will return
     * /src/main/resources/templates/guest-placeorder.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/guest-placeorder", "/guest-placeorder.html" } )
    public String guestplaceorderForm ( final Model model ) {
        return "guest-placeorder";
    }

}
