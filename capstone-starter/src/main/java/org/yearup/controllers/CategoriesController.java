package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.services.CategoryService;
import org.yearup.services.ProductService;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryService categoryService;
    private ProductService productService;

    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }



    // add the appropriate annotation for a get action
    @GetMapping
    public ResponseEntity<List<Category>> getAll()
    {
        // find and return all categories
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // add the appropriate annotation for a get action
    @GetMapping("{id}")
    public ResponseEntity<Category> getById(@PathVariable int id)
    {
        // get the category by id
        Category categoryFound = categoryService.getById(id);

        if (categoryFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryFound);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        List<Product> productList = productService.listByCategoryId(categoryId);

        return ResponseEntity.ok(productList);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        // insert the category
        if (category.getCategoryId() != 0) {
            return ResponseEntity.badRequest().build();
        }

        Category newCategory = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable int categoryId, @RequestBody Category category)
    {
        // update the category by id
        Category category1 = categoryService.update(categoryId, category);
//        boolean success = categoryService.update(categoryId, category);

        if (category1 != null) {
            return ResponseEntity.ok(category1);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId)
    {
        // delete the category by id
       //boolean success =  categoryService.delete(categoryId);
        categoryService.delete(categoryId);
        Category category = categoryService.getById(categoryId);

        if (category != null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }

    }
}
