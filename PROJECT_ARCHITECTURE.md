# E-Commerce Platform - Architecture Diagrams

## 1. SYSTEM ARCHITECTURE DIAGRAM

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                              │
│                   (Frontend / API Clients)                        │
└────────┬────────────────────────────────────────────────────────┘
         │ HTTP Requests (JSON)
         │
┌────────▼────────────────────────────────────────────────────────┐
│                      CONTROLLER LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │UserController│  │ProductControl│  │OrderControl  │           │
│  │(POST,GET,PUT)│  │(POST,GET,PUT)│  │(POST,GET)    │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│  ┌──────────────┐                                               │
│  │CartController│  Global Exception Handler                    │
│  │(POST,PUT,DEL)│  ├─ BadRequestException → 400               │
│  └──────────────┘  ├─ MethodArgumentNotValid → 400            │
│                    ├─ DuplicateResource → 409                 │
│                    ├─ ResourceNotFound → 404                  │
│                    └─ General Exception → 500                 │
└────────┬────────────────────────────────────────────────────────┘
         │ Service calls via dependency injection
         │
┌────────▼────────────────────────────────────────────────────────┐
│                       SERVICE LAYER                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │UserService   │  │ProductService│  │OrderService  │           │
│  │- register()  │  │- createProd()│  │- placeOrder()│           │
│  │- login()     │  │- getProducts │  │- getHistory()│           │
│  │- getUser()   │  │- getByCategory│ │- getOrder()  │           │
│  │- updateUser()│  │- searchProd()│  │              │           │
│  │- deleteUser()│  │- deleteProduct│ │              │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│  ┌──────────────┐                                               │
│  │CartService   │  @Transactional for data consistency         │
│  │- addToCart() │  Password encoding with BCryptPasswordEncoder│
│  │- getCart()   │  PasswordEncoder injected via SecurityConfig │
│  │- updateCart()│                                               │
│  │- removeItem()│                                               │
│  │- clearCart() │                                               │
│  └──────────────┘                                               │
└────────┬────────────────────────────────────────────────────────┘
         │ Repository pattern (Spring Data JPA)
         │
┌────────▼────────────────────────────────────────────────────────┐
│                     REPOSITORY LAYER                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │UserRepository│  │ProductRepository│ │OrderRepository│        │
│  │JpaRepository │  │JpaRepository │  │JpaRepository │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│  ┌──────────────┐  ┌──────────────┐                             │
│  │CartRepository│  │OrderItemRepo │                             │
│  │JpaRepository │  │JpaRepository │                             │
│  └──────────────┘  └──────────────┘                             │
└────────┬────────────────────────────────────────────────────────┘
         │ Entity queries and persistence
         │
┌────────▼────────────────────────────────────────────────────────┐
│                       DATA LAYER                                 │
│              H2 Database (In-Memory/File)                        │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Tables: users | products | orders | order_items |        │ │
│  │         cart_items | cartItem_product_fk                 │ │
│  │                                                            │ │
│  │ Indexes: user_id, product_id, category, email            │ │
│  └────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────┘
```

---

## 2. ENTITY RELATIONSHIP DIAGRAM

```
┌─────────────────────┐
│       USER          │
├─────────────────────┤
│ userId (PK)         │
│ userName            │
│ email (UNIQUE)      │
│ password (encoded)  │
├─────────────────────┤
│ 1 ──────────┬─────────────→ * (ORDERS)
│ 1 ──────────┬─────────────→ * (CART_ITEMS)
└─────────────────────┘
        │
        │ cascade: ALL, orphanRemoval: true
        │
        ├──────────────────────────┬──────────────────────────┐
        │                          │                          │
        ▼                          ▼                          ▼
