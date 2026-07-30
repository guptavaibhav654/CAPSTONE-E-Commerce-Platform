# E-Commerce Platform - Visual Mermaid Diagrams

## 1. SYSTEM ARCHITECTURE (Layer Diagram)

```mermaid
graph TD
    Client["🌐 Client<br/>Frontend/Mobile API"]
    
    Client -->|HTTP JSON| Controller["🎮 Controller Layer<br/>REST Endpoints"]
    
    Controller -->|DI| Service["⚙️ Service Layer<br/>Business Logic"]
    
    Service -->|Method Calls| Mapper["🗺️ Mapper Layer<br/>DTO ↔ Entity"]
    
    Service -->|JPA Queries| Repository["📚 Repository Layer<br/>Data Access"]
    
    Repository -->|SQL| Database["🗄️ Database<br/>H2/MySQL"]
    
    Service -->|Exception Handling| ExceptionHandler["⚠️ Global Exception Handler<br/>Error Responses"]
    
    ExceptionHandler -->|HTTP Errors| Client
    
    Repository -->|Entity| Service
    
    style Client fill:#e1f5ff
    style Controller fill:#fff3e0
    style Service fill:#f3e5f5
    style Mapper fill:#fce4ec
    style Repository fill:#e8f5e9
    style Database fill:#fff9c4
    style ExceptionHandler fill:#ffebee
```

## 2. ENTITY RELATIONSHIP DIAGRAM

```mermaid
erDiagram
    USER ||--o{ ORDER : creates
    USER ||--o{ CART_ITEM : manages
    PRODUCT ||--o{ CART_ITEM : stored-in
    PRODUCT ||--o{ ORDER_ITEM : contains
    ORDER ||--o{ ORDER_ITEM : includes

    USER {
        bigint userId PK
        string userName
        string email UK "Unique"
        string password "BCrypt encoded"
    }

    PRODUCT {
        bigint productId PK
        string productName
        string category
        decimal price "BigDecimal"
    }

    CART_ITEM {
        bigint cartItemId PK
        bigint userId FK
        bigint productId FK
        integer quantity "Min: 1"
    }

    ORDER {
        bigint orderId PK
        bigint userId FK
        datetime orderDate
        decimal totalAmount "BigDecimal"
    }

    ORDER_ITEM {
        bigint orderItemId PK
        bigint orderId FK
        bigint productId FK
        integer quantity "Min: 1"
        decimal price "BigDecimal"
    }
```

## 3. CONTROLLER-SERVICE-REPOSITORY FLOW

```mermaid
graph LR
    subgraph Controllers
        UC["UserController<br/>POST,GET,PUT,DELETE"]
        PC["ProductController<br/>POST,GET,PUT,DELETE"]
        OC["OrderController<br/>POST,GET"]
        CC["CartController<br/>POST,GET,PUT,DELETE"]
    end

    subgraph Services
        US["UserService<br/>register, login<br/>getUser, updateUser"]
        PS["ProductService<br/>createProduct<br/>searchProducts"]
        OS["OrderService<br/>placeOrder<br/>getOrderHistory"]
        CS["CartService<br/>addToCart<br/>updateCart"]
    end

    subgraph Repositories
        UR["UserRepository<br/>findByEmail"]
        PR["ProductRepository<br/>findByCategory<br/>findByProductName"]
        OR["OrderRepository<br/>findByUser<br/>findById"]
        CR["CartRepository<br/>findByUser<br/>deleteAll"]
    end

    UC -->|@Autowired| US
    PC -->|@Autowired| PS
    OC -->|@Autowired| OS
    CC -->|@Autowired| CS

    US -->|@Autowired| UR
    PS -->|@Autowired| PR
    OS -->|@Autowired| OR
    CS -->|@Autowired| CR

    UR -.->|Extends| JPARepo["JpaRepository"]
    PR -.->|Extends| JPARepo
    OR -.->|Extends| JPARepo
    CR -.->|Extends| JPARepo

    JPARepo -->|Uses| Database["H2 Database"]
```

## 4. API ENDPOINT TREE

