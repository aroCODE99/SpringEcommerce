package com.aro.SpringEcommerce.Hateos;

import com.aro.SpringEcommerce.Controllers.OrderController;
import com.aro.SpringEcommerce.Entity.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order) {
        return EntityModel.of(
                order,
                linkTo(methodOn(OrderController.class)
                        .view(order.getId()))
                        .withSelfRel(),
                linkTo(methodOn(OrderController.class)
                        .index())
                        .withRel("orders")
        );
    }
}
