package edu.ncsu.csc.CoffeeMaker;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateAccounts {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void testCreateAccounts () {
        userService.deleteAll();

        final Staff s1 = new Staff( "staff111", "123456pw" );

        userService.save( s1 );

        final Customer c1 = new Customer( "customer111", "123456pw" );

        userService.save( c1 );

        Assert.assertEquals( 2, userService.count() );

    }
}
