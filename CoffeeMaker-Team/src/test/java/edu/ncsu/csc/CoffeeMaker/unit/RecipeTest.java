package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the Recipe Class
 *
 * @author Natalie Meuser
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    /**
     * RecipeService, autowired, for manipulating the recipe class
     */
    @Autowired
    private RecipeService service;

    /**
     * Before each test, to resest
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    // // Milestone 1, individually added tests by Natalie Meuser
    // @Test
    // @Transactional
    // public void testCheckRecipe () {
    // // all are zero, returns true
    // final Recipe r1 = new Recipe();
    // r1.setName( "Coffee" );
    // r1.setPrice( 5 );
    //
    // r1.setCoffee( 0 );
    // r1.setMilk( 0 );
    // r1.setSugar( 0 );
    // r1.setChocolate( 0 );
    // Assertions.assertTrue( r1.checkRecipe() );
    //
    // // all are zero except coffee, returns false
    // final Recipe r2 = new Recipe();
    // r2.setName( "Coffee2" );
    // r2.setPrice( 5 );
    // r2.setCoffee( 1 );
    // r2.setMilk( 0 );
    // r2.setSugar( 0 );
    // r2.setChocolate( 0 );
    // Assertions.assertFalse( r2.checkRecipe() );
    //
    // // all are zero except milk, returns false
    // final Recipe r3 = new Recipe();
    // r3.setName( "Coffee3" );
    // r3.setPrice( 5 );
    // r3.setCoffee( 0 );
    // r3.setMilk( 1 );
    // r3.setSugar( 0 );
    // r3.setChocolate( 0 );
    // Assertions.assertFalse( r3.checkRecipe() );
    //
    // // all are zero except sugar, returns false
    // final Recipe r4 = new Recipe();
    // r4.setName( "Coffee4" );
    // r4.setPrice( 5 );
    // r4.setCoffee( 0 );
    // r4.setMilk( 0 );
    // r4.setSugar( 1 );
    // r4.setChocolate( 0 );
    // Assertions.assertFalse( r4.checkRecipe() );
    //
    // // all are zero except chocolate, returns false
    // final Recipe r5 = new Recipe();
    // r5.setName( "Coffee5" );
    // r5.setPrice( 5 );
    // r5.setCoffee( 0 );
    // r5.setMilk( 0 );
    // r5.setSugar( 0 );
    // r5.setChocolate( 1 );
    // Assertions.assertFalse( r5.checkRecipe() );
    // }

    // // Milestone 1, individually added tests by Natalie Meuser
    // @Test
    // @Transactional
    // public void testUpdateRecipe () {
    // final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
    // service.save( r1 );
    // final Recipe r2 = createRecipe( "Coffee2", 60, 4, 2, 2, 1 );
    // service.save( r2 );
    //
    // // check the contents of r1
    // final Recipe retrieved1 = service.findByName( "Coffee" );
    // Assertions.assertEquals( 50, (int) retrieved1.getPrice() );
    // Assertions.assertEquals( 3, (int) retrieved1.getCoffee() );
    // Assertions.assertEquals( 1, (int) retrieved1.getMilk() );
    // Assertions.assertEquals( 1, (int) retrieved1.getSugar() );
    // Assertions.assertEquals( 0, (int) retrieved1.getChocolate() );
    //
    // // check the contents of r2
    // final Recipe retrieved2 = service.findByName( "Coffee2" );
    // Assertions.assertEquals( 60, (int) retrieved2.getPrice() );
    // Assertions.assertEquals( 4, (int) retrieved2.getCoffee() );
    // Assertions.assertEquals( 2, (int) retrieved2.getMilk() );
    // Assertions.assertEquals( 2, (int) retrieved2.getSugar() );
    // Assertions.assertEquals( 1, (int) retrieved2.getChocolate() );
    //
    // // update r1 to contain r2
    // r1.updateRecipe( r2 );
    //
    // // check the contents of r1 updated
    // final Recipe retrieved1Updated = service.findByName( "Coffee" );
    // Assertions.assertEquals( 60, (int) retrieved1Updated.getPrice() );
    // Assertions.assertEquals( 4, (int) retrieved1Updated.getCoffee() );
    // Assertions.assertEquals( 2, (int) retrieved1Updated.getMilk() );
    // Assertions.assertEquals( 2, (int) retrieved1Updated.getSugar() );
    // Assertions.assertEquals( 1, (int) retrieved1Updated.getChocolate() );
    //
    // // check the contents of r2 updated
    // final Recipe retrieved2Updated = service.findByName( "Coffee2" );
    // Assertions.assertEquals( 60, (int) retrieved2Updated.getPrice() );
    // Assertions.assertEquals( 4, (int) retrieved2Updated.getCoffee() );
    // Assertions.assertEquals( 2, (int) retrieved2Updated.getMilk() );
    // Assertions.assertEquals( 2, (int) retrieved2Updated.getSugar() );
    // Assertions.assertEquals( 1, (int) retrieved2Updated.getChocolate() );
    //
    // }

    // Milestone 1, individually added tests by Natalie Meuser
    /**
     * Test to string
     */
    @Test
    @Transactional
    public void testToString () {
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r2 = createRecipe( "Coffee2", 60, 4, 2, 2, 1 );

        Assertions.assertEquals( "Coffee: Coffee, Milk, Sugar\n", r1.toString() );
        Assertions.assertEquals( "Coffee2: Coffee, Milk, Chocolate, Sugar\n", r2.toString() );
    }

    // Milestone 1, individually added tests by Natalie Meuser
    /**
     * Test hashcode
     */
    @Test
    @Transactional
    public void testHashcode () {
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r2 = createRecipe( "Coffee2", 60, 4, 2, 2, 1 );

        Assertions.assertEquals( r1.hashCode(), r1.hashCode() );
        Assertions.assertNotEquals( r1.hashCode(), r2.hashCode() );
    }

    // Milestone 1, individually added tests by Natalie Meuser
    /**
     * Test equals method
     */
    @Test
    @Transactional
    public void testEquals () {
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r1Double = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        // different name, all ingredients and price the same
        final Recipe r2 = createRecipe( "Coffee2", 50, 3, 1, 1, 0 );

        Assertions.assertTrue( r1.equals( r1 ) );
        Assertions.assertTrue( r1.equals( r1Double ) );
        Assertions.assertTrue( r1Double.equals( r1 ) );
        Assertions.assertFalse( r1.equals( r2 ) );
        Assertions.assertFalse( r2.equals( r1 ) );

        Assertions.assertFalse( r1.equals( null ) );
        // Assertions.assertFalse( r1.equals( "DifferentTypeOfObject" ) );

        final Recipe r1Null = createRecipe( null, 50, 3, 1, 1, 0 );
        Assertions.assertFalse( r1Null.equals( r1 ) );
        Assertions.assertFalse( r1.equals( r1Null ) );
    }

    /**
     * Test add recipe
     */
    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( "Coffee", 1 );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        r2.addIngredient( "Sugar", 1 );
        r2.addIngredient( "Chocolate", 1 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    // @Test
    // @Transactional
    // public void testNoRecipes () {
    // Assertions.assertEquals( 0, service.findAll().size(), "There should be no
    // Recipes in the CoffeeMaker" );
    //
    // final Recipe r1 = new Recipe();
    // r1.setName( "Tasty Drink" );
    // r1.setPrice( 12 );
    // r1.setCoffee( -12 );
    // r1.setMilk( 0 );
    // r1.setSugar( 0 );
    // r1.setChocolate( 0 );
    //
    // final Recipe r2 = new Recipe();
    // r2.setName( "Mocha" );
    // r2.setPrice( 1 );
    // r2.setCoffee( 1 );
    // r2.setMilk( 1 );
    // r2.setSugar( 1 );
    // r2.setChocolate( 1 );
    //
    // final List<Recipe> recipes = List.of( r1, r2 );
    //
    // try {
    // service.saveAll( recipes );
    // Assertions.assertEquals( 0, service.count(),
    // "Trying to save a collection of elements where one is invalid should
    // result in neither getting saved" );
    // }
    // catch ( final Exception e ) {
    // Assertions.assertTrue( e instanceof ConstraintViolationException );
    // }
    //
    // }

    /**
     * Test add recipe
     */
    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */
    /**
     * Test add recipe
     */
    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    /**
     * Test add recipe
     */
    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";

        try {
            createRecipe( name, 50, -3, 1, 1, 2 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final IllegalArgumentException iae ) {
            // expected
        }

    }

    /**
     * Test add recipe
     */
    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    /**
     * Test add recipe
     */
    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    /**
     * Test delete recipe
     */
    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    /**
     * Test delete recipe
     */
    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    /**
     * Added persistence test for testing deleting a recipe that was already
     * removed
     */
    @Test
    @Transactional
    public void testDeleteRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        service.delete( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        // try deleting a recipe that was already removed
        service.delete( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

    }

    /**
     * Added persistence test for testing deleting a recipe that was never in
     * the database
     */
    @Test
    @Transactional
    public void testDeleteRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        // try deleting a recipe never in the database
        service.delete( r2 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

    }

    /**
     * Added persistence test for testing deleting a null object
     */
    @Test
    @Transactional
    public void testDeleteRecipeNull () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        try {
            service.delete( null );

            Assertions.fail( "Able to delete a null recipe object." );
        }
        catch ( final InvalidDataAccessApiUsageException e ) {
            // expected
        }

    }

    /**
     * Test edit recipe
     */
    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, retrieved.getIngredientAmount( "Coffee" ) );
        Assertions.assertEquals( 1, retrieved.getIngredientAmount( "Milk" ) );
        Assertions.assertEquals( 1, retrieved.getIngredientAmount( "Sugar" ) );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    /**
     * Test delete ingredient from recipe
     */
    @Test
    @Transactional
    public void testDeleteIngredientValid () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0, 1 );
        service.save( r1 );

        Assertions.assertEquals( 3, r1.getIngredients().size() );

        r1.deleteIngredient( "Chocolate" );

        Assertions.assertEquals( 2, r1.getIngredients().size() );

        Assertions.assertEquals( 3, r1.getIngredientAmount( "Coffee" ) );
        Assertions.assertEquals( 1, r1.getIngredientAmount( "Milk" ) );

        try {
            r1.getIngredientAmount( "Chocolate" );
            Assertions.fail( "Ingredient should have been deleted from the recipe." );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Ingredient does not exist in the recipe.", iae.getMessage() );
        }

    }

    /**
     * Test delete ingredient from recipe
     */
    @Test
    @Transactional
    public void testDeleteIngredientInvalid () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0, 0 );
        service.save( r1 );

        Assertions.assertEquals( 2, r1.getIngredients().size() );

        try {
            r1.deleteIngredient( "Sugar" );
            Assertions.fail( "Sugar is not in the recipe." );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Ingredient does not exist in the recipe.", iae.getMessage() );
        }

        r1.deleteIngredient( "Milk" );

        Assertions.assertEquals( 1, r1.getIngredients().size() );

        try {
            r1.deleteIngredient( "Coffee" );
            Assertions.fail( "Need at least one ingredient in the recipe." );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Recipe needs at least one ingredient.", iae.getMessage() );
        }

        Assertions.assertEquals( 1, r1.getIngredients().size() );

        Assertions.assertEquals( 3, r1.getIngredientAmount( "Coffee" ) );
    }

    /**
     * Test invalid set ingredient
     */
    @Test
    @Transactional
    public void testInvalidSetIngredient () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Ingredient coffee = new Ingredient( "Coffee", 2 );
        final Ingredient milk = new Ingredient( "Milk", 1 );
        final Ingredient sugar = new Ingredient( "Sugar", 2 );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( coffee );
        r1.addIngredient( milk );

        service.save( r1 );

        // try setting the ingredient amount to a value less than or equal to
        // zero
        try {
            r1.setIngredient( coffee, -1 );
            Assertions.fail( "Ingredient amount must be greater than 0." );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Invalid amount.", iae.getMessage() );
        }

        // try setting an ingredient that is not in the recipe
        try {
            r1.setIngredient( sugar, 3 );
            Assertions.fail( "Ingredient should not be in the recipe." );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( "Ingredient does not exist in the recipe.", iae.getMessage() );
        }

    }

    /**
     * Helper method for creating recipes
     *
     * @param name
     *            of the recipe
     * @param price
     *            of the recipe
     * @param coffee
     *            amount of coffee
     * @param milk
     *            amount of milk
     * @param sugar
     *            amount of sugar
     * @param chocolate
     *            amount of chocolate
     * @return the created recipe
     */
    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( "Coffee", coffee );
        recipe.addIngredient( "Milk", milk );
        if ( chocolate > 0 ) {
            recipe.addIngredient( "Chocolate", chocolate );
        }
        if ( sugar > 0 ) {
            recipe.addIngredient( "Sugar", sugar );
        }

        return recipe;
    }

}
