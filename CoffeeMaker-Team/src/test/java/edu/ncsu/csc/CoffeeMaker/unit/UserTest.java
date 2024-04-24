package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class UserTest {

    @Autowired
    private UserService     userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StaffService    staffService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        userService.deleteAll();
        customerService.deleteAll();
        staffService.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateCustomer () {
        final Customer c1 = new Customer( "username1", "pw1" );

        Assertions.assertEquals( "username1", c1.getUsername() );
        Assertions.assertEquals( "customer", c1.getUserType() );

        userService.save( c1 );

        Assert.assertEquals( 1, userService.count() );
    }

    @Test
    @Transactional
    public void testCreateStaff () {
        final Staff s1 = new Staff( "username1", "pw1" );

        Assertions.assertEquals( "username1", s1.getUsername() );
        Assertions.assertEquals( "staff", s1.getUserType() );

        userService.save( s1 );

        Assert.assertEquals( 1, userService.count() );
    }

    @Test
    @Transactional
    public void testCustomerToString () {
        final Customer c1 = new Customer( "username1", "pw1" );
        Assertions.assertEquals( "customer : username1", c1.toString() );
    }

    @Test
    @Transactional
    public void testStaffToString () {
        final Staff s1 = new Staff( "username1", "pw1" );
        Assertions.assertEquals( "staff : username1", s1.toString() );
    }

    @Test
    @Transactional
    public void testStaffAccountsCreation () {
        final Staff s1 = new Staff( "staff111", "123456Pw" );

        staffService.save( s1 );

        Assertions.assertEquals( "staff111", s1.getUsername() );
        Assertions.assertEquals( "staff", s1.getUserType() );

        final Staff s2 = new Staff( "staff222", "1234567Pw" );

        staffService.save( s2 );

        Assertions.assertEquals( "staff222", s2.getUsername() );
        Assertions.assertEquals( "staff", s2.getUserType() );

        Assert.assertEquals( 2, staffService.count() );
    }

    @Test
    @Transactional
    public void testCustomerAccountsCreation () {
        final Customer c1 = new Customer( "customer111", "123456Pw" );

        customerService.save( c1 );

        Assertions.assertEquals( "customer111", c1.getUsername() );
        Assertions.assertEquals( "customer", c1.getUserType() );

        final Customer c2 = new Customer( "customer222", "123456Pw" );

        customerService.save( c2 );

        Assertions.assertEquals( "customer222", c2.getUsername() );
        Assertions.assertEquals( "customer", c2.getUserType() );

        Assert.assertEquals( 2, customerService.count() );
    }

}
