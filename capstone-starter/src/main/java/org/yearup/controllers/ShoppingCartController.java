package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.*;
import org.yearup.services.ProductService;
import org.yearup.services.ShoppingCartService;
import org.yearup.services.UserService;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;



// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartService shoppingCartService;
    private UserService userService;
    private ProductService productService;


    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    // each method in this controller requires a Principal object as a parameter
    public List<ShoppingCart> getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            Optional<User> user = userService.getByUserName(userName);
            int userId = user.get().getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartService.getAll();
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("products/{productId}")
    public ResponseEntity<String> addProduct(@PathVariable int productId, @RequestBody ShoppingCartItem item,
                                             Principal principal) {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find user by username
            Optional<User> optionalUser = userService.getByUserName(userName);
            if (!optionalUser.isPresent()) {
                throw new UserPrincipalNotFoundException("User not found");
            }

            User user = optionalUser.get();
            int userId = optionalUser.get().getId();

            //check product existence
            Optional<Product> optionalProduct = Optional.ofNullable(productService.getById(productId));
            if (!optionalProduct.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
            }

            // add product to user's shopping cart
            shoppingCartService.addProduct(userId, productId, item.getQuantity());
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added to cart");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the product");
        }

    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
