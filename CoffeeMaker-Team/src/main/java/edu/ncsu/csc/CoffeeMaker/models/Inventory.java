package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long             id;

    /** List of ingredient objects that are in the inventory database */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> inventoryList;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param coffee
     *            default amt of coffee
     * @param milk
     *            default amt of milk
     */
    public Inventory ( final Integer coffee, final Integer milk ) {
        inventoryList = new ArrayList<Ingredient>();

        // // add in the default ingredients
        // addIngredient( "Coffee", coffee );
        // addIngredient( "Milk", milk );
    }

    /**
     * Returns the list of ingredients in the inventory
     *
     * @return a list of ingredients in the inventory
     */
    public List<Ingredient> getInventoryList () {
        return inventoryList;
    }

    /**
     * Sets the inventory list equal to a specific list of ingredient inventory
     *
     * @param inventoryList
     *            to set the inventory list equal to
     */
    public void setInventoryList ( final List<Ingredient> inventoryList ) {
        this.inventoryList = inventoryList;
    }

    /**
     * Add Ingredient to inventory list
     *
     * @param name
     *            of the ingredient
     * @param amount
     *            of the ingredient
     * @return true if the ingredient has been added to the inventory list
     * @throws IllegalArgumentException
     *             if the ingredient already exists in the inventory
     * @throws IllegalArgumentException
     *             if the amount for the ingredient is less than 0
     */
    public boolean addIngredient ( final String name, final Integer amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount of ingredient must be positive." );
        }

        for ( final Ingredient ingredient : inventoryList ) {
            if ( ingredient.getName().equals( name ) ) {
                throw new IllegalArgumentException( "Ingredient already exists." );
            }
        }

        final Ingredient newIngredient = new Ingredient( name, amount );
        inventoryList.add( newIngredient );
        return true;
    }

    /**
     * Add inventory for an ingredient in the list
     *
     * @param name
     *            of the ingredient to add inventory to
     * @param amount
     *            to add to the ingredient
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the inventory
     * @throws IllegalArgumentException
     *             if the amount for the ingredient is less than 0
     */
    public void addInventoryIngredient ( final String name, final Integer amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount of ingredient must be positive." );
        }

        for ( final Ingredient ingredient : inventoryList ) {
            if ( ingredient.getName().equals( name ) ) {
                ingredient.setAmount( ingredient.getAmount() + amount );
                return;
            }
        }

        throw new IllegalArgumentException( "Ingredient does not exist in the inventory." );
    }

    /**
     * Set the amount in the inventory to a specified amount
     *
     * @param name
     *            of the ingredient to set the amount to
     * @param amount
     *            to set the inventory of the ingredient to
     * @throws IllegalArgumentException
     *             if the amount of the ingredient is not positive
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the inventory
     */
    public void setInventoryIngredient ( final String name, final Integer amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount of ingredient must be positive." );
        }

        for ( final Ingredient inventoryIngredient : inventoryList ) {
            if ( inventoryIngredient.getName().equals( name ) ) {
                inventoryIngredient.setAmount( amount );
                return;
            }
        }

        throw new IllegalArgumentException( "Ingredient does not exist in the inventory." );
    }

    /**
     * Get the inventory amount for a specified ingredient
     *
     * @param name
     *            of the ingredient to get the inventory amount for
     * @return the inventory amount for the ingredient
     * @throws IllegalArgumentException
     *             if the ingredient does not exist in the inventory.
     */
    public int getInventoryIngredient ( final String name ) {
        for ( final Ingredient inventoryIngredient : inventoryList ) {
            if ( inventoryIngredient.getName().equals( name ) ) {
                return inventoryIngredient.getAmount();
            }
        }

        throw new IllegalArgumentException( "Ingredient does not exist in the inventory." );
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        final List<Ingredient> recipeIngredients = r.getIngredients();

        for ( final Ingredient recipeIngredient : recipeIngredients ) {
            for ( final Ingredient inventoryIngredient : inventoryList ) {
                if ( inventoryIngredient.getName().equals( recipeIngredient.getName() ) ) {
                    if ( inventoryIngredient.getAmount() < recipeIngredient.getAmount() ) {
                        return false;
                    }
                }
            }
        }

        return true;

        // boolean isEnough = true;
        // if ( coffee < r.getCoffee() ) {
        // isEnough = false;
        // }
        // if ( milk < r.getMilk() ) {
        // isEnough = false;
        // }
        // if ( sugar < r.getSugar() ) {
        // isEnough = false;
        // }
        // if ( chocolate < r.getChocolate() ) {
        // isEnough = false;
        // }
        // return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        final List<Ingredient> recipeIngredients = r.getIngredients();

        if ( enoughIngredients( r ) ) {
            for ( final Ingredient recipeIngredient : recipeIngredients ) {
                for ( final Ingredient inventoryIngredient : inventoryList ) {
                    if ( inventoryIngredient.getName().equals( recipeIngredient.getName() ) ) {
                        inventoryIngredient.setAmount( inventoryIngredient.getAmount() - recipeIngredient.getAmount() );
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }

        // if ( enoughIngredients( r ) ) {
        // setCoffee( coffee - r.getCoffee() );
        // setMilk( milk - r.getMilk() );
        // setSugar( sugar - r.getSugar() );
        // setChocolate( chocolate - r.getChocolate() );
        // return true;
        // }
        // else {
        // return false;
        // }
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( final Ingredient ingredient : inventoryList ) {
            buf.append( ingredient.getName() );
            buf.append( ": " );
            buf.append( ingredient.getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

    // /**
    // * Add the number of chocolate units in the inventory to the current
    // amount
    // * of chocolate units.
    // *
    // * @param chocolate
    // * amount of chocolate
    // * @return checked amount of chocolate
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkChocolate ( final String chocolate ) throws
    // IllegalArgumentException {
    // Integer amtChocolate = 0;
    // try {
    // amtChocolate = Integer.parseInt( chocolate );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of chocolate must be a
    // positive integer" );
    // }
    // if ( amtChocolate < 0 ) {
    // throw new IllegalArgumentException( "Units of chocolate must be a
    // positive integer" );
    // }
    //
    // return amtChocolate;
    // }
    //
    // /**
    // * Add the number of coffee units in the inventory to the current amount
    // of
    // * coffee units.
    // *
    // * @param coffee
    // * amount of coffee
    // * @return checked amount of coffee
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkCoffee ( final String coffee ) throws
    // IllegalArgumentException {
    // Integer amtCoffee = 0;
    // try {
    // amtCoffee = Integer.parseInt( coffee );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of coffee must be a positive
    // integer" );
    // }
    // if ( amtCoffee < 0 ) {
    // throw new IllegalArgumentException( "Units of coffee must be a positive
    // integer" );
    // }
    //
    // return amtCoffee;
    // }
    //
    // /**
    // * Add the number of milk units in the inventory to the current amount of
    // * milk units.
    // *
    // * @param milk
    // * amount of milk
    // * @return checked amount of milk
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkMilk ( final String milk ) throws
    // IllegalArgumentException {
    // Integer amtMilk = 0;
    // try {
    // amtMilk = Integer.parseInt( milk );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of milk must be a positive
    // integer" );
    // }
    // if ( amtMilk < 0 ) {
    // throw new IllegalArgumentException( "Units of milk must be a positive
    // integer" );
    // }
    //
    // return amtMilk;
    // }
    //
    // /**
    // * Add the number of sugar units in the inventory to the current amount of
    // * sugar units.
    // *
    // * @param sugar
    // * amount of sugar
    // * @return checked amount of sugar
    // * @throws IllegalArgumentException
    // * if the parameter isn't a positive integer
    // */
    // public Integer checkSugar ( final String sugar ) throws
    // IllegalArgumentException {
    // Integer amtSugar = 0;
    // try {
    // amtSugar = Integer.parseInt( sugar );
    // }
    // catch ( final NumberFormatException e ) {
    // throw new IllegalArgumentException( "Units of sugar must be a positive
    // integer" );
    // }
    // if ( amtSugar < 0 ) {
    // throw new IllegalArgumentException( "Units of sugar must be a positive
    // integer" );
    // }
    //
    // return amtSugar;
    // }

    // /**
    // * Adds ingredients to the inventory
    // *
    // * @param coffee
    // * amt of coffee
    // * @param milk
    // * amt of milk
    // * @param sugar
    // * amt of sugar
    // * @param chocolate
    // * amt of chocolate
    // * @return true if successful, false if not
    // */
    // public boolean addIngredients ( final Integer coffee, final Integer milk,
    // final Integer sugar,
    // final Integer chocolate ) {
    // if ( coffee < 0 || milk < 0 || sugar < 0 || chocolate < 0 ) {
    // throw new IllegalArgumentException( "Amount cannot be negative" );
    // }
    //
    // setCoffee( this.coffee + coffee );
    // setMilk( this.milk + milk );
    // setSugar( this.sugar + sugar );
    // setChocolate( this.chocolate + chocolate );
    //
    // return true;
    // }

}
