package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Inventory.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Natalie Meuser
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICoffeeOrderController extends APIController {

    /**
     * CoffeeOrderService object, to be autowired in by Spring to allow for
     * manipulating the CoffeeOrder model
     */
    @Autowired
    private CoffeeOrderService service;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService      recipeService;

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService        userService;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Coffee order database. This will convert the CoffeeOrder to a list.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ordersAll" )
    public List<CoffeeOrder> getOrders () {
        final List<CoffeeOrder> orderDatabase = service.findAll();
        return orderDatabase;
    }

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Coffee order database. This will convert the Coffee Orders into their
     * string for display.
     *
     * @return a list of order database strings
     */
    @GetMapping ( BASE_PATH + "/orderAllString" )
    public List<String> getOrdersStrings () {
        final List<CoffeeOrder> orderDatabase = service.findAll();
        final List<String> orderDatabaseStrings = new ArrayList<String>();
        for ( final CoffeeOrder order : orderDatabase ) {
            orderDatabaseStrings.add( order.toInfoString() );
        }
        return orderDatabaseStrings;
    }

    // /**
    // * REST API endpoint to provide GET access to the CoffeeMaker's coffee
    // order
    // * database for a specific customer
    // */
    // @GetMapping ( BASE_PATH + "/ordersAll/{customer}" )
    // public List<CoffeeOrder> getOrdersAllCustomer ( @PathVariable (
    // "customer" ) final String username ) {
    // final Customer customer = (Customer) userService.findByUsername( username
    // );
    // final List<CoffeeOrder> allOrders = service.findAll();
    // final List<CoffeeOrder> coffeeOrdersCustomer = new
    // ArrayList<CoffeeOrder>();
    // for ( final CoffeeOrder order : allOrders ) {
    // if ( order.getCustomer().getUsername().equals( customer.getUsername() ) )
    // {
    // coffeeOrdersCustomer.add( order );
    // }
    // }
    // return coffeeOrdersCustomer;
    // }

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Coffee order database. This will convert the Coffee Orders into their
     * string for display.
     *
     * @param username
     *            of customer to get the order strings of
     *
     * @return a list of customer order strings
     */
    @GetMapping ( BASE_PATH + "/orderAllString/{customer}" )
    public List<String> getCustomerOrdersStrings ( @PathVariable ( "customer" ) final String username ) {
        final Customer customer = (Customer) userService.findByUsername( username );
        final List<CoffeeOrder> orderDatabase = service.findAll();
        final List<String> coffeeOrdersStringsCustomer = new ArrayList<String>();
        for ( final CoffeeOrder order : orderDatabase ) {
            if ( order.getCustomer().getUsername().equals( customer.getUsername() ) ) {
                coffeeOrdersStringsCustomer.add( order.toCustomerString() );
            }
            // if ( order.getCustomer().getUsername().equals( username ) ) {
            // coffeeOrdersStringsCustomer.add( order.toCustomerString() );
            // }
        }
        return coffeeOrdersStringsCustomer;
    }

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's coffee order
     * data of not completed orders
     *
     * @return response to request
     *
     */
    @GetMapping ( BASE_PATH + "/ordersNotCompleted" )
    public List<CoffeeOrder> getOrdersNotCompleted () {
        final List<CoffeeOrder> allOrders = service.findAll();
        final List<CoffeeOrder> notCompletedOrders = new ArrayList<CoffeeOrder>();
        for ( final CoffeeOrder order : allOrders ) {
            if ( !order.getStatus() ) {
                notCompletedOrders.add( order );
            }
        }
        return notCompletedOrders;
    }

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's coffee order
     * data of not completed orders
     *
     * @param username
     *            of the customer to get the orders not picked up yet for
     *
     * @return response to request
     *
     */
    @GetMapping ( BASE_PATH + "/ordersNotPickedUp/{customer}" )
    public List<CoffeeOrder> getOrdersNotPickedUp ( @PathVariable ( "customer" ) final String username ) {
        final List<CoffeeOrder> allOrders = service.findAll();
        final List<CoffeeOrder> notCompletedCustomerCoffeeOrders = new ArrayList<CoffeeOrder>();
        for ( final CoffeeOrder order : allOrders ) {
            if ( !order.getPickUpStatus() && order.getCustomer().getUsername().equals( username ) ) {
                notCompletedCustomerCoffeeOrders.add( order );
            }
        }
        return notCompletedCustomerCoffeeOrders;
    }

    // /**
    // * REST API endpoint to provide GET access to the CoffeeMaker's coffee
    // order
    // * data of not completed orders
    // *
    // * @return response to request
    // *
    // */
    // @GetMapping ( BASE_PATH + "/ordersCompleted" )
    // public List<CoffeeOrder> ordersCompleted () {
    // final List<CoffeeOrder> allOrders = service.findAll();
    // final List<CoffeeOrder> completedOrders = new ArrayList<CoffeeOrder>();
    // for ( final CoffeeOrder order : allOrders ) {
    // if ( order.getStatus() ) {
    // completedOrders.add( order );
    // }
    // }
    // return completedOrders;
    // }

    /**
     * REST API endpoint to add an ingredient to the inventory.
     *
     * @param order
     *            to add to database
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/orderAdd" )
    public ResponseEntity addToOrderDatabase ( @RequestBody final CoffeeOrder order ) {
        final Recipe recipe = recipeService.findByName( order.getRecipeOrdered().getName() );
        Customer customer = (Customer) userService.findByUsername( order.getCustomer().getUsername() );
        if ( customer == null ) {
            customer = (Customer) userService.findByUsername( "guest" );
        }

        final CoffeeOrder orderPlaced = new CoffeeOrder( recipe, customer );

        // final CoffeeOrder orderPlaced = new CoffeeOrder( recipe,
        // order.getCustomer() );

        service.save( orderPlaced );

        return new ResponseEntity( successResponse( orderPlaced.toString() + " successfully created." ),
                HttpStatus.OK );

    }

    /**
     * REST API method to provide GET access to a specific coffee order, as
     * indicated by the path variable provided (the id of the order desired)
     *
     * @param id
     *            order id
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/order/{id}" )
    public ResponseEntity getCoffeeOrder ( @PathVariable ( "id" ) final Long id ) {
        final CoffeeOrder order = service.findById( id );
        return null == order ? new ResponseEntity( errorResponse( "No order found." ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( order, HttpStatus.OK );
    }

    // /**
    // * REST API method to allow deleting a CoffeeOrder from the CoffeeMaker's
    // * Inventory, by making a DELETE request to the API endpoint and
    // indicating
    // * the order to delete (as a path variable)
    // *
    // * @param id
    // * The id of the order to delete
    // * @return Success if the order could be deleted; an error if the order
    // does
    // * not exist
    // */
    // @DeleteMapping ( BASE_PATH + "/order/{id}" )
    // public ResponseEntity deleteOrder ( @PathVariable ( "id" ) final Long id
    // ) {
    // final CoffeeOrder order = service.findById( id );
    // if ( null == order ) {
    // return new ResponseEntity( errorResponse( "No order found" ),
    // HttpStatus.NOT_FOUND );
    // }
    // service.delete( order );
    //
    // return new ResponseEntity( successResponse( order.toString() + " was
    // deleted successfully." ), HttpStatus.OK );
    // }
}
