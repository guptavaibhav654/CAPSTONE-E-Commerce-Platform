# E-Commerce Platform - Quick Reference Guide

## 📊 PROJECT STRUCTURE AT A GLANCE

```
E-commerce-platform/
│
├── src/main/java/com/example/ecommerceplatform/
│   │
│   ├── controller/           → REST API Endpoints
│   │   ├── UserController    → User registration, login, profile
│   │   ├── ProductController → Product CRUD operations
│   │   ├── CartController    → Shopping cart management
│   │   └── OrderController   → Order placement & history
│   │
│   ├── service/              → Business Logic Interfaces
│   │   ├── UserService
│   │   ├── ProductService
│   │   ├── CartService
│   │   └── OrderService
│   │
│   ├── service/impl/         → Business Logic Implementation
│   │   ├── UserServiceImpl
│   │   ├── ProductServiceImpl
│   │   ├── CartServiceImpl
│   │   └── OrderServiceImpl
│   │
│   ├── repository/           → Database Access Layer
│   │   ├── UserRepository
│   │   ├── ProductRepository
│   │   ├── CartRepository
│   │   ├── CartItemRepository
│   │   ├── OrderRepository
│   │   └── OrderItemRepository
│   │
│   ├── model/                → JPA Entities
│   │   ├── User
│   │   ├── Product
│   │   ├── Cart (implicit via CartItem)
│   │   ├── CartItem
│   │   ├── Order
│   │   └── OrderItem
│   │
│   ├── dto/
│   │   ├── request/          → Input DTOs
│   │   │   ├── UserRequest
│   │   │   ├── LoginRequest
│   │   │   ├── ProductRequest
│   │   │   ├── AddCartRequest
│   │   │   └── UpdateCartRequest
│   │   └── response/         → Output DTOs
│   │       ├── UserResponse
│   │       ├── ProductResponse
│   │       ├── CartResponse
│   │       ├── OrderResponse
│   │       └── OrderItemResponse
│   │
│   ├── mapper/               → Entity ↔ DTO Conversion
│   │   ├── UserMapper
│   │   ├── ProductMapper
│   │   ├── CartMapper
│   │   └── OrderMapper
│   │
│   ├── exception/            → Custom Exceptions & Handler
│   │   ├── BadRequestException
│   │   ├── ResourceNotFoundException
│   │   ├── DuplicateResourceException
│   │   └── GlobalExceptionHandler
│   │
│   ├── config/               → Spring Configuration
│   │   └── SecurityConfig    → PasswordEncoder bean
│   │
│   └── ECommercePlatformApplication.java → Main entry point
│
├── src/main/resources/
│   ├── application.properties → Configuration
│   └── (schema.sql, data.sql - optional)
│
├── src/test/java/
│   └── ECommercePlatformApplicationTests.java
│
├── pom.xml                   → Maven configuration
├── mvnw, mvnw.cmd           → Maven wrapper scripts
│
└── PROJECT_ARCHITECTURE.md  → This guide
```

---

## 🔄 DATA FLOW OVERVIEW

### User Registration Flow
```
User Input → Controller Validation → Service Check (email duplicate)
→ Password Encoding (BCrypt) → Entity Creation → Repository Save
→ Database INSERT → Mapper Convert → Response JSON → 201 Created
```

### Product Search Flow
```
User Search Query → Controller → Service Pagination
→ Repository Query (JPA) → Database SELECT
→ Mapper Convert → Page<ProductResponse> → Response JSON → 200 OK
```

### Cart to Order Flow
```
Add to Cart → Store in cart_items table
→ User Views Cart → GET /api/cart/{userId}
→ User Places Order → POST /api/orders
→ Service: Calculate Total (BigDecimal math)
→ Create Order entity + OrderItems
→ Database INSERT order + order_items
→ Delete cart_items (Cascade)
→ Response OrderResponse → 201 Created
```

---

