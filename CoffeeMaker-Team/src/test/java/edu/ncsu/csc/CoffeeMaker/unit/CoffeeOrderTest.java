package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the CoffeeOrder class
 *
 * @author Natalie Meuser
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CoffeeOrderTest {

    /**
     * RecipeService, autowired, for manipulating the Recipe model
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Test creating a valid order
     */
    @Test
    @Transactional
    public void testCreateValidOrder () {
        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username2", "pw2" );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );

        Assertions.assertEquals( "Coffee", valid.getRecipeOrdered().getName() );
        Assertions.assertEquals( "username2", valid.getCustomer().getUsername() );
    }

    /**
     * Test creating an invalid order since a null customer
     */
    @Test
    @Transactional
    public void testCreateInvalidOrderCustomer () {
        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = null;

        try {
            new CoffeeOrder( r2, c2 );
            Assertions.fail( "Order should not have been created." );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Customer must not be null.", iae.getMessage() );
        }
    }

    /**
     * Test creating an invalid order because of a null recipe
     */
    @Test
    @Transactional
    public void testCreateInvalidOrderRecipe () {
        final Recipe r2 = null;
        final Customer c2 = new Customer( "username2", "pw2" );

        try {
            new CoffeeOrder( r2, c2 );
            Assertions.fail( "Order should not have been created." );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Recipe must not be null.", iae.getMessage() );
        }
    }

    /**
     * Test fulfilling and picking up an order
     */
    @Test
    @Transactional
    public void testFulfillPickUpOrder () {
        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username2", "pw2" );
        final Staff s2 = new Staff( "usernameS2", "pwS2" );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );

        Assertions.assertFalse( valid.getStatus() );
        Assertions.assertFalse( valid.getPickUpStatus() );
        Assertions.assertNull( valid.getStaff() );

        try {
            valid.pickUpOrder();
            Assertions.fail( "Order must be ready before it can be picked up." );
        }
        catch ( final IllegalArgumentException e ) {
            Assertions.assertEquals( "Order must be ready before it can be picked up.", e.getMessage() );
        }

        valid.fulfilledOrder( s2 );

        Assertions.assertTrue( valid.getStatus() );
        Assertions.assertFalse( valid.getPickUpStatus() );
        Assertions.assertEquals( s2.getUsername(), valid.getStaff().getUsername() );

        valid.pickUpOrder();

        Assertions.assertTrue( valid.getStatus() );
        Assertions.assertTrue( valid.getPickUpStatus() );
        Assertions.assertEquals( s2.getUsername(), valid.getStaff().getUsername() );

    }

    /**
     * Test to string
     */
    @Test
    @Transactional
    public void testToString () {
        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username2", "pw2" );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );
        Assertions.assertEquals( "Order of Coffee", valid.toString() );
    }

    /**
     * Test to info string
     */
    @Test
    @Transactional
    public void testToInfoString () {
        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username2", "pw2" );
        final Staff s2 = new Staff( "usernameS2", "pwS2" );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );

        final String resultString = "Order of Coffee, placed by username2. Not yet fulfilled.\n";
        Assertions.assertEquals( resultString, valid.toInfoString() );

        valid.fulfilledOrder( s2 );

        final String resultFulfilledString = "Order of Coffee, placed by username2. Fulfilled by usernameS2; Not picked up.\n";
        Assertions.assertEquals( resultFulfilledString, valid.toInfoString() );

        valid.pickUpOrder();

        final String resultPickedUpString = "Order of Coffee, placed by username2. Fulfilled by usernameS2; Picked up.\n";
        Assertions.assertEquals( resultPickedUpString, valid.toInfoString() );
    }

    /**
     * Test to customer string
     */
    @Test
    @Transactional
    public void testToCustomerString () {
        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username2", "pw2" );
        final Staff s2 = new Staff( "usernameS2", "pwS2" );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );

        final String resultString = "Order of Coffee. Not yet fulfilled.\n";
        Assertions.assertEquals( resultString, valid.toCustomerString() );

        valid.fulfilledOrder( s2 );

        final String resultFulfilledString = "Order of Coffee. Fulfilled; Not picked up.\n";
        Assertions.assertEquals( resultFulfilledString, valid.toCustomerString() );

        valid.pickUpOrder();

        final String resultPickedUpString = "Order of Coffee. Fulfilled; Picked up.\n";
        Assertions.assertEquals( resultPickedUpString, valid.toCustomerString() );
    }

}
