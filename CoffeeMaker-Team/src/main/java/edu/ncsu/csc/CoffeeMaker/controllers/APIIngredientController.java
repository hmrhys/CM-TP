package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Ingredient.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Natalie Meuser
 *
 */
@RestController
public class APIIngredientController extends APIController {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Ingredient. This will convert the Ingredient to JSON.
     *
     * @param name
     *            of the ingredient to get
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
    public ResponseEntity getIngredient ( @PathVariable ( "name" ) final String name ) {

        final Ingredient ingr = ingredientService.findByName( name );

        if ( null == ingr ) {
            return new ResponseEntity( errorResponse( "No ingredient found with name " + name ), HttpStatus.NOT_FOUND );
        }

        return new ResponseEntity( ingr, HttpStatus.OK );
    }

}
