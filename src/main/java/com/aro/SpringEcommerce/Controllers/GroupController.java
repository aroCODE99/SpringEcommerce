package com.aro.SpringEcommerce.Controllers;

import com.aro.SpringEcommerce.Entity.ProductGroup;
import com.aro.SpringEcommerce.Hateos.GroupModelAssembler;
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
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private EcommerceService ecommerceService;

    @Autowired
    private GroupModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<ProductGroup>> index() {
        List<EntityModel<ProductGroup>> groups = ecommerceService.getGroups()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(
                groups,
                linkTo(methodOn(GroupController.class)
                        .index()
                ).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<ProductGroup> view(@PathVariable("id") long id){
        return assembler.toModel(ecommerceService.getGroup(id));
    }

    @PostMapping("/{id}")
    public ProductGroup edit(@PathVariable(value = "id", required = false) long id, @RequestBody @Valid ProductGroup group){

        ProductGroup updatedGroup = ecommerceService.getGroup(id);

        if(updatedGroup == null){
            return null;
        }

        updatedGroup.setGroupName(group.getGroupName());
        updatedGroup.setGroupVariants(group.getGroupVariants());

        // We must do this manually b/c of Hibernate.
        if(updatedGroup.getGroupVariants() !=null ){
            updatedGroup.getGroupVariants().forEach(gv -> gv.setGroup(updatedGroup));
        }

        return ecommerceService.saveGroup(updatedGroup);
    }

    @PostMapping
    public ProductGroup create(@RequestBody @Valid ProductGroup group){

        // We must do this manually b/c of Hibernate.
        if( group.getGroupVariants() != null ) {
            group.getGroupVariants().forEach(gv -> gv.setGroup(group));
        }

        return ecommerceService.saveGroup(group);
    }

    // Todo: add delete method

}