```mermaid
graph TD
    API["🚀 /api"]
    
    API --> Users["👤 /users"]
    API --> Products["📦 /products"]
    API --> Orders["📋 /orders"]
    API --> Cart["🛒 /cart"]
    
    Users --> UPost["POST /register<br/>UserRequest → UserResponse<br/>201 Created"]
    Users --> UPost2["POST /login<br/>LoginRequest → UserResponse<br/>200 OK"]
    Users --> UGet["GET /{id}<br/>→ UserResponse<br/>200 OK"]
    Users --> UPut["PUT /{id}<br/>UserRequest → UserResponse<br/>200 OK"]
    Users --> UDel["DELETE /{id}<br/>→ 204 No Content"]
    
    Products --> PPost["POST<br/>ProductRequest → ProductResponse<br/>201 Created"]
    Products --> PGet["GET /{id}<br/>→ ProductResponse<br/>200 OK"]
    Products --> PList["GET /page?page=0&size=10<br/>→ Page<ProductResponse><br/>200 OK"]
    Products --> PCategory["GET /category/{cat}?page=0<br/>→ Page<ProductResponse><br/>200 OK"]
    Products --> PSearch["GET /search?keyword=x<br/>→ Page<ProductResponse><br/>200 OK"]
    Products --> PPut["PUT /{id}<br/>ProductRequest → ProductResponse<br/>200 OK"]
    Products --> PDel["DELETE /{id}<br/>→ 204 No Content"]
    
    Orders --> OPost["POST<br/>userId → OrderResponse<br/>201 Created"]
    Orders --> OHistory["GET /history/{userId}<br/>→ List<OrderResponse><br/>200 OK"]
    Orders --> OGet["GET /{orderId}<br/>→ OrderResponse<br/>200 OK"]
    
    Cart --> CPost["POST<br/>AddCartRequest → CartResponse<br/>201 Created"]
    Cart --> CGet["GET /{userId}<br/>→ List<CartResponse><br/>200 OK"]
    Cart --> CPut["PUT /{cartItemId}<br/>UpdateCartRequest → CartResponse<br/>200 OK"]
    Cart --> CDel["DELETE /{cartItemId}<br/>→ 204 No Content"]
    Cart --> CClear["DELETE /clear/{userId}<br/>→ 204 No Content"]
    
    style API fill:#1976d2,color:#fff
    style Users fill:#388e3c,color:#fff
    style Products fill:#d32f2f,color:#fff
    style Orders fill:#f57c00,color:#fff
    style Cart fill:#7b1fa2,color:#fff
```

## 5. ORDER CREATION DATA FLOW

```mermaid
sequenceDiagram
    participant User
    participant Controller as CartController
    participant Service as CartService
    participant Mapper as CartMapper
    participant OrderSvc as OrderService
    participant Repo as Repository
    participant DB as Database

    User->>Controller: POST /orders {userId}
    
    activate Controller
    Controller->>Service: placeOrder(userId)
    deactivate Controller
    
    activate Service
    Service->>Repo: findById(userId)
    Repo->>DB: SELECT * FROM users
    DB-->>Repo: User entity
    Repo-->>Service: User
    
    Service->>Repo: findByUser(user)
    Repo->>DB: SELECT * FROM cart_items
    DB-->>Repo: List<CartItem>
    Repo-->>Service: List<CartItem>
    
    Service->>Service: Validate cart not empty
    Service->>Service: Calculate totalAmount (BigDecimal)
    
    Service->>Repo: save(order)
    Repo->>DB: INSERT INTO orders
    DB-->>Repo: Order with ID
    Repo-->>Service: Saved Order
    
    Service->>Repo: save(orderItems)
    Repo->>DB: INSERT INTO order_items
    DB-->>Repo: OrderItems
    Repo-->>Service: List<OrderItem>
    
    Service->>Repo: deleteAll(cartItems)
    Repo->>DB: DELETE FROM cart_items
    DB-->>Repo: Deleted
    Repo-->>Service: Success
    
    Service->>Mapper: toResponse(order)
    Mapper-->>Service: OrderResponse
    
    Service-->>Controller: OrderResponse
    activate Controller
    Controller-->>User: 201 CREATED + JSON
    deactivate Controller
    deactivate Service
```

## 6. PASSWORD ENCODING FLOW

```mermaid
graph TD
    A["User Registration<br/>password: 'MySecure@123'"] -->|POST /register| B["UserController"]
    
    B -->|Call| C["UserService.register()"]
    
    C -->|Inject| D["SecurityConfig<br/>PasswordEncoder bean"]
    
    D -->|BCryptPasswordEncoder| E["Encode Password"]
    
    E -->|Hash: $2a$10$...| F["Encoded: '$2a$10$xXxXxXxXxXx'"]
    
    F -->|Save with| C
    
    C -->|UserRepository.save| G["Database<br/>users table"]
    
    G -->|"password: '$2a$10$xXxXxXxXxXx'"| H["Stored (Secure)"]
    
    I["User Login<br/>password: 'MySecure@123'"] -->|POST /login| J["UserController"]
    
    J -->|Call| K["UserService.login()"]
    
    K -->|Get User| L["UserRepository.findByEmail()"]
    
    L -->|Retrieve| M["User entity<br/>password: '$2a$10$...'"]
    
    M -->|Inject| N["SecurityConfig<br/>PasswordEncoder bean"]
    
    N -->|BCrypt.matches| O["Compare<br/>Input vs Hash"]
    
    O -->|Match: TRUE| P["Login Success<br/>Return UserResponse"]
    
    O -->|Match: FALSE| Q["BadRequestException<br/>Invalid credentials"]
    
    P -->|200 OK| R["Response to Client"]
    Q -->|400 Bad Request| R
    
    style A fill:#e3f2fd
    style E fill:#fff3e0
    style F fill:#f3e5f5
    style H fill:#c8e6c9
    style M fill:#c8e6c9
    style O fill:#fff9c4
    style P fill:#c8e6c9
    style Q fill:#ffcdd2
```

