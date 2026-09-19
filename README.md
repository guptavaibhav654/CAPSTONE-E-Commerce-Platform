# E-Commerce Platform

## Description
In this capstone project, you will design and implement an E-Commerce Platform that allows users to browse products, add items to their cart, place orders, and manage their profiles. The system should support functionalities such as product management, user registration, shopping cart operations, order processing, and search functionality. The project will integrate various Java concepts, including Object-Oriented Programming (OOP), Collections, Java 8 features, SOLID principles, and design patterns.

## Topics Covered
- **Object-Oriented Programming (OOP)**
  - Classes and Objects
  - Inheritance
  - Polymorphism
  - Encapsulation
  - Abstraction
- **Strings**
  - String manipulation
  - StringBuilder and StringBuffer
- **Collections**
  - List, Set, Map
  - Iteration and manipulation of collections
- **Java 8 Essentials**
  - Stream API
  - Lambda Expressions
  - Functional Interfaces
  - Optional
- **SOLID Principles**
  - Single Responsibility Principle
  - Open/Closed Principle
  - Liskov Substitution Principle
  - Interface Segregation Principle
  - Dependency Inversion Principle
- **Design Patterns**
  - Factory Pattern
  - Decorator Pattern
  - Builder Pattern
  - Strategy Pattern

## Objectives
By completing this project, learners should be able to:
1. Apply OOP principles to design a robust and maintainable system.
2. Utilize Java Collections to manage data efficiently.
3. Implement Java 8 features such as Stream API, Lambda Expressions, and Functional Interfaces.
4. Adhere to SOLID principles to ensure code quality and scalability.
5. Use design patterns to solve common software design problems.

## Project Scope
**Complexity Level:** Easy

**Key Features/Modules to be Implemented:**
1. **Product.Product Management:**
   - Add new products
   - Update product details
   - Delete products
   - Search for products by name, category, or price range
2. **User.User Management:**
   - Register new users
   - Update user profiles
   - Delete users
   - Authenticate users (login/logout)
3. **Shopping Cart:**
   - Add items to the cart
   - Remove items from the cart
   - View cart contents
   - Calculate total price
4. **Order Processing:**
   - Place orders
   - View order history
   
## Deliverables
1. **Code:**
   - Well-structured and documented source code
2. **Documentation:**
   - Project documentation including design decisions, class diagrams, and usage instructions
3. **Test Cases:**
   - Unit tests for key functionalities
4. **Deployment Instructions:**
   - Instructions to run the application locally

## Guidelines
**Day 1:**
1. **Project Setup:**
   - Initialize a new Java project
   - Set up the project structure with packages for different modules (e.g., products, users, cart, orders)
2. **Product.Product Management Module:**
   - Create classes for Product.Product and Product.ProductService
   - Implement CRUD operations for products
   - Use Collections (e.g., List) to store product data
3. **User.User Management Module:**
   - Create classes for User.User and User.UserService
   - Implement CRUD operations for users
   - Use Collections (e.g., List) to store user data

**Day 2:**

4. **Shopping Cart Module:**
   - Create classes for Cart and CartService
   - Implement functionalities to add, remove, and view items in the cart
   - Calculate total price of items in the cart

5. **Order Processing Module:**
   - Create classes for Order and OrderService
   - Implement functionalities to place orders and view order history
   
6. **Java 8 Features:**
   - Use Optional to handle null values
   - Implement Functional Interfaces for custom operations

**Day 3:**

7. **SOLID Principles:**
   - Refactor code to adhere to SOLID principles
   - Ensure each class has a single responsibility
   - Use interfaces and abstract classes where appropriate

8. **Design Patterns:**
   - Implement Factory Pattern for creating product and user objects
   - Use Decorator Pattern to add additional features to products (e.g., discounts)
   - Apply Builder Pattern for constructing complex objects
   - Implement Strategy Pattern for different search strategies

**Day 4:**

9. **Testing and Documentation:**
   - Write unit tests for key functionalities
   - Document the project with design decisions and usage instructions
