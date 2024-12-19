package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.*;
import org.yearup.services.ProductService;
import org.yearup.services.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;



// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserService userService;
    private ProductService productService;


    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserService userService, ProductService productService) {
        this.shoppingCartDao =shoppingCartDao;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    // each method in this controller requires a Principal object as a parameter
    public ResponseEntity<ShoppingCart> getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            Optional<User> user = userService.getByUserName(userName);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // use the shoppingCartService to get all items in the cart and return the cart
            int userId = user.get().getId();
            ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

            return ResponseEntity.ok(shoppingCart);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("products/{productId}")
    public ResponseEntity<ShoppingCart> addProduct(@PathVariable int productId,
                                             @RequestBody ShoppingCartItem item,
                                             Principal principal) {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find user by username
            Optional<User> optionalUser = userService.getByUserName(userName);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            User user = optionalUser.get();
            int userId = optionalUser.get().getId();

            //check product existence
            Optional<Product> optionalProduct = Optional.ofNullable(productService.getById(productId));
            if (optionalProduct.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // add product to user's shopping cart
            ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);
//            if (shoppingCart == null) {
//                shoppingCart = new ShoppingCart();
//                shoppingCart.setItems(new HashMap<>());
//            }
            if (shoppingCart.contains(productId)) {
                shoppingCartDao.updateQuantity(userId, productId, shoppingCart.getItems().get(productId).getQuantity()+1);
            } else {
                item.setProduct(optionalProduct.get());
                item.setQuantity(item.getQuantity()+1);

                shoppingCart.add(item);
                shoppingCartDao.save(shoppingCart, userId);
            }


            return ResponseEntity.status(HttpStatus.CREATED).body(shoppingCart);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
   public ResponseEntity<ShoppingCart> updateProduct(@PathVariable int productId,
                                               @RequestBody ShoppingCartItem item,
                                               Principal principal) {
        try {
            // validate quantity
            if (item.getQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // get the username
            String userName = principal.getName();
            // find user by username
            Optional<User> optionalUser = userService.getByUserName(userName);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            User user = optionalUser.get();
            int userId = user.getId();

            // check if product exists in the cart
            ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

            if (shoppingCart == null || !shoppingCart.contains(productId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }


            shoppingCartDao.updateQuantity(userId, productId, item.getQuantity());

            return ResponseEntity.status(HttpStatus.OK).body(shoppingCart);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    public ResponseEntity<ShoppingCart> clearCart(Principal principal) {
        try {
            // get users username
            String userName = principal.getName();

            // find user by username
            Optional<User> optionalUser = userService.getByUserName(userName);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            User user = optionalUser.get();
            int userId = user.getId();

            // clear products in the user's shopping cart
            shoppingCartDao.clearItems(userId);
            ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

            return  ResponseEntity.ok(shoppingCart);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
