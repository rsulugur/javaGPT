package com.ai.gpt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
