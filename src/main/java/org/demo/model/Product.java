package org.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = "products")
@Schema(description = "Product entity representing an item in the store")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    @NotBlank(message = "Product name is required")
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Double)
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Integer)
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant updatedAt;

    // Relation 1: Product Reviews (Nested - One-to-Many)
    @Field(type = FieldType.Nested)
    private List<ProductReview> reviews = new ArrayList<>();

    // Relation 2: Supplier Information (Object - Many-to-One)
    @Field(type = FieldType.Object)
    private Supplier supplier;

    @Field(type = FieldType.Double)
    private Double averageRating;

    @Field(type = FieldType.Integer)
    private Integer totalReviews;

    // Helper methods for relations
    public void addReview(ProductReview review) {
        this.reviews.add(review);
        updateAverageRating();
    }

    private void updateAverageRating() {
        if (reviews != null && !reviews.isEmpty()) {
            double sum = reviews.stream().mapToDouble(ProductReview::getRating).sum();
            this.averageRating = sum / reviews.size();
            this.totalReviews = reviews.size();
        }
    }

}