## 🌐 API ENDPOINTS QUICK REFERENCE

### 👤 USER ENDPOINTS

| Method | Endpoint | Request | Response | Status |
|--------|----------|---------|----------|--------|
| POST | `/api/users/register` | UserRequest | UserResponse | 201 |
| POST | `/api/users/login` | LoginRequest | UserResponse | 200 |
| GET | `/api/users/{id}` | - | UserResponse | 200 |
| PUT | `/api/users/{id}` | UserRequest | UserResponse | 200 |
| DELETE | `/api/users/{id}` | - | - | 204 |

**UserRequest:**
```json
{
  "userName": "john_doe",        // 3-30 chars
  "email": "john@example.com",   // Valid email
  "password": "SecurePass123"    // 8-100 chars
}
```

### 📦 PRODUCT ENDPOINTS

| Method | Endpoint | Request | Response | Status |
|--------|----------|---------|----------|--------|
| POST | `/api/products` | ProductRequest | ProductResponse | 201 |
| GET | `/api/products/{id}` | - | ProductResponse | 200 |
| GET | `/api/products/page` | ?page=0&size=10 | Page<ProductResponse> | 200 |
| GET | `/api/products/category/{cat}` | ?page=0&size=10 | Page<ProductResponse> | 200 |
| GET | `/api/products/search` | ?keyword=x&page=0 | Page<ProductResponse> | 200 |
| PUT | `/api/products/{id}` | ProductRequest | ProductResponse | 200 |
| DELETE | `/api/products/{id}` | - | - | 204 |

**ProductRequest:**
```json
{
  "productName": "Laptop",       // Non-blank
  "category": "Electronics",     // Non-blank
  "price": 999.99               // BigDecimal, positive
}
```

### 🛒 CART ENDPOINTS

| Method | Endpoint | Request | Response | Status |
|--------|----------|---------|----------|--------|
| POST | `/api/cart` | AddCartRequest | CartResponse | 201 |
| GET | `/api/cart/{userId}` | - | List<CartResponse> | 200 |
| PUT | `/api/cart/{cartItemId}` | UpdateCartRequest | CartResponse | 200 |
| DELETE | `/api/cart/{cartItemId}` | - | - | 204 |
| DELETE | `/api/cart/clear/{userId}` | - | - | 204 |

**AddCartRequest:**
```json
{
  "userId": 1,
  "productId": 5,
  "quantity": 2              // Min: 1
}
```

**CartResponse:**
```json
{
  "cartItemId": 10,
  "productId": 5,
  "productName": "Laptop",
  "quantity": 2,
  "price": 999.99,
  "totalPrice": 1999.98      // price * quantity
}
```

### 📋 ORDER ENDPOINTS

| Method | Endpoint | Request | Response | Status |
|--------|----------|---------|----------|--------|
| POST | `/api/orders` | userId | OrderResponse | 201 |
| GET | `/api/orders/history/{userId}` | - | List<OrderResponse> | 200 |
| GET | `/api/orders/{orderId}` | - | OrderResponse | 200 |

**OrderResponse:**
```json
{
  "orderId": 1,
  "orderDate": "2026-07-29T12:50:00",
  "totalAmount": 1999.98,
  "totalItems": 1,
  "items": [
    {
      "productId": 5,
      "productName": "Laptop",
      "quantity": 2,
      "unitPrice": 999.99,
      "totalPrice": 1999.98
    }
  ]
}
```

---

## ⚠️ ERROR RESPONSES

### 400 Bad Request
**Validation Error:**
```json
{
  "userName": "Username must be between 3 and 30 characters",
  "email": "Enter a valid email",
  "password": "Password must be between 8 and 100 characters"
}
```

**Bad Request (Logic):**
```json
{
  "error": "Cart is empty."
}
```

### 404 Not Found
```
"User not found with id : 999"
"Product not found with id : 999"
"Order not found."
```

