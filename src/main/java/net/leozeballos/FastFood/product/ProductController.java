package net.leozeballos.FastFood.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/product/{id}", produces = "application/json")
    public @ResponseBody Product getProduct(@PathVariable("id") Long id) {
        return productService.findById(id);
    }

    @RequestMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return "redirect:/product/list";
    }

    @RequestMapping("/product/disable/{id}")
    public String disableProduct(@PathVariable("id") Long id) {
        productService.disableItem(id);
        return "redirect:/product/list";
    }

    @RequestMapping("/product/enable/{id}")
    public String enableProduct(@PathVariable("id") Long id) {
        productService.enableItem(id);
        return "redirect:/product/list";
    }

    @RequestMapping("/product/edit/{id}")
    public ModelAndView editProduct(@PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("product/edit_product");
        mav.addObject("product", productService.findById(id));
        mav.addObject("pageTitle", "Edit Product");
        return mav;
    }

    @RequestMapping("/product/new")
    public String newProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "New Product");
        return "product/new_product";
    }

    @RequestMapping("/product/save")
    public String saveProduct(@ModelAttribute("product") Product product) throws RuntimeException {
        if (productService.save(product) != null) {
            return "redirect:/product/list";
        } else {
            throw new RuntimeException("Error saving product");
        }
    }

    @RequestMapping("/product/list")
    public String listProducts(Model model) {
        model.addAttribute("listProducts", productService.findAll());
        model.addAttribute("pageTitle", "Products List");
        return "product/list_product";
    }

}
