package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Test the inventory class
 *
 * @author Natalie Meuser
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    /**
     * InventoryService, autowired, for manipulating the inventory model
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * Before each test, to reset
     */
    @BeforeEach
    public void setup () {
        inventoryService.deleteAll();
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredient( "Coffee", 500 );
        ivt.addIngredient( "Milk", 500 );
        ivt.addIngredient( "Chocolate", 500 );

        inventoryService.save( ivt );
    }

    /**
     * Test getting the inventory
     */
    @Test
    @Transactional
    public void testGetInventory () {
        final List<Ingredient> inventoryList = inventoryService.getInventory().getInventoryList();

        Assertions.assertEquals( "Coffee", inventoryList.get( 0 ).getName() );
        Assertions.assertEquals( "Milk", inventoryList.get( 1 ).getName() );
        Assertions.assertEquals( "Chocolate", inventoryList.get( 2 ).getName() );

    }

    /**
     * Test setting the inventory
     */
    @Test
    @Transactional
    public void testSetInventory () {
        final List<Ingredient> newInventory = new ArrayList<Ingredient>();
        newInventory.add( new Ingredient( "New Coffee", 200 ) );
        newInventory.add( new Ingredient( "New Milk", 200 ) );

        final List<Ingredient> inventoryList = inventoryService.getInventory().getInventoryList();
        Assertions.assertEquals( 3, inventoryList.size() );
        Assertions.assertEquals( "Coffee", inventoryList.get( 0 ).getName() );
        Assertions.assertEquals( "Milk", inventoryList.get( 1 ).getName() );
        Assertions.assertEquals( "Chocolate", inventoryList.get( 2 ).getName() );

        final Inventory ivt = inventoryService.getInventory();
        ivt.setInventoryList( newInventory );
        inventoryService.save( ivt );

        final List<Ingredient> updatedInventoryList = inventoryService.getInventory().getInventoryList();
        Assertions.assertEquals( 2, updatedInventoryList.size() );
        Assertions.assertEquals( "New Coffee", updatedInventoryList.get( 0 ).getName() );
        Assertions.assertEquals( "New Milk", updatedInventoryList.get( 1 ).getName() );
    }

    /**
     * Test using the inventory
     */
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( "Chocolate", 10 );
        recipe.addIngredient( "Milk", 20 );
        recipe.addIngredient( "Coffee", 1 );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */
        Assertions.assertEquals( 490, i.getInventoryIngredient( "Chocolate" ) );
        Assertions.assertEquals( 480, i.getInventoryIngredient( "Milk" ) );
        Assertions.assertEquals( 499, i.getInventoryIngredient( "Coffee" ) );
    }

    /**
     * Test adding inventory
     */
    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addInventoryIngredient( "Coffee", 5 );
        ivt.addInventoryIngredient( "Milk", 3 );
        ivt.addInventoryIngredient( "Chocolate", 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, ivt.getInventoryIngredient( "Coffee" ),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, ivt.getInventoryIngredient( "Milk" ),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 502, ivt.getInventoryIngredient( "Chocolate" ),
                "Adding to the inventory should result in correctly-updated values sugar" );
    }

    /**
     * Test adding inventory
     */
    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addInventoryIngredient( "Coffee", -5 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Milk" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    /**
     * Test adding inventory
     */
    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addInventoryIngredient( "Milk", -3 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Milk" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }

    }

    /**
     * Test adding invalid inventory
     */
    @Test
    @Transactional
    public void testInvalidAddIngredientDuplicateName () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( "Milk", 5 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Ingredient already exists.", iae.getMessage() );
            Assertions.assertEquals( 3, ivt.getInventoryList().size() );
        }
    }

    /**
     * Test adding invalid inventory
     */
    @Test
    @Transactional
    public void testInvalidAddIngredientInvalidAmount () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( "Sugar", -5 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Amount of ingredient must be positive.", iae.getMessage() );
            Assertions.assertEquals( 3, ivt.getInventoryList().size() );
        }
    }

    /**
     * Test adding invalid inventory
     */
    @Test
    @Transactional
    public void testInvalidAddInventoryIngredientInvalidAmount () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredient( "Milk", -5 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Amount of ingredient must be positive.", iae.getMessage() );
            Assertions.assertEquals( 3, ivt.getInventoryList().size() );
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Milk" ) );
        }
    }

    /**
     * Test adding invalid inventory
     */
    @Test
    @Transactional
    public void testInvalidAddInventoryIngredient () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addInventoryIngredient( "Sugar", 5 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Ingredient does not exist in the inventory.", iae.getMessage() );
            Assertions.assertEquals( 3, ivt.getInventoryList().size() );
        }
    }

    /**
     * Test invalid set inventory
     */
    @Test
    @Transactional
    public void testInvalidSetInventoryIngredientInvalidAmount () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.setInventoryIngredient( "Coffee", -5 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Amount of ingredient must be positive.", iae.getMessage() );
            Assertions.assertEquals( 3, ivt.getInventoryList().size() );
            Assertions.assertEquals( 500, ivt.getInventoryIngredient( "Coffee" ) );
        }
    }

    /**
     * Test invalid set inventory
     */
    @Test
    @Transactional
    public void testInvalidSetInventoryIngredient () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.setInventoryIngredient( "Sugar", 5 );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Ingredient does not exist in the inventory.", iae.getMessage() );
            Assertions.assertEquals( 3, ivt.getInventoryList().size() );
        }
    }

    /**
     * Test invalid set inventory
     */
    @Test
    @Transactional
    public void testInvalidGetInventoryIngredient () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.getInventoryIngredient( "Sugar" );
            Assertions.fail( "Should have thrown exception" );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Ingredient does not exist in the inventory.", iae.getMessage() );
            Assertions.assertEquals( 3, ivt.getInventoryList().size() );
        }
    }

    // @Test
    // @Transactional
    // public void testSetInvalid () {
    // final Inventory ivt = inventoryService.getInventory();
    //
    // Assertions.assertEquals( 500, (int) ivt.getCoffee() );
    // Assertions.assertEquals( 500, (int) ivt.getMilk() );
    // Assertions.assertEquals( 500, (int) ivt.getSugar() );
    // Assertions.assertEquals( 500, (int) ivt.getChocolate() );
    //
    // ivt.setChocolate( -1 );
    // ivt.setCoffee( -1 );
    // ivt.setMilk( -1 );
    // ivt.setSugar( -1 );
    //
    // // ensure the values did not change
    // Assertions.assertEquals( 500, (int) ivt.getCoffee() );
    // Assertions.assertEquals( 500, (int) ivt.getMilk() );
    // Assertions.assertEquals( 500, (int) ivt.getSugar() );
    // Assertions.assertEquals( 500, (int) ivt.getChocolate() );
    // }
    //
    // @Test
    // @Transactional
    // public void testIsEnoughIngredients () {
    // final Inventory ivt = inventoryService.getInventory();
    //
    // ivt.setChocolate( 5 );
    // ivt.setCoffee( 5 );
    // ivt.setMilk( 5 );
    // ivt.setSugar( 5 );
    //
    // // ensure values were updated
    // Assertions.assertEquals( 5, (int) ivt.getCoffee() );
    // Assertions.assertEquals( 5, (int) ivt.getMilk() );
    // Assertions.assertEquals( 5, (int) ivt.getSugar() );
    // Assertions.assertEquals( 5, (int) ivt.getChocolate() );
    //
    // // try using recipe where not enough coffee ingredient
    // final Recipe rCoffee = createRecipe( "rCoffee", 50, 10, 3, 3, 3 );
    // Assertions.assertFalse( ivt.enoughIngredients( rCoffee ) );
    //
    // // try using recipe where not through milk ingredient
    // final Recipe rMilk = createRecipe( "rMilk", 50, 3, 10, 3, 3 );
    // Assertions.assertFalse( ivt.enoughIngredients( rMilk ) );
    //
    // // try using recipe where not through sugar ingredient
    // final Recipe rSugar = createRecipe( "rSugar", 50, 3, 3, 10, 3 );
    // Assertions.assertFalse( ivt.enoughIngredients( rSugar ) );
    //
    // // try using recipe where not through sugar ingredient
    // final Recipe rChocolate = createRecipe( "rChocolate", 50, 3, 3, 3, 10 );
    // Assertions.assertFalse( ivt.enoughIngredients( rChocolate ) );
    // }

    /**
     * Test to string
     */
    @Test
    @Transactional
    public void testToString () {
        final Inventory ivt = inventoryService.getInventory();

        final String finalString = "Coffee: 500\nMilk: 500\nChocolate: 500\n";

        Assertions.assertEquals( finalString, ivt.toString() );
    }

    // private Recipe createRecipe ( final String name, final Integer price,
    // final Integer coffee, final Integer milk,
    // final Integer sugar, final Integer chocolate ) {
    // final Recipe recipe = new Recipe();
    // recipe.setName( name );
    // recipe.setPrice( price );
    // recipe.setCoffee( coffee );
    // recipe.setMilk( milk );
    // recipe.setSugar( sugar );
    // recipe.setChocolate( chocolate );
    //
    // return recipe;
    // }

}
