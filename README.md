# Java Spring E-commerce

E-commerce REST API built with Java, Spring Boot, Hibernate ORM, MySQL, Spring HATEOAS, JWT, and Redis.

## REST API Endpoints

All requests and responses use JSON format.

```
/login
  POST / - Login using username: b and password: b

/product
  GET / - List of products
  POST / - Add product - required: String name, String groupId, String userId
  GET /{id} - View product
  POST /{id} - Update product
  GET /{id}/images - View product images
  GET /image/{id} - View image
  POST /{id}/uploadimage - Upload product image

/group
  GET / - List of groups
  POST / - Add group
  GET /{id} - View group
  POST /{id} - Update group

/order
  GET / - List of orders
  POST / - Add order
  GET /{id} - View order
  POST /{id} - Update order

/cart
  POST / - Create cart
  GET /{id} - Get items for cart with ID {id}
  POST /{id} - Add CartItem to cart with ID {id}
  DELETE /{id}/{product_id} - Remove product with ID {product_id} from cart with ID {id}
  POST /{id}/quantity - Update cart item quantity
  POST /{id}/order - Create an order from the cart
```
