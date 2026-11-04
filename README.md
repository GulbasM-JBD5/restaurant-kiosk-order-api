# Restaurant Kiosk Order Management API

A RESTful backend application built with **Java 25, Spring Boot, Spring Data JPA, Hibernate, and MySQL** to manage restaurant products, kiosk orders, order lifecycle, and sales statistics.

This project was developed as part of the **Maitre Backend Internship Technical Assignment**.

## Overview

The application provides a backend API for a restaurant kiosk system where customers place orders and kitchen staff manage their preparation.

The system supports multiple restaurants, each with its own products and orders. It enforces business rules throughout the order lifecycle and provides paginated order retrieval and product sales statistics.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language |
| Spring Boot 4.1.1 | Backend framework |
| Spring Web | REST API development |
| Spring Data JPA | Data access and repository layer |
| Hibernate | ORM |
| MySQL 8 | Relational database |
| Maven | Dependency and build management |
| Jakarta Bean Validation | Request validation |
| Postman | API testing |

## Architecture

The application follows a layered architecture to separate responsibilities and keep the code maintainable.

```text
src/main/java/com/example/_4group/
│
├── controller/
│   ├── RestoranController
│   ├── ProductController
│   └── OrderController
│
├── service/
│   ├── RestoranService
│   ├── ProductService
│   └── OrderService
│
├── repository/
│   ├── RestoranRepository
│   ├── ProductRepository
│   ├── OrderRepository
│   └── OrderItemRepository
│
├── entity/
│   ├── Restoran
│   ├── Product
│   ├── Order
│   └── OrderItem
│
├── dto/
│   ├── request/
│   └── response/
│
└── enums/
    └── OrderStatus
```

**Layer responsibilities:**

- **Controller:** Handles HTTP requests, validates input, and returns appropriate responses.
- **Service:** Contains business logic and enforces application rules.
- **Repository:** Handles database operations using Spring Data JPA.
- **Entity:** Represents the database model and relationships.
- **DTO:** Separates API request/response structures from persistence entities.
- **Enum:** Defines the allowed order statuses.

## Features

### 1. Restaurant Management
- Create restaurants.
- Associate products and orders with their respective restaurants.
- Keep restaurant data isolated at the business logic level.

### 2. Product Management
- Create products for a specific restaurant.
- Update product prices.
- Remove products from sale.
- Return previously unavailable products to sale.
- Validate product names, categories, and prices.

New products are available for sale by default.

### 3. Order Creation
- Create an order for a specific restaurant.
- Add multiple products with individual quantities.
- Calculate line subtotals and the total order amount.
- Assign `PENDING` status to newly created orders.

The application also checks that:
- The restaurant exists.
- Every requested product exists.
- Products belong to the selected restaurant.
- Products are currently available for sale.
- Quantities are positive.

### 4. Order Status Management

The order lifecycle follows a strict transition sequence:

`PENDING → PREPARING → READY → COMPLETED`

Invalid status transitions are rejected.

Orders can only be cancelled while their status is `PENDING`.

### 5. Restaurant Orders
- Retrieve orders belonging to a specific restaurant.
- Filter orders by status.
- Support pagination.
- Sort results by order ID in descending order.
- Include product names, quantities, unit prices, line subtotals, and total order amounts.

### 6. Top 5 Best-Selling Products

Retrieve the five best-selling products for a restaurant, ranked by the total quantity sold.

Cancelled orders are excluded from the calculation.

The implementation uses a custom JPQL aggregation query with `SUM`, `GROUP BY`, and `ORDER BY`, rather than loading all orders into application memory.

## API Endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/restaurants` | Create a restaurant |
| POST | `/products` | Create a product |
| PUT | `/products/{id}/price` | Update product price |
| PUT | `/products/{id}/remove-from-sale` | Remove product from sale |
| PUT | `/products/{id}/return-to-sale` | Return product to sale |
| POST | `/orders` | Create an order |
| PUT | `/orders/{id}/status?status=PREPARING` | Change order status |
| PUT | `/orders/{id}/cancel` | Cancel a pending order |
| GET | `/orders/restaurants/{restaurantId}/orders` | Retrieve restaurant orders |
| GET | `/orders/restaurants/{restaurantId}/top-selling-products` | Retrieve top 5 products |

### Example Requests

**Create a product**

`POST /products`

```json
{
  "name": "Classic Burger",
  "category": "Fast Food",
  "price": 12.50,
  "restoranId": 1
}
```

**Create an order**

`POST /orders`

