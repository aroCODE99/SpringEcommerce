package com.aro.SpringEcommerce.Cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CartItem {

    private long productId;
    private long variantId;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        CartItem item = (CartItem) o;
        return item!= null && item.getProductId()==this.getProductId();
    }
}
