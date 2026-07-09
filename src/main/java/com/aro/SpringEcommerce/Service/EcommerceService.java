package com.aro.SpringEcommerce.Service;

import com.aro.SpringEcommerce.Entity.Order;
import com.aro.SpringEcommerce.Entity.Product;
import com.aro.SpringEcommerce.Entity.ProductGroup;
import com.aro.SpringEcommerce.Entity.ProductImage;
import com.aro.SpringEcommerce.Repos.GroupRepo;
import com.aro.SpringEcommerce.Repos.OrderRepo;
import com.aro.SpringEcommerce.Repos.ProductRepo;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EcommerceService {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private GroupRepo groupRepository;

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private SessionFactory sessionFactory;


    /* PRODUCT */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    @Cacheable(value = "products", key = "#id")
    public Product getProduct(long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found with id " + id)
        );
    }
    @CachePut(value =  "products", key = "#result.id")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public String addProductImage(final String productId, final String filename) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductImage image = new ProductImage();
        image.setProductId(Long.parseLong(productId));
        image.setPath(filename);

        try {
            session.persist(image);
            session.getTransaction().commit();
            return "Product Image added successfully";
        } catch (HibernateException e) {
            System.out.print(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return "";
    }

    /* GROUPS */
    public List<ProductGroup> getGroups(){
        return groupRepository.findAll();
    }
    public ProductGroup getGroup(long id){
        return groupRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product group not found with id " + id)
        );
    }
    public ProductGroup saveGroup(ProductGroup group){
        return groupRepository.save(group);
    }

    /* ORDERS */
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }
    public Order getOrder(long id){
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found with id " + id)
        );
    }
    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }
}
