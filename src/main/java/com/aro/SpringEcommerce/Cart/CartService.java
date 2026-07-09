package com.aro.SpringEcommerce.Cart;

import com.aro.SpringEcommerce.Entity.Order;

import java.util.List;

public interface CartService {

    public String createCart();
    public void addProduct(String cartId, CartItem item);
    public void removeProduct(String cartId, Long productId);
    public List<CartItem> getItems(String cartId);
    public void setProductQuantity(String cartId, CartItem item);
    public Order createOrder(String cartId, Order order);
}
