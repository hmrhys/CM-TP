
package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Test database interactions
 *
 * @author Natalie Meuser
 * @author Sophie Scherer
 * @author Caleb Young
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {
    /**
     * RecipeService, autowired, for manipulating the recipe model
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {
        final Recipe r = new Recipe();
        r.setName( "Mocha" );
        r.setPrice( 350 );

        final Ingredient coffeeIngredient = new Ingredient( "Coffee", 2 );
        final Ingredient milkIngredient = new Ingredient( "Milk", 1 );
        final Ingredient sugarIngredient = new Ingredient( "Sugar", 1 );
        final Ingredient chocolateIngredient = new Ingredient( "Chocolate", 1 );
        r.addIngredient( coffeeIngredient );
        r.addIngredient( milkIngredient );
        r.addIngredient( sugarIngredient );
        r.addIngredient( chocolateIngredient );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();
        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );
        assertEquals( r.getName(), dbRecipe.getName() );

        assertEquals( "Mocha", dbRecipe.getName() );
        assertEquals( 350, (int) dbRecipe.getPrice() );
        final List<Ingredient> recipeIngredients = dbRecipe.getIngredients();

        assertEquals( 2, recipeIngredients.get( 0 ).getAmount() );
        assertEquals( "Coffee", recipeIngredients.get( 0 ).getName() );
        assertEquals( 1, recipeIngredients.get( 1 ).getAmount() );
        assertEquals( "Milk", recipeIngredients.get( 1 ).getName() );
        assertEquals( 1, recipeIngredients.get( 2 ).getAmount() );
        assertEquals( "Sugar", recipeIngredients.get( 2 ).getName() );
        assertEquals( 1, recipeIngredients.get( 3 ).getAmount() );
        assertEquals( "Chocolate", recipeIngredients.get( 3 ).getName() );

        final Recipe getRecipe = recipeService.findByName( "Mocha" );
        assertEquals( r.getName(), getRecipe.getName() );
        assertEquals( "Mocha", getRecipe.getName() );
        assertEquals( 350, (int) getRecipe.getPrice() );
        final List<Ingredient> getRecipeIngredients = getRecipe.getIngredients();
        assertEquals( 2, getRecipeIngredients.get( 0 ).getAmount() );
        assertEquals( "Coffee", getRecipeIngredients.get( 0 ).getName() );
        assertEquals( 1, getRecipeIngredients.get( 1 ).getAmount() );
        assertEquals( "Milk", getRecipeIngredients.get( 1 ).getName() );
        assertEquals( 1, getRecipeIngredients.get( 2 ).getAmount() );
        assertEquals( "Sugar", getRecipeIngredients.get( 2 ).getName() );
        assertEquals( 1, getRecipeIngredients.get( 3 ).getAmount() );
        assertEquals( "Chocolate", getRecipeIngredients.get( 3 ).getName() );
        dbRecipe.setPrice( 15 );
        dbRecipe.setIngredient( sugarIngredient, 2 );
        recipeService.save( dbRecipe );

        assertEquals( 1, recipeService.count() );

        assertEquals( 15, (int) recipeService.findAll().get( 0 ).getPrice() );

        assertEquals( 2, recipeService.findAll().get( 0 ).getIngredients().get( 2 ).getAmount() );

    }

}
