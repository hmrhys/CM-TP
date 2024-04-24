package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DebugController {

    @GetMapping ( "/debug" )
    public String debug ( final Model model ) {
        final String debugMessage = "This is a debug message.";
        model.addAttribute( "debugMessage", debugMessage );
        return "debug"; // Assuming you have a debug.html template
    }
}
