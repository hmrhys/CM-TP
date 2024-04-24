package edu.ncsu.csc.CoffeeMaker;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Test generating recipes
 *
 * @author Natalie Meuser
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {

    /**
     * RecipeService, autowired, for manipulating the recipe model
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Before each test, reset the recipe sevice
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Test creating a recipe
     */
    @Test
    @Transactional
    public void createRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Delicious Coffee" );

        r1.setPrice( 50 );

        r1.addIngredient( new Ingredient( "Coffee", 10 ) );
        r1.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 2 ) );

        recipeService.save( r1 );

        printRecipes();
    }

    /**
     * Helper method for printing the recipes
     */
    private void printRecipes () {
        for ( final DomainObject r : recipeService.findAll() ) {
            System.out.println( r );
        }
    }

}
