package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * The Ingredient class which manages all of the aspects of an ingredient
 *
 * @author Natalie Meuser
 */
@Entity
public class Ingredient extends DomainObject {

    /** The id of the ingredient */
    @Id
    @GeneratedValue
    private Long   id;

    // /** The ingredient */
    // @Enumerated ( EnumType.STRING )
    // private IngredientType ingredient;

    /** Amount of the ingredient */
    private int    amount;

    /** Amount of the ingredient */
    private String name;

    /**
     * Returns the name of the ingredient
     *
     * @return name of the ingredient
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of the ingredient to the specified name
     *
     * @param name
     *            to set the name of the ingredient to
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Constructor of ingredient with no parameters Used by Hibernate when
     * loading objects from the database
     */
    public Ingredient () {
        super();
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Creates a new ingredient with the specific ingredient type and amount
     *
     * @param name
     *            of ingredient
     * @param amount
     *            of ingredient needed in the recipe
     */
    public Ingredient ( final String name, final int amount ) {
        super();
        setName( name );
        setAmount( amount );
    }

    // /**
    // * Returns the type of the ingredient
    // *
    // * @return the ingredient type
    // */
    // public IngredientType getIngredient () {
    // return ingredient;
    // }

    // /**
    // * Sets the ingredient type of the ingredient
    // *
    // * @param ingredient
    // * type to set the ingredient to
    // */
    // public void setIngredient ( final IngredientType ingredient ) {
    // this.ingredient = ingredient;
    // }

    /**
     * Returns the amount of the ingredient needed in the recipe
     *
     * @return the amount of the ingredient needed
     */
    public int getAmount () {
        return amount;
    }

    /**
     * Set the amount of the ingredient needed in the recipe
     *
     * @param amount
     *            of ingredient to set for the recipe
     */
    public void setAmount ( final int amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount must be positive." );
        }
        this.amount = amount;
    }

    /**
     * Creates a string representation of the ingredient
     *
     * @return a string representation of the ingredient
     */
    @Override
    public String toString () {
        return "Ingredient " + name + ", amount=" + amount + "\n";
    }

}
