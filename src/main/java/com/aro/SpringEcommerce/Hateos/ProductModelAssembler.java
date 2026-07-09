package com.aro.SpringEcommerce.Hateos;

import com.aro.SpringEcommerce.Controllers.ProductController;
import com.aro.SpringEcommerce.Entity.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {
        return EntityModel.of(
                product,
                linkTo(methodOn(ProductController.class)
                        .view(product.getId()))
                        .withSelfRel(),
                linkTo(methodOn(ProductController.class)
                        .index())
                        .withRel("products")
        );
    }

}