┌─────────────────────┐   ┌──────────────────┐   ┌────────────────────┐
│      ORDER          │   │   CART_ITEM      │   │     PRODUCT        │
├─────────────────────┤   ├──────────────────┤   ├────────────────────┤
│ orderId (PK)        │   │ cartItemId (PK)  │   │ productId (PK)     │
│ userId (FK)    ◄────┼───│ userId (FK)      │   │ productName        │
│ orderDate           │   │ productId (FK)   ├───→ category           │
│ totalAmount (BD)    │   │ quantity (1-*)   │   │ price (BigDecimal) │
├─────────────────────┤   ├──────────────────┤   ├────────────────────┤
│ 1 ──────────┬──────────→ * (ORDER_ITEMS)   │   │ 1 ───┬─────→ * (ORDER_ITEMS)
│             │           │                  │   │     │
│             │           └──────────────────┘   │     │
│             │                                  │     │
│             └──────────────────────────────────┘     │
└─────────────────────┘                                │
        │                                              │
        │ cascade: ALL, orphanRemoval: true           │
        │                                              │
        ▼                                              │
┌─────────────────────────┐                           │
│    ORDER_ITEM           │                           │
├─────────────────────────┤                           │
│ orderItemId (PK)        │                           │
│ orderId (FK)      ◄─────┼───────────────────────────┘
│ productId (FK)    ◄─────────────────────────────────────┐
│ quantity (BD)           │                               │
│ price (BigDecimal)      │                               │
├─────────────────────────┤                               │
│ Many-to-One with Order  │                               │
│ Many-to-One with Product│                               │
└─────────────────────────┘


RELATIONSHIPS:
  User → Order (1:N) - cascade delete, orphan removal
  User → CartItem (1:N) - cascade delete, orphan removal
  Product → CartItem (1:N) - lazy load
  Product → OrderItem (1:N) - lazy load
  Order → OrderItem (1:N) - cascade delete, orphan removal

CONSTRAINTS:
  - Unique: User.email
  - NotNull: User (all), Product (all), CartItem.quantity, Order.user
  - Positive: Product.price
  - Min(1): CartItem.quantity, OrderItem.quantity
```

---

## 3. API ENDPOINT FLOW DIAGRAM

```
USER MANAGEMENT
├── POST /api/users/register
│   ├─ Input: UserRequest (userName, email, password:8-100chars)
│   ├─ Process: Check email duplicate → Hash password → Save user
│   └─ Output: UserResponse (userId, userName, email)
│
├── POST /api/users/login
│   ├─ Input: LoginRequest (email, password:8-100chars)
│   ├─ Process: Find user → Verify password → Return user
│   └─ Output: UserResponse
│
├── GET /api/users/{id}
│   ├─ Process: Find user by ID
│   └─ Output: UserResponse
│
├── PUT /api/users/{id}
│   ├─ Input: UserRequest (updated fields)
│   ├─ Process: Validate email uniqueness → Update user
│   └─ Output: UserResponse
│
└── DELETE /api/users/{id}
    └─ Process: Delete user + cascade delete orders & cart items

PRODUCT MANAGEMENT
├── POST /api/products (201 CREATED)
│   ├─ Input: ProductRequest (productName, category, price:BigDecimal)
│   ├─ Process: Save product
│   └─ Output: ProductResponse
│
├── GET /api/products/{id}
│   └─ Output: ProductResponse
│
├── GET /api/products/page?page=0&size=10
│   └─ Output: Page<ProductResponse>
│
├── GET /api/products/category/{cat}?page=0&size=10
│   ├─ Process: Find products by category (paginated)
│   └─ Output: Page<ProductResponse>
│
├── GET /api/products/search?keyword=keyword&page=0&size=10
│   ├─ Process: Search by product name (case-insensitive, paginated)
│   └─ Output: Page<ProductResponse>
│
├── PUT /api/products/{id}
│   ├─ Input: ProductRequest
│   └─ Output: ProductResponse
│
└── DELETE /api/products/{id}
    └─ Process: Delete product

CART MANAGEMENT
├── POST /api/cart (201 CREATED)
│   ├─ Input: AddCartRequest (userId, productId, quantity:1+)
│   ├─ Process: Validate quantity → Add to cart
│   └─ Output: CartResponse (cartItemId, productId, quantity, price, totalPrice)
│
├── GET /api/cart/{userId}
│   ├─ Process: Get all cart items for user
│   └─ Output: List<CartResponse>
│
├── PUT /api/cart/{cartItemId}
│   ├─ Input: UpdateCartRequest (quantity:1+)
│   ├─ Process: Update cart item quantity
│   └─ Output: CartResponse
│
├── DELETE /api/cart/{cartItemId}
│   └─ Process: Remove item from cart
│
└── DELETE /api/cart/clear/{userId}
    └─ Process: Clear all items from user's cart

