package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * Testing the Ingredient Class
 *
 * @author Natalie Meuser
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    /**
     * Test creating valid ingredient
     */
    @Test
    @Transactional
    public void testCreateValidIngredient () {
        final Ingredient valid = new Ingredient( "Coffee", 5 );

        Assertions.assertEquals( "Coffee", valid.getName() );
        Assertions.assertEquals( 5, valid.getAmount() );
    }

    /**
     * Test creating an invalid ingredient
     */
    @Test
    @Transactional
    public void testCreateInvalidIngredient () {
        try {
            new Ingredient( "Coffee", -5 );
            Assertions.fail( "Ingredient should not have been created." );
        }
        catch ( final IllegalArgumentException iae ) {
            // expected
        }
    }

    /**
     * Test the to string
     */
    @Test
    @Transactional
    public void testToString () {
        final Ingredient valid = new Ingredient( "Coffee", 5 );
        Assertions.assertEquals( "Ingredient Coffee, amount=5\n", valid.toString() );
    }

}
