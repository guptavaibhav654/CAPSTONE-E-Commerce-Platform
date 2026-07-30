# E-Commerce Platform

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/guptavaibhav654/CAPSTONE-E-Commerce-Platform/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Contributors](https://img.shields.io/badge/contributors-1-orange.svg)](https://github.com/guptavaibhav654/CAPSTONE-E-Commerce-Platform/graphs/contributors)

A Spring Boot-based E-commerce platform featuring users, products, shopping cart, and orders. Includes REST endpoints, service layer, JPA repositories, DTOs, mappers, and basic exception handling.

## Quick Start

Prerequisites:
- Java 21
- Git
- Maven (or use the included Maven Wrapper)

Clone and run:

```bash
git clone https://github.com/guptavaibhav654/CAPSTONE-E-Commerce-Platform.git
cd CAPSTONE-E-Commerce-Platform
./mvnw clean package
./mvnw spring-boot:run
```

API base: `http://localhost:8080/api`
H2 console: `http://localhost:8080/api/h2-console`

## Important Endpoints
- POST /api/users/register — register user (UserRequest)
- POST /api/users/login — login (LoginRequest)
- POST /api/products — create product (ProductRequest)
- GET /api/products/page — paginated products
- POST /api/cart — add to cart (AddCartRequest)
- POST /api/orders — place order (userId)

Full API details and diagrams: `PROJECT_ARCHITECTURE.md`, `MERMAID_DIAGRAMS.md`, `QUICK_REFERENCE.md`.

## Configuration
Edit `src/main/resources/application.properties` to configure datasource, port, and logging.

## Security
- Passwords hashed with BCrypt (PasswordEncoder bean in `SecurityConfig`)
- Validation via Jakarta Validation annotations

## Development Notes
- Monetary values use `BigDecimal`.
- Global exception handler returns appropriate HTTP status codes (400/404/409/500).
- Use the Maven wrapper (`mvnw`) if Maven is not installed system-wide.

## Contributing
1. Fork the repo
2. Create a branch: `git checkout -b feature/my-feature`
3. Commit changes: `git commit -m "Add feature"`
4. Push and open a PR

Include this Co-authored-by footer in commits if using Copilot changes:
```
Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>
```

## License
This project uses the MIT License. Add `LICENSE` file to the repository.

---

For detailed architecture diagrams and developer guide see `PROJECT_ARCHITECTURE.md` and `QUICK_REFERENCE.md`.
