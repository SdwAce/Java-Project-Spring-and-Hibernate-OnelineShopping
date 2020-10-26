package onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import onlineShop.model.Customer;
import onlineShop.service.CustomerService;

@Controller
public class RegistrationController {
    //inject customerservice
    @Autowired
    private CustomerService customerService;
    //map URL to the getRegistrationForm method
    @RequestMapping(value = "/customer/registration", method = RequestMethod.GET)
    public ModelAndView getRegistrationForm() {
        //initialize a customer instance
        Customer customer = new Customer();
        //create new modelandview instance,with viewName,modelName and modelObject (the customer object we just create)
        return new ModelAndView("register", "customer", customer);
    }

    @RequestMapping(value = "/customer/registration", method = RequestMethod.POST)
    //map custoemr info in form to customer object
    public ModelAndView registerCustomer(@ModelAttribute(value = "customer") Customer customer,
                                         BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("register");
            return modelAndView;
        }
        //implement addCustomer method in customerService
        customerService.addCustomer(customer);
        modelAndView.setViewName("login");
        //Add an attribute to the model, as return result
        modelAndView.addObject("registrationSuccess", "Registered Successfully. Login using username and password");
        return modelAndView;
    }
}

