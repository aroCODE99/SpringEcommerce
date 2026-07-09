package com.aro.SpringEcommerce.Controllers;

import com.aro.SpringEcommerce.Entity.Product;
import com.aro.SpringEcommerce.Entity.ProductImage;
import com.aro.SpringEcommerce.Hateos.ProductModelAssembler;
import com.aro.SpringEcommerce.Service.EcommerceService;
import com.aro.SpringEcommerce.Storage.StorageService;
import jakarta.validation.Valid;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private EcommerceService ecommerceService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Product>> index() {
        List<EntityModel<Product>> products = ecommerceService.getProducts()
                .stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(products, linkTo(methodOn(ProductController.class).index()).withSelfRel());
    }

    @PostMapping
    public Product create(@RequestBody @Valid Product product){
        return ecommerceService.saveProduct(product);
    }

    @GetMapping("/{id}")
    public EntityModel<Product> view(@PathVariable("id") long id){
        return assembler.toModel(ecommerceService.getProduct(id));
    }

    @PostMapping(value = "/{id}")
    public Product edit(@PathVariable("id") long id, @RequestBody @Valid Product product){

        Product updatedProduct = ecommerceService.getProduct(id);

        if(updatedProduct == null){
            return null;
        }

        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setDescription(product.getDescription());

        return ecommerceService.saveProduct(updatedProduct);
    }

    @GetMapping("/{id}/images")
    public List<ProductImage> viewImages(@PathVariable("id") String productId){
        Session session = sessionFactory.openSession();
        List<ProductImage> list = session.createSelectionQuery("FROM ProductImage WHERE product_id = :product_id"
                , ProductImage.class)
                .setParameter(1, Long.parseLong(productId))
                .list();
        session.close();
        return list;
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("id") String id) {

        Session session = sessionFactory.openSession();
        ProductImage image = session.find(ProductImage.class, Long.parseLong(id));

        session.close();

        // Relative path to StorageProperties.rootLocation
        String path = "product-images/" + image.getProductId() + "/";

        Resource file = storageService.loadAsResource(path+image.getPath());
        String mimeType = "image/png";
        try {
            mimeType = file.getURL().openConnection().getContentType();
        } catch (IOException e){
            System.out.println("Can't get file mimeType. " + e.getMessage());
        }
        return ResponseEntity
                .ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(file);
    }

    @PostMapping("/{id}/uploadimage")
    public String handleFileUpload(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {

        // Relative path to the rootLocation in storageService
        String path = "/product-images/" + id;
        String filename = storageService.store(file, path);

        return ecommerceService.addProductImage(id, filename);
    }

    // Todo: add delete method

}