package edu.ncsu.csc.CoffeeMaker.models;

/**
 * Ingredient data transfer object
 *
 * @author Natalie Meuser
 *
 */
public class IngredientDTO {

    /** Name of the ingredient */
    private String name;

    /** Amount of the ingredient */
    private int    amount;

    /**
     * Creates a new ingredient data transfer object
     *
     * @param name
     *            of the ingredient
     * @param amount
     *            of the ingredient
     *
     */
    public IngredientDTO ( final String name, final int amount ) {
        super();
        setName( name );
        setAmount( amount );
    }

    /**
     * Returns the name of the ingredient
     *
     * @return name of the ingredient
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of the ingredient
     *
     * @param name
     *            of the ingredient
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the amount of the ingredient
     *
     * @return the amount of the ingredient
     */
    public int getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient
     *
     * @param amount
     *            of the ingredient
     */
    public void setAmount ( final int amount ) {
        this.amount = amount;
    }

}