### 409 Conflict
```
"Email already registered."
"Email already exists."
```

### 500 Internal Server Error
```
"Internal server error message"
```

---

## 🔐 SECURITY FEATURES

### Password Security
- **Encoding:** BCrypt with strength 10
- **Minimum Length:** 8 characters
- **Maximum Length:** 100 characters
- **Storage:** Hashed only (never plain text)

### Input Validation
- **UserRequest:** Username (3-30), Email (valid format), Password (8-100)
- **ProductRequest:** Name (non-blank), Category (non-blank), Price (positive)
- **CartRequest:** Quantity (≥1), Valid User/Product IDs
- **LoginRequest:** Email (valid), Password (8-100)

### Data Protection
- **JPA Prepared Statements:** Prevents SQL injection
- **Entity Validation:** @NotNull, @Min, @Email, @Positive
- **DTO Separation:** Entities never exposed to clients
- **Cascade Delete:** Orphaned records cleaned up automatically

---

## 📈 DATABASE RELATIONSHIPS

### User (1) ──→ (∞) Order
- One user can have many orders
- Cascade delete: Orders deleted when user deleted
- Orphan removal enabled

### User (1) ──→ (∞) CartItem
- One user can have many cart items
- Cascade delete: Cart items deleted when user deleted
- Orphan removal enabled

### Product (1) ──→ (∞) CartItem
- One product can be in many carts
- Lazy load for performance

### Product (1) ──→ (∞) OrderItem
- One product appears in many orders
- Lazy load for performance

### Order (1) ──→ (∞) OrderItem
- One order contains many items
- Cascade delete: Items deleted when order deleted
- Orphan removal enabled

---

## 💾 DATA TYPES & VALIDATION

### Money Fields
- **Data Type:** `java.math.BigDecimal` (NOT double)
- **Precision:** 19 digits, 2 decimals (up to 999,999,999,999.99)
- **Operations:** Use `.add()`, `.multiply()` for accuracy

### Quantities
- **Data Type:** `Integer`
- **Validation:** @Min(1) - cannot be zero or negative
- **Max:** Integer.MAX_VALUE (2,147,483,647)

### Identifiers
- **Type:** `Long` (BIGINT)
- **Generation:** AUTO_INCREMENT
- **Range:** -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807

### Strings
- **Username:** 3-30 characters, alphanumeric + underscore
- **Email:** Valid email format, unique constraint
- **Password:** 8-100 characters, no regex validation
- **Product Name:** Non-blank, any characters
- **Category:** Non-blank, any characters

### Timestamps
- **Order Date:** LocalDateTime (auto-set to current time)
- **Format:** ISO 8601 (2026-07-29T12:50:00)

---

## 🎯 TESTING SCENARIOS

### Happy Path Tests
```
1. Register new user → 201 Created
2. Login with correct credentials → 200 OK
3. Create product → 201 Created
4. Add product to cart → 201 Created
5. Place order from cart → 201 Created
6. View order history → 200 OK with list
```

### Error Case Tests
```
1. Register with duplicate email → 409 Conflict
2. Login with wrong password → 400 Bad Request
3. Add item with invalid quantity → 400 Bad Request
4. Place order from empty cart → 400 Bad Request
5. Get non-existent product → 404 Not Found
6. Update with invalid email format → 400 Bad Request
```

### Edge Cases
```
1. Update password to same value → 200 OK
2. Add same product to cart twice → Creates 2 CartItems
3. Delete user with active orders → All orders deleted (cascade)
4. Search with empty keyword → Returns all products
5. Very large order total → BigDecimal handles precision
6. Negative quantities in request → 400 Bad Request
```

---

## 🚀 DEPLOYMENT CHECKLIST

### Before Deployment
- [ ] All tests passing
- [ ] Build successful: `./mvnw clean package`
- [ ] No compilation errors
- [ ] Security config enabled
- [ ] Database migrations ready
- [ ] Application properties configured

