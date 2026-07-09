package com.aro.SpringEcommerce.Cart;

import com.aro.SpringEcommerce.Entity.GroupVariant;
import com.aro.SpringEcommerce.Entity.Order;
import com.aro.SpringEcommerce.Entity.OrderItem;
import com.aro.SpringEcommerce.Entity.Product;
import com.aro.SpringEcommerce.Service.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String, Cart> template;

    @Autowired
    private EcommerceService ecommerceService;

    @Override
    public String createCart() {
        String id = UUID.randomUUID().toString();
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        template.opsForValue().set("carts::"+id, cart);
        return id;
    }

    @Override
    public void addProduct(String cartId, CartItem item) {
        String key = "carts::"+cartId;
        Cart cart = template.opsForValue().get(key); // first get Cart
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        cart.getItems().add(item);
        template.opsForValue().set(key, cart);
    }

    @Override
    public void removeProduct(String cartId, Long productId) {
        String key = "carts::"+cartId;
        Cart cart = template.opsForValue().get(key);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        // checking if we have product in the cart
        for (CartItem item : cart.getItems()) {
            if (!Objects.equals(item.getProductId(),  productId)) {
                throw new RuntimeException("Product not found in the Cart");
            }
        }
        cart.setItems(cart.getItems().stream()
                .filter(item -> item.getProductId() != productId)
                .toList());
        template.opsForValue().set(key, cart);
    }

    @Override
    public List<CartItem> getItems(String cartId) {
        String key = "carts::"+cartId;
        Cart cart = template.opsForValue().get(key);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        return cart.getItems();
    }

    @Override
    public void setProductQuantity(String cartId, CartItem cartItem) {
        String key = "carts::"+cartId;
        Cart cart = template.opsForValue().get(key);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }
        boolean updated = false;
        for (CartItem item : cart.getItems()) {
            if (Objects.equals(item.getProductId(), cartItem.getProductId())
                    && Objects.equals(item.getVariantId(), cartItem.getVariantId())) {
                item.setQuantity(cartItem.getQuantity());
                updated = true;
            }
        }
        if (!updated) {
            throw new RuntimeException("Product not found in the Cart");
        }
        template.opsForValue().set(key, cart);
    }

    @Override
    public Order createOrder(String cartId, Order order) {
        String key = "carts::" + cartId;
        List<CartItem> items = Objects.requireNonNull(template.opsForValue().get(key)).getItems();
        order = ecommerceService.saveOrder(addCartItemsToOrders(items, order));
        template.delete(key);
        return order;
    }

    private Order addCartItemsToOrders(List<CartItem> cartItems, Order order){

        cartItems.forEach(cartItem -> {

            Product prod = ecommerceService.getProduct(cartItem.getProductId());
            int qty = cartItem.getQuantity() > 0 ? cartItem.getQuantity() : 1;
            long variantId = cartItem.getVariantId();

            for(int i = 0; i < qty; i++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(prod);
                if(variantId > 0) {
                    GroupVariant v = new GroupVariant();
                    v.setId(variantId);
                    orderItem.setGroupVariant(v);
                }
                orderItem.setOrder(order);
                order.getItems().add(orderItem);
            }

        } );

        return order;
    }

}