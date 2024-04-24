package edu.ncsu.csc.CoffeeMaker.api;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests the API Coffee Controller
 *
 * @author Natalie Meuser
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * Context of the web application
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * CoffeeOrderService, autowired, allows for manipulating the CoffeeOrder
     * model
     */
    @Autowired
    private CoffeeOrderService    coffeeOrderService;

    /**
     * UserService, autowired, allows for manipulating the User model
     */
    @Autowired
    private UserService           userService;

    /**
     * InventoryService, autowired, allows for manipulating the Inventory model
     */
    @Autowired
    private InventoryService      iService;

    /**
     * RecipeService, autowired, allows for manipulating the Recipe model
     */
    @Autowired
    private RecipeService         recipeService;

    /**
     * Instance of the recipe used for testing
     */
    private Recipe                recipe = null;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        coffeeOrderService.deleteAll();
        recipeService.deleteAll();
        userService.deleteAll();
        iService.deleteAll();

        final Inventory ivt = iService.getInventory();

        final List<Ingredient> inventoryList = ivt.getInventoryList();

        for ( final Ingredient ingredient : inventoryList ) {
            ivt.setInventoryIngredient( ingredient.getName(), 15 );
        }

        iService.save( ivt );

        recipe = new Recipe();
        recipe.setName( "Coffee" );
        recipe.setPrice( 50 );
        recipe.addIngredient( "Coffee", 2 );
        recipe.addIngredient( "Milk", 1 );
        recipeService.save( recipe );
    }

    // @Test
    // @Transactional
    // public void testMakeCoffee () throws Exception {
    //
    // final Customer c2 = new Customer( "username3", "pw3" );
    // userService.save( c2 );
    // final Staff s3 = new Staff( "staffUsername3", "pw3" );
    // userService.save( s3 );
    //
    // final CoffeeOrder valid = new CoffeeOrder( recipe, c2 );
    //
    // final String id = "" + valid.getId();
    //
    // final String makeCoffeeResult = mvc
    // .perform( post( String.format( "/api/v1/makecoffee/%s", id )
    // ).contentType( MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( s3 ) ) )
    // .andExpect( status().isOk()
    // ).andReturn().getResponse().getContentAsString();
    //
    // Assertions.assertEquals( "", makeCoffeeResult );
    //
    // }

    // @Test
    // @Transactional
    // public void testOrderCoffee () throws Exception {
    // final Customer c2 = new Customer( "username3", "pw3" );
    // userService.save( c2 );
    // final Staff s3 = new Staff( "staffUsername3", "pw3" );
    // userService.save( s3 );
    //
    // final CoffeeOrder valid = new CoffeeOrder( recipe, c2 );
    //
    // valid.fulfilledOrder( s3 );
    //
    // coffeeOrderService.save( valid );
    //
    // final String name = recipe.getName();
    //
    // mvc.perform( post( String.format( "/api/v1/orderCoffee/%s", name )
    // ).contentType( MediaType.APPLICATION_JSON )
    // .content( TestUtils.asJsonString( 3 ) ) ).andExpect( status().isOk() )
    // .andExpect( jsonPath( "$.message" ).value( 2 ) );
    //
    // }
}
