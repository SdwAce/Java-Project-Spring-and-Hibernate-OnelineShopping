package onlineShop.controller;

import onlineShop.model.Product;
import onlineShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    public ModelAndView getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ModelAndView("productList", "products", products);
    }

    @RequestMapping(value = "/getProductById/{productId}", method = RequestMethod.GET)
    public ModelAndView getProductById(@PathVariable(value = "productId") int productId) {
        Product product = productService.getProductById(productId);
        return new ModelAndView("productPage", "product", product);
    }

    @RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.GET)
    public ModelAndView getProductForm() {
        return new ModelAndView("addProduct", "productForm", new Product());
    }
    //follows the product model attribute is populated with data from a form submitted to the addProduct endpoint.
    @RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute(value = "productForm") Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "addProduct";
        }
        productService.addProduct(product);
        return "redirect:/getAllProducts";
    }

    @RequestMapping(value = "/admin/delete/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") int productId) {
        productService.deleteProduct(productId);
        return "redirect:/getAllProducts";
    }

    @RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.GET)
    public ModelAndView getEditForm(@PathVariable(value = "productId") int productId) {
        Product product = productService.getProductById(productId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editProduct");
        modelAndView.addObject("editProductObj", product);
        modelAndView.addObject("productId", productId);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.POST)
    public String editProduct(@ModelAttribute(value = "editProductObj") Product product,
                              @PathVariable(value = "productId") int productId) {
        product.setId(productId);
        productService.updateProduct(product);
        return "redirect:/getAllProducts";
    }
}

