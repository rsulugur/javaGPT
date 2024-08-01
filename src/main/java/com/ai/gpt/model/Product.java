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

    private String name;

    @Column(length = 250)
    private String url;

    private Float price;

    private String source;

    private String logo;

    @Column(length = 2046)
    private String desc;

    private Date createdAt = new Date();

    @JsonIgnore
    public boolean isValidProduct() {
        return Objects.nonNull(this.name) && Objects.nonNull(this.price);
    }
}
