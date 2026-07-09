package com.aro.SpringEcommerce.Cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Cart {

    @Getter
    @Setter
    private List<CartItem> items;

}
