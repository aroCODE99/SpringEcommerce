package com.aro.SpringEcommerce.Hateos;

import com.aro.SpringEcommerce.Controllers.GroupController;
import com.aro.SpringEcommerce.Controllers.ProductController;
import com.aro.SpringEcommerce.Entity.ProductGroup;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GroupModelAssembler implements RepresentationModelAssembler<ProductGroup, EntityModel<ProductGroup>> {

    @Override
    public EntityModel<ProductGroup> toModel(ProductGroup group) {
        return EntityModel.of(
                group,
                linkTo(methodOn(GroupController.class)
                        .view(group.getId())
                ).withSelfRel(),
                linkTo(methodOn(ProductController.class)
                        .index()).withRel("groups")
        );
    }
}
