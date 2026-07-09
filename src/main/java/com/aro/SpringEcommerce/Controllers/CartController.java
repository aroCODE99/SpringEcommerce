package com.aro.SpringEcommerce.Controllers;

import com.aro.SpringEcommerce.Cart.CartItem;
import com.aro.SpringEcommerce.Cart.CartService;
import com.aro.SpringEcommerce.Entity.Order;
import com.aro.SpringEcommerce.Hateos.OrderModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderModelAssembler assembler;

    @PostMapping
    public ResponseEntity<String> create() {
        return ResponseEntity.ok(cartService.createCart());
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> addProduct(@PathVariable("id") String cartId, @RequestBody CartItem cartItem) {
        cartService.addProduct(cartId, cartItem);
        System.out.println(cartItem.toString());
        return ResponseEntity.ok("Product added successfully");
    }


    @GetMapping("/{id}")
    public List<CartItem> getCartItems(@PathVariable("id") String cartId) {
        return cartService.getItems(cartId);
    }

    @DeleteMapping("{id}/{product_id}")
    public ResponseEntity<String> removeItem(@PathVariable("id") String cartId, @PathVariable("product_id") Long productId){
        cartService.removeProduct(cartId, productId);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("{id}/quantity")
    public ResponseEntity<String> setProductQuantity(@PathVariable("id") String cartId, @RequestBody CartItem cartItem) {
        cartService.setProductQuantity(cartId, cartItem);
        return ResponseEntity.ok("Product quantity added successfully");
    }

    @PostMapping("{id}/order")
    public EntityModel<Order> createOrder(@PathVariable("id") String cartId, @RequestBody  Order order) {
        if(order == null){
            System.out.println("Order not in POST");
            return null;
        }
        return assembler.toModel(cartService.createOrder(cartId, order));
    }

}
