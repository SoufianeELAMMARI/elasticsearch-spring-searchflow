package org.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private String id;
    private String name;
    private String contactEmail;
    private String contactPhone;
    private String country;
    private Double rating;
    private Boolean isActive = true;
}