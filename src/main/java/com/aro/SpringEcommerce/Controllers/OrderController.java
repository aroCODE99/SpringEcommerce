package com.aro.SpringEcommerce.Controllers;

import com.aro.SpringEcommerce.Entity.Order;
import com.aro.SpringEcommerce.Hateos.OrderModelAssembler;
import com.aro.SpringEcommerce.Service.EcommerceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private EcommerceService ecommerceService;

    @Autowired
    private OrderModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Order>> index() {
        List<EntityModel<Order>> orders = ecommerceService.getOrders()
                .stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(
                orders,
                linkTo(methodOn(OrderController.class).index()).withSelfRel()
        );
    }

    @PostMapping
    public Order create(@RequestBody @Valid Order order){

        // Required by Hibernate ORM to save properly
        if(order.getItems() != null){
            order.getItems().forEach(item -> item.setOrder(order));
        }
        return ecommerceService.saveOrder(order);
    }

    @RequestMapping("/{id}")
    public EntityModel<Order> view(@PathVariable("id") long id){
        return assembler.toModel(ecommerceService.getOrder(id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Order edit(@PathVariable("id") long id, @RequestBody @Valid Order order){

        Order updatedOrder = ecommerceService.getOrder(id);

        if(updatedOrder== null){
            return null;
        }


        return ecommerceService.saveOrder(updatedOrder);
    }
}
