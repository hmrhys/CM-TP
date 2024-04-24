package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.IngredientDTO;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Inventory.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIInventoryController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService service;

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's singleton
     * Inventory. This will convert the Inventory to JSON.
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity getInventory () {
        final Inventory inventory = service.getInventory();
        return new ResponseEntity( inventory, HttpStatus.OK );
    }

    /**
     * REST API endpoint to provide GET access to the CoffeeMaker's inventory
     *
     * @return response to request
     *
     */
    @GetMapping ( BASE_PATH + "/inventoryList" )
    public List<IngredientDTO> getInventoryList () {
        final Inventory inventory = service.getInventory();
        final List<Ingredient> ingredients = inventory.getInventoryList();
        final List<IngredientDTO> dtos = new ArrayList<>();
        for ( final Ingredient ingredient : ingredients ) {
            dtos.add( new IngredientDTO( ingredient.getName(), ingredient.getAmount() ) );
        }
        return dtos;
    }

    /**
     * REST API endpoint to provide update access to CoffeeMaker's singleton
     * Inventory. This will update the Inventory of the CoffeeMaker by adding
     * amounts from the Inventory provided to the CoffeeMaker's stored inventory
     *
     * @param inventory
     *            amounts to add to inventory
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventory" )
    public ResponseEntity updateInventory ( @RequestBody final Inventory inventory ) {
        final Inventory inventoryCurrent = service.getInventory();

        final List<Ingredient> inventoryList = inventory.getInventoryList();

        for ( final Ingredient i : inventoryList ) {
            try {
                inventoryCurrent.getInventoryIngredient( i.getName() );
                inventoryCurrent.setInventoryIngredient( i.getName(), i.getAmount() );
            }
            catch ( final IllegalArgumentException iae ) {
                inventoryCurrent.addIngredient( i.getName(), i.getAmount() );
            }
        }

        // inventoryCurrent.addIngredients( inventory.getCoffee(),
        // inventory.getMilk(), inventory.getSugar(),
        // inventory.getChocolate() );
        service.save( inventoryCurrent );
        return new ResponseEntity( successResponse( "Inventory was successfully updated." ), HttpStatus.OK );
    }

    /**
     * REST API endpoint to add an ingredient to the inventory.
     *
     * @param ingredient
     *            name and starting amount of the ingredient to add
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/inventoryAdd" )
    public ResponseEntity addToInventory ( @RequestBody final Ingredient ingredient ) {

        final Inventory inventory = service.getInventory();
        final List<Ingredient> ingredients = inventory.getInventoryList();

        for ( final Ingredient ingredientItem : ingredients ) {
            if ( ingredientItem.getName().equals( ingredient.getName() ) ) {
                return new ResponseEntity(
                        errorResponse( "Ingredient with the name " + ingredient.getName() + " already exists." ),
                        HttpStatus.CONFLICT );
            }
        }

        // System.out.println( ingredient.toString() );

        inventory.addIngredient( ingredient.getName(), ingredient.getAmount() );

        // System.out.println( inventory.toString() );

        service.save( inventory );

        // final Inventory updatedInventory = service.getInventory();
        //
        // System.out.println( updatedInventory.toString() );

        return new ResponseEntity( successResponse( ingredient.getName() + " successfully created." ), HttpStatus.OK );
    }
}