```json
{
  "restaurantId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

**Change order status**

`PUT /orders/1/status?status=PREPARING`

**Filter and paginate orders**

`GET /orders/restaurants/1/orders?status=PENDING&page=0&size=10`

The `status` parameter is optional. If omitted, all orders belonging to the restaurant are returned.

**Retrieve best-selling products**

`GET /orders/restaurants/1/top-selling-products`

## Database Design

The application uses MySQL with four main entities:

- **Restoran:** Stores restaurant information.
- **Product:** Stores product details, price, availability, and restaurant relationship.
- **Order:** Stores restaurant orders, status, and total amount.
- **OrderItem:** Stores individual products and quantities within an order.

### Entity Relationships

- One restaurant can have multiple products.
- One restaurant can have multiple orders.
- One order can contain multiple order items.
- Each order item references one product.

Product prices are stored in `BigDecimal` to avoid floating-point precision issues in monetary calculations.

**Historical price consistency:** Each order item stores a snapshot of the product's unit price at the time the order is created. Therefore, changing a product's current price does not affect previously created orders.

## Business Rules & Design Decisions

Some details were not explicitly defined in the assignment. The following decisions were made:

| Scenario | Decision |
|---|---|
| New product availability | Products are available by default. |
| Product price | Must be greater than zero. |
| Order quantity | Must be at least one. |
| Product availability | Products removed from sale cannot be ordered. |
| Restaurant ownership | An order cannot contain products belonging to another restaurant. |
| Initial order status | Every new order starts as `PENDING`. |
| Status transitions | Only the specified sequential transitions are allowed. |
| Cancellation | Only `PENDING` orders can be cancelled. |
| Sales calculation | All non-cancelled orders contribute to sales statistics. |
| Price changes | Existing order items retain their original unit prices. |
| Best-selling products | Products are ranked by the sum of ordered quantities. |
| Pagination | Default page is 0, default page size is 10. |

## Error Handling & Validation

The application uses Jakarta Bean Validation to validate incoming request data.

Examples include:

- `@NotBlank` for required text fields.
- `@NotNull` for required identifiers and values.
- `@NotEmpty` for order item collections.
- `@Min(1)` for product quantities.
- `@DecimalMin` for product prices.

Business rule violations and missing resources are handled using appropriate HTTP status codes through Spring's `ResponseStatusException`.

| HTTP Status | Meaning |
|---|---|
| 201 Created | Resource successfully created |
| 200 OK | Request successfully processed |
| 400 Bad Request | Invalid input or business rule violation |
| 404 Not Found | Requested resource does not exist |

## Getting Started

### Prerequisites

Make sure the following are installed:

- Java 25 or compatible JDK
- MySQL 8+
- Git

### 1. Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd 34group
```

### 2. Create the database

Open MySQL and execute:

```sql
CREATE DATABASE kiosk_db;
```

### 3. Configure database credentials

The application uses an environment variable for the database password.

In `src/main/resources/application.properties`:

```properties
spring.application.name=34group

spring.datasource.url=jdbc:mysql://localhost:3306/kiosk_db
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Set the `DB_PASSWORD` environment variable to your local MySQL password.

For example, in PowerShell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

Do not commit database passwords or other secrets to the repository.

### 4. Run the application

Using Maven Wrapper on Windows:

```bash
.\mvnw.cmd spring-boot:run
```

The application will start at:

`http://localhost:8080`

Hibernate automatically creates or updates the required database tables.

### 5. Test the API

You can test the endpoints using Postman or another HTTP client.

## Testing

The implemented endpoints and business scenarios were manually verified using Postman.

The verification covered:

- Restaurant and product creation.
- Product price updates and availability changes.
- Order creation and total amount calculation.
- Product ownership and availability validation.
- Sequential order status transitions.
- Pending order cancellation.
- Rejection of invalid status transitions.
- Restaurant-specific order retrieval.
- Status filtering and pagination.
- Best-selling product aggregation.
- Exclusion of cancelled orders from sales statistics.

## Time Spent

Approximately **[INSERT YOUR ACTUAL TIME]** was spent on implementation, debugging, and manual API verification.

## What I Would Improve With More Time

Given additional development time, I would focus on:

1. **Automated testing:** Add unit tests for service-layer business rules and integration tests for REST endpoints and database interactions.
2. **Centralized exception handling:** Introduce `@RestControllerAdvice` to provide consistent error response structures.
3. **Database migrations:** Replace automatic schema updates with Flyway or Liquibase.
4. **API documentation:** Integrate OpenAPI/Swagger for interactive endpoint documentation.
5. **Concurrency handling:** Introduce appropriate transaction and concurrency strategies for simultaneous order operations.
6. **Integration testing:** Add a reproducible test environment for database-related scenarios.

## Final Notes

This project focuses on applying backend fundamentals to a practical business scenario: maintaining data consistency, enforcing business rules, handling invalid operations, and retrieving meaningful information efficiently.

The implementation prioritizes clear separation of concerns, explicit business logic, and reliable database interactions.