## 7. EXCEPTION HANDLING FLOW

```mermaid
graph TD
    Request["Incoming Request"]
    
    Request --> Controller["Controller<br/>@Valid Validation"]
    
    Controller -->|Validation Fails| ValError["MethodArgumentNotValidException"]
    ValError --> ExceptionHandler["GlobalExceptionHandler"]
    ExceptionHandler -->|Handle| ValHandler["handleValidationException()"]
    ValHandler --> Val400["400 Bad Request<br/>Field errors map"]
    Val400 --> Client
    
    Controller -->|Proceed| Service["Service Layer"]
    
    Service --> LogicCheck{"Business Logic<br/>Validation"}
    
    LogicCheck -->|User not found| NotFound["ResourceNotFoundException"]
    NotFound --> ExceptionHandler
    ExceptionHandler -->|Handle| NotHandler["handleResourceNotFound()"]
    NotHandler --> Not404["404 Not Found"]
    Not404 --> Client
    
    LogicCheck -->|Email exists| Duplicate["DuplicateResourceException"]
    Duplicate --> ExceptionHandler
    ExceptionHandler -->|Handle| DupHandler["handleDuplicateResource()"]
    DupHandler --> Dup409["409 Conflict"]
    Dup409 --> Client
    
    LogicCheck -->|Bad request data| BadRequest["BadRequestException"]
    BadRequest --> ExceptionHandler
    ExceptionHandler -->|Handle| BadHandler["handleBadRequestException()"]
    BadHandler --> Bad400["400 Bad Request"]
    Bad400 --> Client
    
    LogicCheck -->|Unexpected error| Generic["Exception"]
    Generic --> ExceptionHandler
    ExceptionHandler -->|Handle| GenHandler["handleException()"]
    GenHandler --> Gen500["500 Internal Server Error"]
    Gen500 --> Client
    
    LogicCheck -->|Success| Success["Response Entity"]
    Success --> Client["Client Response"]
    
    style Request fill:#e1f5ff
    style ValError fill:#ffcdd2
    style NotFound fill:#ffcdd2
    style Duplicate fill:#ffcdd2
    style BadRequest fill:#ffcdd2
    style Generic fill:#ffcdd2
    style ExceptionHandler fill:#fff3e0
    style Val400 fill:#ffebee
    style Not404 fill:#ffebee
    style Dup409 fill:#ffebee
    style Bad400 fill:#ffebee
    style Gen500 fill:#ffebee
    style Success fill:#c8e6c9
    style Client fill:#e0f2f1
```

## 8. TECHNOLOGY STACK VISUALIZATION

```mermaid
graph TD
    A["🚀 E-Commerce Platform"]
    
    A --> Backend["Backend Framework"]
    Backend --> Java["Java 21"]
    Backend --> SpringBoot["Spring Boot 4.1.0"]
    SpringBoot --> Web["Spring Web"]
    SpringBoot --> JPA["Spring Data JPA"]
    SpringBoot --> Security["Spring Security"]
    SpringBoot --> Validation["Spring Validation"]
    
    A --> Database["Database Layer"]
    Database --> H2["H2 Database<br/>In-Memory"]
    Database --> MySQL["MySQL<br/>Production"]
    Database --> Hibernate["Hibernate ORM"]
    
    A --> Libraries["Core Libraries"]
    Libraries --> Lombok["Lombok<br/>Code Generation"]
    Libraries --> Jackson["Jackson<br/>JSON Processing"]
    Libraries --> JUnit["JUnit 5<br/>Testing"]
    
    A --> Build["Build & Deployment"]
    Build --> Maven["Maven 3.x"]
    Build --> Dependencies["Dependency Management"]
    
    A --> Security2["Security"]
    Security2 --> BCrypt["BCrypt<br/>Password Hashing"]
    Security2 --> InputVal["Input Validation<br/>Jakarta Validation"]
    
    style A fill:#1976d2,color:#fff,stroke:#0d47a1,stroke-width:3px
    style Backend fill:#388e3c,color:#fff
    style Database fill:#d32f2f,color:#fff
    style Libraries fill:#f57c00,color:#fff
    style Build fill:#7b1fa2,color:#fff
    style Security2 fill:#c2185b,color:#fff
    style Java fill:#fff3e0
    style SpringBoot fill:#fff3e0
    style H2 fill:#c8e6c9
    style MySQL fill:#c8e6c9
    style Lombok fill:#b2dfdb
    style Jackson fill:#b2dfdb
    style JUnit fill:#b2dfdb
    style BCrypt fill:#ffccbc
    style InputVal fill:#ffccbc
```

---

**Visual diagrams generated on:** 2026-07-29  
**Format:** Mermaid Diagram Syntax (Compatible with GitHub, GitLab, Notion, etc.)
