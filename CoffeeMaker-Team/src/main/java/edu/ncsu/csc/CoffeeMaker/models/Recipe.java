package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long             id;

    /** Recipe name */
    private String           name;

    /** Recipe price */
    @Min ( 0 )
    private Integer          price;

    /** Ingredients associated with the Recipe */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /** The order the uses the recipe */
    @OneToOne ( cascade = CascadeType.ALL )
    private CoffeeOrder      order;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Adds an ingredient object to the Recipe's ingredient list
     *
     * @param ingredient
     *            the ingredient to add
     */
    public void addIngredient ( final Ingredient ingredient ) {
        this.ingredients.add( ingredient );
    }

    /**
     * Adds an ingredient object to the Recipe's ingredient list
     *
     * @param name
     *            of the ingredient to add
     * @param amount
     *            of the ingredient to use in the recipe
     */
    public void addIngredient ( final String name, final Integer amount ) {
        if ( amount <= 0 ) {
            throw new IllegalArgumentException( "Amount must be positive." );
        }
        this.ingredients.add( new Ingredient( name, amount ) );
    }

    /**
     * Removes an ingredient from the Recipe's ingredient list
     *
     * @param name
     *            of the ingredient to remove
     * @return true if able to remote
     * @throws IllegalArgumentException
     *             if trying to remove an ingredient when the recipe only has 1
     *             or less ingredients
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the recipe
     */
    public boolean deleteIngredient ( final String name ) {
        if ( ingredients.size() <= 1 ) {
            throw new IllegalArgumentException( "Recipe needs at least one ingredient." );
        }

        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName().equals( name ) ) {
                ingredients.remove( i );
                return true;
            }
        }

        throw new IllegalArgumentException( "Ingredient does not exist in the recipe." );
    }

    /**
     * Returns the list of ingredients associated with the recipe
     *
     * @return the list of ingredients
     */
    public List<Ingredient> getIngredients () {
        return this.ingredients;
    }

    /**
     * Sets the list of ingredients to the current list
     *
     * @param listOfIngredients
     *            of ingredients
     */
    public void setIngredients ( final List<Ingredient> listOfIngredients ) {
        this.ingredients = listOfIngredients;

        // for ( int i = 0; i < this.ingredients.size(); i++ ) {
        // this.ingredients.remove( i );
        // }
        // System.out.println( "" );
        // System.out.println( "" );
        // System.out.println( this.ingredients.toString() );
        // System.out.println( "" );
        // System.out.println( "" );
        // for ( int i = 0; i < listOfIngredients.size(); i++ ) {
        // this.ingredients.add( listOfIngredients.get( i ) );
        // }
    }

    /**
     * Set the amount of the ingredient needed in the recipe to the specified
     * amount
     *
     * @param ingredient
     *            to set the amount of
     * @param amount
     *            of the ingredient to set the amount to
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the recipe
     * @throws IllegalArgumentException
     *             if the amount is not positive
     */
    public void setIngredient ( final Ingredient ingredient, final Integer amount ) {
        if ( amount <= 0 ) {
            throw new IllegalArgumentException( "Invalid amount." );
        }

        for ( final Ingredient recipeIngredient : ingredients ) {
            if ( recipeIngredient.getName().equals( ingredient.getName() ) ) {
                recipeIngredient.setAmount( amount );
                return;
            }
        }

        throw new IllegalArgumentException( "Ingredient does not exist in the recipe." );
    }

    /**
     * Returns the amount of the ingredient needed in the recipe
     *
     * @param name
     *            of the ingredient to get the amount of
     * @return the amount of the ingredient needed in the recipe
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the recipe
     */
    public int getIngredientAmount ( final String name ) {
        for ( final Ingredient recipeIngredient : ingredients ) {
            if ( recipeIngredient.getName().equals( name ) ) {
                return recipeIngredient.getAmount();
            }
        }

        throw new IllegalArgumentException( "Ingredient does not exist in the recipe." );
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
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuilder buf = new StringBuilder();
        buf.append( name );
        buf.append( ": " );
        for ( final Ingredient ingredient : ingredients ) {
            buf.append( ingredient.getName() );
            buf.append( ", " );
        }
        buf.delete( buf.length() - 2, buf.length() );
        buf.append( "\n" );

        return buf.toString();
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
