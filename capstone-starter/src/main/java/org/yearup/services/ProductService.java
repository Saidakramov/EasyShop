package org.yearup.services;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.yearup.models.Product;
import org.yearup.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color) {

        // Maaike helped
        List<Product> products = productRepository.findAll();
        if(categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        }
        if(minPrice != null) {
            List<Product> tempProducts = productRepository.findByPriceGreaterThan(minPrice);
            products.retainAll(tempProducts);
        }
        if(maxPrice != null) {
            List<Product> tempProducts = productRepository.findByPriceLessThan(maxPrice);
            products.retainAll(tempProducts);
        }
        if(color != null) {
            List<Product> tempProducts = productRepository.findByColor(color);
            products.retainAll(tempProducts);
        }
        return products;
    }

    public List<Product> listByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getById(int productId) {
        Optional<Product> optionalProduct =productRepository.findById(productId);

        return optionalProduct.orElse(null);
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public void update(int productId, Product product) {
        Product productToBeUpdated = getById(productId);

        if (productToBeUpdated != null) {
            productToBeUpdated = new Product(product.getProductId(), product.getName(), product.getPrice(),
                    product.getCategoryId(), product.getDescription(), product.getColor(), product.getStock(),
                    product.isFeatured(), product.getImageUrl());

            productRepository.save(productToBeUpdated);
        }
    }
    public void delete(int productId) {
        productRepository.deleteById(productId);
    }
}
