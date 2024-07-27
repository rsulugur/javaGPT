package com.ai.gpt.model;

import lombok.*;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class Product {
    private int id;
    private String productName;
    private String productPrice;
    private String productDescription;
}
