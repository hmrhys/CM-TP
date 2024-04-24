package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.IngredientDTO;

/**
 * Testing the Ingredient DTO class
 *
 * @author Natalie Meuser
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientDTOTest {

    /**
     * Test creating a valid ingredientDTO
     */
    @Test
    @Transactional
    public void testCreateValidIngredientDTO () {
        final IngredientDTO valid = new IngredientDTO( "Coffee", 5 );

        Assertions.assertEquals( "Coffee", valid.getName() );
        Assertions.assertEquals( 5, valid.getAmount() );
    }
}
