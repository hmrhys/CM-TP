package edu.ncsu.csc.CoffeeMaker.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername ( final String username ) throws UsernameNotFoundException {
        // Retrieve user entity from the data source based on the provided
        // username
        final User user = userRepository.findByUsername( username );

        // If user not found, throw UsernameNotFoundException
        if ( user == null ) {
            throw new UsernameNotFoundException( "User not found with username: " + username );
        }

        // Map user entity to UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username( user.getUsername() )
                .password( user.getPassword() ) // Ensure that password is
                                                // encoded properly
                .roles( user.getRoles().stream()
                        .map( Enum::name ) // Use enum constant names as role
                                           // names
                        .collect( Collectors.toList() )
                        .toArray( new String[0] ) )
                .build();
    }
}
