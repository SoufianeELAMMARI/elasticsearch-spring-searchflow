package org.demo.service;


import org.demo.exception.ProductAlreadyExistsException;
import org.demo.model.Product;
import org.demo.model.ProductReview;
import org.demo.model.Supplier;
import org.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return  StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        // Validate product doesn't already exist
        if (product.getName() != null && productRepository.existsByName(product.getName())) {
            throw new ProductAlreadyExistsException("Product with name '" + product.getName() + "' already exists");
        }

        // Set timestamps (if not using @PrePersist)
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(Instant.now());
        }
        product.setUpdatedAt(Instant.now());

        // Generate ID if not provided
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }

        // Process reviews if provided
        if (product.getReviews() != null && !product.getReviews().isEmpty()) {
            processReviews(product);
        }

        // Validate supplier if provided
        if (product.getSupplier() != null) {
            validateSupplier(product.getSupplier());
        }

        // Initialize computed fields
        initializeComputedFields(product);

        return productRepository.save(product);
    }


    public Product updateProduct(String id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> getProductsByCategoryAndMaxPrice(String category, Double maxPrice) {
        return productRepository.findByCategoryAndPriceLessThan(category, maxPrice);
    }

    private void processReviews(Product product) {
        for (ProductReview review : product.getReviews()) {
            // Generate review ID if not provided
            if (review.getId() == null || review.getId().isEmpty()) {
                review.setId(UUID.randomUUID().toString());
            }

            // Set review creation time if not provided
            if (review.getCreatedAt() == null) {
                review.setCreatedAt(LocalDateTime.now());
            }

            // Validate rating
            if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
                throw new IllegalArgumentException("Review rating must be between 1 and 5");
            }
        }

        // Update average rating and total reviews
        updateAverageRating(product);
    }

    private void validateSupplier(Supplier supplier) {
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier name is required");
        }

        if (supplier.getContactEmail() != null && !isValidEmail(supplier.getContactEmail())) {
            throw new IllegalArgumentException("Invalid supplier email format");
        }

        // Set default values
        if (supplier.getIsActive() == null) {
            supplier.setIsActive(true);
        }

        if (supplier.getRating() != null && (supplier.getRating() < 0 || supplier.getRating() > 5)) {
            throw new IllegalArgumentException("Supplier rating must be between 0 and 5");
        }
    }

    private void initializeComputedFields(Product product) {
        // Initialize average rating and total reviews if reviews exist
        if (product.getReviews() != null && !product.getReviews().isEmpty()) {
            updateAverageRating(product);
        } else {
            product.setAverageRating(0.0);
            product.setTotalReviews(0);
        }
    }

    private void updateAverageRating(Product product) {
        if (product.getReviews() != null && !product.getReviews().isEmpty()) {
            double sum = product.getReviews().stream()
                    .mapToDouble(ProductReview::getRating)
                    .sum();
            product.setAverageRating(sum / product.getReviews().size());
            product.setTotalReviews(product.getReviews().size());
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }


}