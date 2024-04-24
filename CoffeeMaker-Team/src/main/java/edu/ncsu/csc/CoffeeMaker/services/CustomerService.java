package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;

/**
 * The CustomerService is used to handle CRUD operations on the Customer model.
 * In addition to all functionality from `Service`, we also have functionality
 * for retrieving a single Customer by name.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class CustomerService extends Service<Customer, Long> {

    /**
     * PasswordEncoder, to be autowired in by Spring and provide password salt
     * security functionality on User authentication. This will ensure for a
     * safer user logging experience, and provide security for user data.
     */
    @Autowired
    private PasswordEncoder    passwordEncoder;

    /**
     * CustomerRepository, to be autowired in by Spring and provide CRUD
     * operations on Customer model.
     */
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    protected JpaRepository<Customer, Long> getRepository () {
        return customerRepository;
    }

    public long countGuests () {
        return customerRepository.countByRolesContaining( Role.GUEST );
    }

    /**
     * Find a Customer with the provided username
     *
     * @param username
     *            username of the Customer to find
     * @return found Customer, null if none
     */
    public Customer findByUsername ( final String username ) {
        return customerRepository.findByUsername( username );
    }

    /**
     * Checks whether the User is in the database or not
     *
     * @param User
     *            the user to check
     */
    public boolean authenticateUser ( final String username, final String password ) {
        final Customer customer = customerRepository.findByUsername( username );

        return ( customer != null && passwordEncoder.matches( password, customer.getPassword() ) );
    }
}
