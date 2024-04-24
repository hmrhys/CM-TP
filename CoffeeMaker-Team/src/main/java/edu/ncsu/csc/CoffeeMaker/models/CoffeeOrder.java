package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The Coffee class that manages all aspects of an order placed through
 * CoffeeMaker
 *
 * @author Natalie Meuser
 *
 */
@Entity
public class CoffeeOrder extends DomainObject {

    /** id for order entry */
    @Id
    @GeneratedValue
    private Long     id;

    /**
     * Order status of the order; false for not fulfilled, true for fulfilled
     */
    private boolean  orderStatus;

    /**
     * Order status of if the order has been picked up; false for not, true for
     * yes
     */
    private boolean  pickedUpStatus;

    /**
     * The customer who placed the order
     */
    @ManyToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn ( name = "customer_id" )
    private Customer customer;

    /**
     * The staff who fulfilled the order
     */
    @ManyToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn ( name = "staff_id" )
    private Staff    staff;

    /**
     * The recipe ordered
     */
    @ManyToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn ( name = "recipe_id" )
    private Recipe   recipe;

    /**
     * Empty constructor used by hibernate
     */
    public CoffeeOrder () {
        super();
    }

    /**
     * Creates a new Coffee Order
     *
     * @param recipeOrdered
     *            the recipe that is ordered
     * @param customer
     *            the customer who ordered the recipe
     */
    public CoffeeOrder ( final Recipe recipeOrdered, final Customer customer ) {
        super();
        setCustomer( customer );
        setStaff( null );
        setRecipeOrdered( recipeOrdered );
        setStatus( false );
        setPickUpStatus( false );
    }

    /**
     * Fulfills an order by setting the status to true and setting the staff
     * member who fulfilled the order
     *
     * @param staffFulfilled
     *            the staff member who fulfilled the order
     */
    public void fulfilledOrder ( final Staff staffFulfilled ) {
        setStaff( staffFulfilled );
        setStatus( true );
    }

    /**
     * Picks up an order by setting the pick up status to true
     */
    public void pickUpOrder () {
        setPickUpStatus( true );
    }

    /**
     * Sets the recipe that will be ordered
     *
     * @param recipeOrdered
     *            recipe to be ordered
     * @throws IllegalArgumentException
     *             if the recipe is null
     */
    private void setRecipeOrdered ( final Recipe recipeOrdered ) {
        if ( recipeOrdered == null ) {
            throw new IllegalArgumentException( "Recipe must not be null." );
        }
        this.recipe = recipeOrdered;

    }

    /**
     * Returns the recipe that was ordered
     *
     * @return the recipe that was ordered
     */
    public Recipe getRecipeOrdered () {
        return this.recipe;
    }

    /**
     * Sets the staff that fulfilled the ordered
     *
     * @param staff
     *            that fulfilled the order
     */
    private void setStaff ( final Staff staff ) {
        this.staff = staff;
    }

    /**
     * Returns the staff that fulfilled the order, or null if the order has not
     * be fulfilled yet
     *
     * @return the staff who fulfilled the order
     */
    public Staff getStaff () {
        return staff;
    }

    /**
     * Sets the customer who placed the order
     *
     * @param customer
     *            who placed the order
     * @throws IllegalArgumentException
     *             if the customer is null
     */
    private void setCustomer ( final Customer customer ) {
        if ( customer == null ) {
            throw new IllegalArgumentException( "Customer must not be null." );
        }
        this.customer = customer;
    }

    /**
     * Returns the customer who placed the order
     *
     * @return the customer who placed the order
     */
    public Customer getCustomer () {
        return customer;

    }

    /**
     * Sets the status of the order as false as not fulfilled or true as
     * fulfilled
     *
     * @param orderStatus
     *            to set the status as
     */
    private void setStatus ( final boolean orderStatus ) {
        this.orderStatus = orderStatus;
    }

    /**
     * Returns the status of the order
     *
     * @return the status of the order
     */
    public boolean getStatus () {
        return orderStatus;
    }

    /**
     * Sets the pick up status of the order as false if not picked up or true if
     * it has been picked up
     *
     * @param pickUpStatus
     *            to set the status as
     * @throws IllegalArgumentException
     *             if trying to set pickup status as true if the order is not
     *             ready yet
     */
    private void setPickUpStatus ( final boolean pickUpStatus ) {
        if ( !this.orderStatus && pickUpStatus ) {
            throw new IllegalArgumentException( "Order must be ready before it can be picked up." );
        }
        this.pickedUpStatus = pickUpStatus;
    }

    /**
     * Returns the pick up status of the order
     *
     * @return the pick up status of the order
     */
    public boolean getPickUpStatus () {
        return this.pickedUpStatus;
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
     * Returns the id of the order
     *
     * @return the id of the order
     */
    @Override
    public Long getId () {
        return this.id;
    }

    /**
     * Creates a string representation of a coffee order
     *
     * @return a string representation of a coffee order
     */
    @Override
    public String toString () {
        return "Order of " + recipe.getName();
    }

    /**
     * Creates a string containing all order information
     *
     * @return a string containing all order information
     */
    public String toInfoString () {
        final StringBuilder buf = new StringBuilder();
        buf.append( "Order of " );
        buf.append( recipe.getName() );
        buf.append( ", placed by " );
        buf.append( customer.getUsername() );
        buf.append( "." );

        if ( orderStatus ) {
            buf.append( " Fulfilled by " );
            buf.append( staff.getUsername() );
            if ( pickedUpStatus ) {
                buf.append( "; Picked up." );
            }
            else {
                buf.append( "; Not picked up." );
            }
        }
        else {
            buf.append( " Not yet fulfilled." );
        }
        buf.append( "\n" );

        return buf.toString();
    }

    /**
     * Creates a string containing all order information
     *
     * @return a string containing all order information
     */
    public String toCustomerString () {
        final StringBuilder buf = new StringBuilder();
        buf.append( "Order of " );
        buf.append( recipe.getName() );
        buf.append( "." );

        if ( orderStatus ) {
            buf.append( " Fulfilled; " );
            if ( pickedUpStatus ) {
                buf.append( "Picked up." );
            }
            else {
                buf.append( "Not picked up." );
            }
        }
        else {
            buf.append( " Not yet fulfilled." );
        }
        buf.append( "\n" );

        return buf.toString();
    }

}
