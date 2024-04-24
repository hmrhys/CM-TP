package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 *
 * The APICoffeeController is responsible for making coffee when a user submits
 * a request to do so.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICoffeeController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService   inventoryService;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService      recipeService;

    /**
     * CoffeeOrderService object, to be autowired in by Spring to allow for
     * maniupulating the CoffeeOrder model
     */
    @Autowired
    private CoffeeOrderService coffeeOrderService;

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the UserService model
     */
    @Autowired
    private UserService        userService;

    /**
     * REST API method to make coffee by completing a POST request with the ID
     * of the order as the path variable
     *
     * @param id
     *            order id
     * @param staff
     *            who fulfilled the order
     * @return stating the order was successful or not successful
     */
    @PostMapping ( BASE_PATH + "/makecoffee/{number}" )
    public ResponseEntity makeCoffee ( @PathVariable ( "number" ) final Long id,
            @RequestBody final String staffUsername ) {
        final CoffeeOrder order = coffeeOrderService.findById( id );
        final Recipe recipe = recipeService.findByName( order.getRecipeOrdered().getName() );
        final Staff staffMember = (Staff) userService.findByUsername( staffUsername );
        if ( recipe == null ) {
            return new ResponseEntity( errorResponse( "No recipe selected." ), HttpStatus.NOT_FOUND );
        }

        final boolean makeCoffee = makeCoffeeHelper( recipe );
        if ( !makeCoffee ) {
            return new ResponseEntity( errorResponse( "Not enough inventory." ), HttpStatus.CONFLICT );
        }
        order.fulfilledOrder( staffMember );
        coffeeOrderService.save( order );
        return new ResponseEntity<String>( successResponse( recipe.getName() + " made." ), HttpStatus.OK );

    }

    /**
     * REST API method to order coffee by completing a POST request with the
     * name of the recipe as the path variable and the amount that has been paid
     * as the body of the response
     *
     * @param name
     *            recipe name
     * @param amtPaid
     *            amount paid
     * @return The change the customer is due if successful
     */
    @PostMapping ( BASE_PATH + "/orderCoffee/{name}" )
    public ResponseEntity orderCoffee ( @PathVariable ( "name" ) final String name, @RequestBody final int amtPaid ) {
        final Recipe recipe = recipeService.findByName( name );
        if ( recipe == null ) {
            return new ResponseEntity( errorResponse( "No recipe selected." ), HttpStatus.NOT_FOUND );
        }

        final int change = orderCoffeeHelper( recipe, amtPaid );
        if ( change == amtPaid ) {
            return new ResponseEntity( errorResponse( "Not enough money paid." ), HttpStatus.CONFLICT );
        }
        return new ResponseEntity<String>( successResponse( String.valueOf( change ) ), HttpStatus.OK );
    }

    /**
     * REST API method to pick up an order by completing a POST request with the
     * ID of the order as the path variable
     *
     * @param id
     *            order id
     * @return The change the customer is due if successful
     */
    @PostMapping ( BASE_PATH + "/pickUpOrder/{id}" )
    public ResponseEntity pickUpOrder ( @PathVariable ( "id" ) final Long id ) {
        final CoffeeOrder order = coffeeOrderService.findById( id );
        if ( order == null ) {
            return new ResponseEntity( errorResponse( "No order selected." ), HttpStatus.NOT_FOUND );
        }

        order.pickUpOrder();
        coffeeOrderService.save( order );
        return new ResponseEntity<String>( successResponse( "Order picked up. Enjoy!" ), HttpStatus.OK );
    }

    /**
     * Helper method to make coffee
     *
     * @param toPurchase
     *            recipe that we want to make
     * @return change if there was enough money to make the coffee, throws
     *         exceptions if not
     */
    public boolean makeCoffeeHelper ( final Recipe toPurchase ) {
        final Inventory inventory = inventoryService.getInventory();

        if ( toPurchase == null ) {
            throw new IllegalArgumentException( "Recipe not found" );
        }
        if ( inventory.useIngredients( toPurchase ) ) {
            inventoryService.save( inventory );
            return true;
        }
        // not enough inventory
        return false;
    }

    /**
     * Helper method to order coffee
     *
     * @param toPurchase
     *            recipe that we want to make
     * @param amtPaid
     *            money that the user has given the machine
     * @return change if there was enough money to make the coffee, throws
     *         exceptions if not
     */
    public int orderCoffeeHelper ( final Recipe toPurchase, final int amtPaid ) {
        int change = amtPaid;

        if ( toPurchase == null ) {
            throw new IllegalArgumentException( "Recipe not found." );
        }
        else if ( toPurchase.getPrice() <= amtPaid ) {
            change = amtPaid - toPurchase.getPrice();
            return change;
        }
        // not enough money
        return change;
    }
}