ORDER MANAGEMENT
├── POST /api/orders (201 CREATED)
│   ├─ Input: Long userId
│   ├─ Process: Get cart items → Calculate total (BigDecimal) → Create order → Clear cart
│   ├─ Validation: Cart must not be empty
│   └─ Output: OrderResponse (orderId, orderDate, totalAmount, items)
│
├── GET /api/orders/history/{userId}
│   ├─ Process: Get all orders for user (ordered by date desc)
│   └─ Output: List<OrderResponse>
│
└── GET /api/orders/{orderId}
    └─ Output: OrderResponse


ERROR HANDLING:
├── 400 Bad Request
│   ├─ BadRequestException (invalid data)
│   └─ MethodArgumentNotValidException (validation failed)
├── 404 Not Found
│   └─ ResourceNotFoundException
├── 409 Conflict
│   └─ DuplicateResourceException
└── 500 Internal Server Error
    └─ Generic exceptions
```

---

## 4. DATA FLOW - NEW ORDER CREATION

```
User                Controller              Service              Repository        Database
 │                      │                       │                    │              │
 ├─ POST /orders ───────→│                       │                    │              │
 │  (userId=1)           │                       │                    │              │
 │                       ├─ placeOrder(1) ─────→│                    │              │
 │                       │                       ├─ find user ───────→│ Query user   │
 │                       │                       │◄─────────────user──┤              │
 │                       │                       │                    │              │
 │                       │                       ├─ get cart items ─→│ Query cart   │
 │                       │                       │◄── list<CartItem>─┤  items       │
 │                       │                       │                    │              │
 │                       │                       ├─ validate cart ────┤              │
 │                       │                       │ (not empty)         │              │
 │                       │                       │                    │              │
 │                       │                       ├─ calculate total ──┤              │
 │                       │                       │ (BigDecimal math)   │              │
 │                       │                       │                    │              │
 │                       │                       ├─ create order ────→│ INSERT       │
 │                       │                       │◄─── orderId ───────┤ ORDER       │
 │                       │                       │                    │              │
 │                       │                       ├─ for each item ───→│ INSERT       │
 │                       │                       │  create OrderItem  │ ORDER_ITEM  │
 │                       │                       │                    │              │
 │                       │                       ├─ delete cart ─────→│ DELETE      │
 │                       │                       │  (user's items)    │ CART_ITEMS  │
 │                       │                       │                    │              │
 │                       │◄─ OrderResponse ─────┤                    │              │
 │◄─ 201 + JSON ────────┤                       │                    │              │
 │                       │                       │                    │              │ Transaction
 │                       │                       │                    │              │ Committed
 │                       │                       │                    │              │
```

---

## 5. COMPONENT DEPENDENCY DIAGRAM

```
┌─────────────────────────────────────────────────────────────┐
│                  Spring Boot Application                     │
│  ECommercePlatformApplication.java (Main entry point)       │
└────────────────────┬────────────────────────────────────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
    ┌────────┐  ┌────────┐  ┌────────┐
    │Config  │  │Service │  │DTO    │
    │Layer   │  │ Layer  │  │ Layer  │
    └────────┘  └────────┘  └────────┘
        │            │            │
        ├────────────┼────────────┤
        │
┌───────▼──────────────────────────────────────────────────────┐
│              SecurityConfig                                   │
│  @Configuration                                              │
│  ├─ PasswordEncoder bean (BCryptPasswordEncoder)             │
│  └─ Used by: UserServiceImpl (password encoding/validation)  │
└───────┬──────────────────────────────────────────────────────┘
        │
        ├─────────────────┬──────────────────┬────────────────┐
        ▼                 ▼                  ▼                ▼
   ┌─────────┐      ┌──────────┐      ┌──────────┐      ┌──────────┐
   │UserSvc  │      │ProductSvc│      │OrderSvc  │      │CartSvc   │
   │Impl     │      │Impl      │      │Impl      │      │Impl      │
   └────┬────┘      └────┬─────┘      └────┬─────┘      └────┬─────┘
        │                │                  │                │
        ├─ Password      ├─ Mapper          ├─ BigDecimal    ├─ Cart
        │  Encoder       ├─ Repository      │  Calculations  │  Validation
        │                ├─ Validation      ├─ Transactions  ├─ Quantity
        └─ Exception     └─ Pagination      ├─ Exceptions    │  Min(1)
           Handling                         └─ Repository    └─ User
                                                Calls           Lookup


Mapper Components:
  ├─ UserMapper: Entity ↔ UserResponse
  ├─ ProductMapper: Entity ↔ ProductResponse
  ├─ CartMapper: Entity ↔ CartResponse (totalPrice calc)
  └─ OrderMapper: Entity ↔ OrderResponse (with items)

Exception Classes:
  ├─ BadRequestException (extends RuntimeException)
  ├─ ResourceNotFoundException (extends RuntimeException)
  ├─ DuplicateResourceException (extends RuntimeException)
  └─ GlobalExceptionHandler (@RestControllerAdvice)

DTO Package:
  ├─ Request DTOs: UserRequest, ProductRequest, LoginRequest,
  │                AddCartRequest, UpdateCartRequest
  └─ Response DTOs: UserResponse, ProductResponse, CartResponse,
                    OrderResponse, OrderItemResponse
```

---

## 6. TECHNOLOGY STACK

```
┌────────────────────────────────────────────────────────────┐
│                    Technology Stack                        │
├────────────────────────────────────────────────────────────┤
│                                                             │
│  Language & Framework:                                     │
│  ├─ Java 21                                               │
│  ├─ Spring Boot 4.1.0 (Starter Parent)                   │
│  ├─ Spring Web (REST API)                                │
│  └─ Spring Data JPA (ORM)                                │
│                                                             │
│  Security:                                                 │
│  ├─ Spring Security (Password encoding)                  │
│  ├─ BCrypt (Password hashing)                           │
│  └─ Input validation (Jakarta Validation)               │
│                                                             │
│  Database:                                                 │
│  ├─ H2 Database (In-memory for dev)                      │
│  ├─ H2 Console (Web-based DB browser)                    │
│  ├─ Hibernate (JPA implementation)                       │
│  └─ MySQL Driver (for production)                        │
│                                                             │
│  Development Tools:                                        │
│  ├─ Lombok (Reduce boilerplate)                         │
│  ├─ Maven (Build & dependency management)               │
│  ├─ Spring Boot DevTools (Hot reload)                   │
│  └─ JUnit 5 + Spring Boot Test                          │
│                                                             │
│  API & Communication:                                      │
│  ├─ REST API (HTTP/JSON)                                │
│  ├─ Jackson (JSON serialization)                         │
│  └─ Apache HTTP Client                                   │
│                                                             │
│  Build:                                                    │
│  ├─ Maven 3.x with wrapper                              │
│  ├─ Java Compiler Plugin                                │
│  └─ Spring Boot Maven Plugin                            │
│                                                             │
└────────────────────────────────────────────────────────────┘
```

---

## 7. REQUEST/RESPONSE FLOW EXAMPLE

```
=== USER REGISTRATION ===

REQUEST:
POST /api/users/register
Content-Type: application/json

{
  "userName": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123"
}

VALIDATION:
  ✓ userName: NotBlank, Size(3-30)
  ✓ email: NotBlank, Email format
  ✓ password: NotBlank, Size(8-100)

PROCESSING:
  1. UserController.createUser() receives request
  2. UserServiceImpl.register() called
  3. Check: Email not in database
  4. Create User entity
  5. Encode password: BCryptPasswordEncoder.encode()
  6. UserRepository.save()
  7. UserMapper.toResponse()

RESPONSE:
HTTP/1.1 201 CREATED
Content-Type: application/json

{
  "userId": 1,
  "userName": "john_doe",
  "email": "john@example.com"
}

NOTE: Password is not included in response


=== ADD TO CART ===

REQUEST:
POST /api/cart
Content-Type: application/json

{
  "userId": 1,
  "productId": 5,
  "quantity": 2
}

VALIDATION:
  ✓ userId: positive
  ✓ productId: positive
  ✓ quantity: positive, Min(1)

PROCESSING:
  1. CartController.addToCart()
  2. CartServiceImpl.addToCart()
  3. Validate user exists
  4. Validate product exists
  5. Validate quantity >= 1
  6. Create CartItem entity
  7. CartRepository.save()
  8. Calculate totalPrice = price * quantity (BigDecimal)
  9. CartMapper.toResponse()

RESPONSE:
HTTP/1.1 201 CREATED
Content-Type: application/json

{
  "cartItemId": 10,
  "productId": 5,
  "productName": "Laptop",
  "quantity": 2,
  "price": "999.99",
  "totalPrice": "1999.98"
}


=== ERROR HANDLING ===

REQUEST:
POST /api/cart
{
  "userId": 1,
  "productId": 999,
  "quantity": 2
}

PROCESSING:
  1. ProductRepository.findById(999) returns empty
  2. OrderServiceImpl throws ResourceNotFoundException
  3. GlobalExceptionHandler.handleResourceNotFound()

RESPONSE:
HTTP/1.1 404 NOT_FOUND
Content-Type: application/json

"Product not found with id : 999"


REQUEST:
POST /api/users/register
{
  "userName": "john",
  "email": "john@example.com",  ← Already exists
  "password": "SecurePass123"
}

PROCESSING:
  1. UserRepository.existsByEmail("john@example.com") → true
  2. UserServiceImpl throws DuplicateResourceException
  3. GlobalExceptionHandler.handleDuplicateResource()

RESPONSE:
HTTP/1.1 409 CONFLICT
Content-Type: application/json

"Email already registered."


REQUEST:
POST /api/users/register
{
  "userName": "j",        ← Too short
  "email": "john",        ← Invalid format
  "password": "short"     ← Too short
}

PROCESSING:
  1. @Valid validation fails on UserRequest
  2. MethodArgumentNotValidException thrown
  3. GlobalExceptionHandler.handleValidationException()

RESPONSE:
HTTP/1.1 400 BAD_REQUEST
Content-Type: application/json

{
  "userName": "Username must be between 3 and 30 characters",
  "email": "Enter a valid email",
  "password": "Password must be between 8 and 100 characters"
}
```

---

## 8. DATABASE SCHEMA OVERVIEW

```
Table: users
├─ userId (BIGINT, PK, AUTO_INCREMENT)
├─ userName (VARCHAR(255), NOT NULL)
├─ email (VARCHAR(255), NOT NULL, UNIQUE)
└─ password (VARCHAR(255), NOT NULL) [BCrypt encoded]

Table: products
├─ productId (BIGINT, PK, AUTO_INCREMENT)
├─ productName (VARCHAR(255), NOT NULL)
├─ category (VARCHAR(255), NOT NULL)
└─ price (DECIMAL(19,2), NOT NULL) [BigDecimal]

Table: orders
├─ orderId (BIGINT, PK, AUTO_INCREMENT)
├─ userId (BIGINT, FK → users.userId, NOT NULL)
├─ orderDate (TIMESTAMP, default: now())
└─ totalAmount (DECIMAL(19,2), NOT NULL) [BigDecimal]

Table: order_items
├─ orderItemId (BIGINT, PK, AUTO_INCREMENT)
├─ orderId (BIGINT, FK → orders.orderId, NOT NULL)
├─ productId (BIGINT, FK → products.productId, NOT NULL)
├─ quantity (INT, NOT NULL, MIN:1)
└─ price (DECIMAL(19,2), NOT NULL) [BigDecimal]

Table: cart_items
├─ cartItemId (BIGINT, PK, AUTO_INCREMENT)
├─ userId (BIGINT, FK → users.userId, NOT NULL)
├─ productId (BIGINT, FK → products.productId, NOT NULL)
└─ quantity (INT, NOT NULL, MIN:1)

Indexes (Performance):
├─ users.email (UNIQUE)
├─ products.category
├─ orders.userId
├─ orders.orderDate
├─ order_items.orderId
├─ order_items.productId
├─ cart_items.userId
└─ cart_items.productId
```

---

**Diagrams created on:** 2026-07-29  
**E-Commerce Platform v1.0**