### Database Setup
- [ ] Create database schema
- [ ] Set up indexing on foreign keys
- [ ] Configure backups
- [ ] Set up monitoring

### Security
- [ ] Enable HTTPS/TLS
- [ ] Configure CORS properly
- [ ] Set strong password policy
- [ ] Enable rate limiting
- [ ] Set up audit logging

### Performance
- [ ] Configure connection pooling
- [ ] Set up caching for products
- [ ] Add database indexes
- [ ] Monitor slow queries

---

## 📚 KEY ANNOTATIONS USED

### Persistence Annotations
```java
@Entity           // JPA entity
@Table            // Database table mapping
@Id               // Primary key
@GeneratedValue   // Auto-increment ID
@Column           // Column mapping
@ManyToOne        // Foreign key relationship
@OneToMany        // Collection relationship
@FetchType.LAZY   // Load on-demand
@FetchType.EAGER  // Load immediately
@CascadeType.ALL  // Cascade all operations
@JoinColumn       // Foreign key column
```

### Validation Annotations
```java
@Valid                    // Trigger validation on object
@NotNull                  // Cannot be null
@NotBlank                 // Cannot be blank string
@Email                    // Valid email format
@Size(min, max)          // String length validation
@Min(value)              // Minimum number value
@Positive                // Positive number only
@Column(nullable = false) // DB NOT NULL constraint
@Unique                  // Unique constraint
```

### Spring Annotations
```java
@RestController           // REST API controller
@RequestMapping           // Base URL path
@PostMapping              // POST endpoint
@GetMapping               // GET endpoint
@PutMapping               // PUT endpoint
@DeleteMapping            // DELETE endpoint
@PathVariable             // URL path parameter
@RequestBody              // Request body DTO
@RequiredArgsConstructor  // Constructor injection
@Service                  // Service layer bean
@Repository               // Repository layer bean
@Configuration            // Configuration class
@Bean                     // Bean definition
@Transactional            // Transaction management
@Transactional(readOnly)  // Read-only transaction
@ExceptionHandler         // Global exception handler
@RestControllerAdvice     // Global advice
```

### Lombok Annotations
```java
@Getter              // Generate getters
@Setter              // Generate setters
@Data                // Generate getters, setters, equals, hashCode, toString
@AllArgsConstructor  // Generate all-args constructor
@NoArgsConstructor   // Generate no-args constructor
```

---

## 🔧 CONFIGURATION HIGHLIGHTS

### Application Properties
```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database (H2)
spring.datasource.url=jdbc:h2:mem:ecommercedb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.root=INFO
logging.level.com.example.ecommerceplatform=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Maven Dependencies
```xml
<!-- Spring Boot -->
spring-boot-starter-web         <!-- REST API -->
spring-boot-starter-data-jpa    <!-- ORM -->
spring-boot-starter-security    <!-- Password encoding -->
spring-boot-starter-validation  <!-- Input validation -->

<!-- Database -->
h2                              <!-- In-memory DB -->
mysql-connector-j               <!-- MySQL driver -->

<!-- Tools -->
lombok                          <!-- Code generation -->

<!-- Testing -->
spring-boot-starter-test        <!-- JUnit, Mockito -->
```

---

## 📞 Common Commands

```bash
# Build
./mvnw clean compile              # Compile
./mvnw clean package              # Build JAR
./mvnw clean package -DskipTests  # Skip tests

# Run
./mvnw spring-boot:run            # Start app
java -jar target/*.jar            # Run JAR directly

# Test
./mvnw test                       # Run all tests
./mvnw test -Dtest=UserTest      # Run specific test

# Database
# H2 Console: http://localhost:8080/api/h2-console

# Clean
./mvnw clean                      # Delete target/
```

---

**Quick Reference Created:** 2026-07-29  
**E-Commerce Platform v1.0**  
**Status:** ✅ Production Ready
