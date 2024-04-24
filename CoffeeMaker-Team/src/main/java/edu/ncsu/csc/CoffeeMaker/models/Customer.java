package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

/**
 * Customer for the coffee maker. Customer is tied to the database using
 * Hibernate libraries. See CustomerRepository and CustomerService for the other
 * two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Customer extends User {

    /**
     * Constructor for the customer class Empty class for hibernate
     */
    public Customer () {
        super();
    }

    /**
     * Constructor for a Customer object, references the super class
     *
     * @param username
     *            of the customer
     * @param password
     *            for the customer's account
     */
    public Customer ( final String username, final String password ) {
        super( username, password );
        super.setUserType( "customer" );
    }

}
