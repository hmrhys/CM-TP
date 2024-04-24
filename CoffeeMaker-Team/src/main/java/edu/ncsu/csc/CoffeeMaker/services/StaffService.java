package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.repositories.StaffRepository;

/**
 * The StaffService is used to handle CRUD operations on the Staff model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Staff by name.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class StaffService extends Service<Staff, Long> {

    /**
     * PasswordEncoder, to be autowired in by Spring and provide password salt
     * security functionality on User authentication. This will ensure for a
     * safer user logging experience, and provide security for user data.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * StaffRepository, to be autowired in by Spring and provide CRUD operations
     * on Staff model.
     */
    @Autowired
    private StaffRepository staffRepository;

    @Override
    protected JpaRepository<Staff, Long> getRepository () {
        return staffRepository;
    }

    /**
     * Find a Staff with the provided username
     *
     * @param username
     *            username of the Staff to find
     * @return found Staff, null if none
     */
    public Staff findByUsername ( final String username ) {
        return staffRepository.findByUsername( username );
    }

    /**
     * Checks whether the User is in the database or not
     *
     * @param User
     *            the user to check
     */
    public boolean authenticateUser ( final String username, final String password ) {
        final Staff staff = staffRepository.findByUsername( username );

        return ( staff != null && passwordEncoder.matches( password, staff.getPassword() ) );
    }

}
