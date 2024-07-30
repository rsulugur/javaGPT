package com.ai.gpt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "products")
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    private String productName;
    @Column(length = 250)
    private String productUrl;
    private Float productPrice;
    @Column(length = 2046)
    private String productDescription;
    private Date createdAt = new Date();

    @JsonIgnore
    public boolean isValidProduct() {
        return Objects.nonNull(this.productName) && Objects.nonNull(this.productPrice) && !this.productName.isEmpty();
    }
}
