package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

/**
 * The UserService is used to handle CRUD operations on the User model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single User by name.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class UserService extends Service<User, Long> {

    /**
     * UserRepository, to be autowired in by Spring and provide CRUD operations
     * on User model.
     */
    @Autowired
    private UserRepository userRepository;

    @Override
    protected JpaRepository<User, Long> getRepository () {
        return userRepository;
    }

    /**
     * Find a User with the provided username
     *
     * @param username
     *            username of the User to find
     * @return found User, null if none
     */
    public User findByUsername ( final String username ) {
        return userRepository.findByUsername( username );
    }

}
