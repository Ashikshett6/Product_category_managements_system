# Product Category Management System – Complete Project Summary (Start to End)

---

## 1. PROJECT OVERVIEW

| Item | Detail |
|------|--------|
| **Project Name** | Product Category Management System (PCMS) |
| **Type** | REST API |
| **Purpose** | Manage product categories and products (One Category → Many Products) |
| **Architecture** | Layered (Controller → Service → Repository → Database) |
| **Port** | 8082 |

---

## 2. TECH STACK

| Technology | Purpose |
|------------|---------|
| Java 21 | Programming language |
| Spring Boot 3.5.9 | Application framework |
| Spring Data JPA | Database access |
| MySQL | Database |
| ModelMapper | DTO ↔ Entity conversion |
| Lombok | Reduce boilerplate |
| Bean Validation | Request validation |
| Maven | Build & dependency management |

---

## 3. PROJECT STRUCTURE

```
ProductCategoryMangementSystem/
├── src/main/java/com/PCMS/ProductCategoryMangementSystem/
│   ├── ProductCategoryMangementSystemApplication.java   ← Entry point
│   ├── controller/
│   │   ├── CategoryController.java
│   │   └── ProductController.java
│   ├── service/
│   │   ├── CategoryService.java (interface)
│   │   ├── CategoryServiceImp.java
│   │   ├── ProductService.java (interface)
│   │   └── ProductServiceImpl.java
│   ├── repository/
│   │   ├── CategoryRepo.java
│   │   └── ProductRepo.java
│   ├── entity/
│   │   ├── Category.java
│   │   └── Product.java
│   ├── dto/
│   │   ├── CategoryDto.java
│   │   └── ProductDto.java
│   ├── exception/
│   │   └── ResourceNotFoundException.java
│   ├── advice/
│   │   └── GlobalExceptionHandler.java
│   └── configuration/
│       └── ModelMapperConfig.java
├── src/main/resources/
│   └── application.yaml
└── pom.xml
```

---

## 4. DATABASE DESIGN

### Tables

**categories**
| Column | Type | Description |
|--------|------|-------------|
| category_id | BIGINT (PK, Auto) | Primary key |
| category_name | VARCHAR (UNIQUE) | Category name |
| description | VARCHAR | Optional description |

**products**
| Column | Type | Description |
|--------|------|-------------|
| product_id | BIGINT (PK, Auto) | Primary key |
| product_name | VARCHAR (UNIQUE) | Product name |
| description | VARCHAR | Optional description |
| category_id | BIGINT (FK) | References categories |

### Relationship
- **One Category** has **Many Products** (OneToMany / ManyToOne)
- Product owns the relationship (has foreign key categoryId)

---

## 5. ENTITIES (JPA)

### Category Entity
- `categoryId` (long) – Primary key, auto-generated
- `categoryName` (String) – Unique, required
- `description` (String) – Optional
- `products` (List<Product>) – OneToMany, cascade ALL, orphanRemoval true
- Uses `@JsonManagedReference` to avoid circular JSON

### Product Entity
- `productId` (Long) – Primary key, auto-generated
- `productName` (String) – Unique, required
- `description` (String) – Optional
- `category` (Category) – ManyToOne, JoinColumn categoryId
- Uses `@JsonBackReference` to avoid circular JSON

---

## 6. DTOs (Data Transfer Objects)

### CategoryDto
- `categoryId` – For response
- `categoryName` – Required (@NotBlank)
- `description` – Optional
- `products` – List<ProductDto> (nested)

### ProductDto
- `productId` – For response
- `productName` – Required (@NotBlank)
- `description` – Optional
- No category field (category comes from URL path)

---

## 7. REPOSITORIES

### CategoryRepo
```java
extends JpaRepository<Category, Long>
```
- `findById()`, `findAll()`, `save()`, `delete()` – inherited

### ProductRepo
```java
extends JpaRepository<Product, Long>
```
- `findById()`, `findAll()`, `save()`, `delete()` – inherited

---

## 8. SERVICES (Business Logic)

### CategoryService / CategoryServiceImp
| Method | Description |
|--------|-------------|
| createCategory(dto) | DTO→Entity, save, Entity→DTO |
| fetchCategoryById(id) | Find by ID, throw if not found, return DTO |
| fetchAllCategories() | Find all, map to DTOs, return list |
| updateCategory(dto, id) | Find entity, map non-null from DTO, save, return DTO |
| deleteProduct(categoryId, productId) | Validate both exist & product belongs to category, delete product, return updated category |

### ProductService / ProductServiceImpl
| Method | Description |
|--------|-------------|
| createProduct(dto, categoryId) | DTO→Entity, set category, save, return DTO |
| updateNameDescription(dto, productId) | Find product, map non-null from DTO, save, return DTO |

---

## 9. CONTROLLERS (REST API)

### CategoryController (`/category`)

| Method | Endpoint | Action |
|--------|----------|--------|
| POST | /category | Create category |
| GET | /category | Get all categories |
| GET | /category/{categoryId} | Get category by ID |
| PUT | /category/{categoryId} | Update category |
| DELETE | /category/{categoryId}/product/{productId} | Delete product from category |

### ProductController (`/product`)

| Method | Endpoint | Action |
|--------|----------|--------|
| POST | /product/{categoryId} | Create product under category |
| PUT | /product/{productId} | Update product |

---

## 10. REQUEST FLOW (START TO END)

### Example: Create Category

```
1. Client sends POST /category with JSON body
   {
     "categoryName": "Electronics",
     "description": "Electronic devices"
   }

2. CategoryController.createCategory()
   - @Valid triggers validation (categoryName must not be blank)
   - Calls service.createCategory(dto)

3. CategoryServiceImp.createCategory()
   - modelMapper.map(dto, Category.class) → DTO to Entity
   - repo.save(entity) → Saves to MySQL
   - modelMapper.map(savedCategory, CategoryDto.class) → Entity to DTO
   - Returns CategoryDto

4. Controller returns ResponseEntity.status(201).body(categoryDto)

5. Client receives 201 Created with:
   {
     "categoryId": 1,
     "categoryName": "Electronics",
     "description": "Electronic devices",
     "products": null
   }
```

---

### Example: Create Product

```
1. Client sends POST /product/1 with JSON body
   (1 = categoryId)
   {
     "productName": "Laptop",
     "description": "Gaming laptop"
   }

2. ProductController.createProduct(dto, 1)
   - Calls service.createProduct(dto, categoryId)

3. ProductServiceImpl.createProduct()
   - modelMapper.map(dto, Product.class)
   - categoryRepo.findById(1) → Gets Category (or throws 404)
   - entity.setCategory(category)
   - productRepo.save(entity)
   - Returns ProductDto

4. Client receives 201 Created
```

---

### Example: Update Category (Partial)

```
1. Client sends PUT /category/1
   {
     "categoryName": "Electronics Updated",
     "description": null
   }

2. CategoryServiceImp.updateCategory()
   - repo.findById(1) → Get existing Category
   - modelMapper.setSkipNullEnabled(true) → Only copy non-null fields
   - modelMapper.map(dto, entity) → Updates categoryName, skips null description
   - repo.save(entity)
   - Returns updated CategoryDto
```

---

### Example: Delete Product

```
1. Client sends DELETE /category/1/product/5

2. CategoryServiceImp.deleteProduct(1, 5)
   - repo.findById(1) → Validate category exists
   - productRepo.findById(5) → Validate product exists
   - Check: product.getCategory().getCategoryId() == 1 (belongs to category)
   - productRepo.delete(product) → Deletes from DB
   - repo.findById(1) → Get updated category (without deleted product)
   - Returns CategoryDto

3. Parent category's product list automatically updated (orphanRemoval)
```

---

## 11. EXCEPTION HANDLING

### ResourceNotFoundException
- Thrown when `findById()` returns empty Optional
- Message: "Category with id X does not exist" or similar

### GlobalExceptionHandler (@RestControllerAdvice)
- Catches ResourceNotFoundException
- Returns 404 NOT FOUND with message in body

### Validation Failure (@Valid)
- If @NotBlank or @NotNull fails
- Spring returns 400 Bad Request (before controller method runs)

---

## 12. CONFIGURATION

### application.yaml
- **Database:** MySQL at localhost:3306/pcmsr, user root
- **JPA:** ddl-auto: update (auto-create/update tables), show-sql: true
- **Dialect:** MySQL
- **Server:** Port 8082

### ModelMapperConfig
- MatchingStrategy: STRICT (exact field names)
- SkipNullEnabled: true (for partial updates)

---

## 13. DEPENDENCIES (pom.xml)

- spring-boot-starter-data-jpa
- spring-boot-starter-web
- spring-boot-starter-validation
- modelmapper (3.2.6)
- mysql-connector-j
- lombok

---

## 14. HOW TO RUN

1. **Prerequisites:** Java 21, Maven, MySQL running
2. **Create database:** `CREATE DATABASE pcmsr;`
3. **Update application.yaml** if DB credentials differ
4. **Run:** `mvn spring-boot:run` or run main class
5. **Base URL:** http://localhost:8082

---

## 15. API QUICK REFERENCE

| Method | URL | Body | Description |
|--------|-----|------|-------------|
| POST | /category | {"categoryName":"X","description":"Y"} | Create category |
| GET | /category | - | Get all categories |
| GET | /category/{id} | - | Get category by ID |
| PUT | /category/{id} | {"categoryName":"X","description":"Y"} | Update category |
| DELETE | /category/{catId}/product/{prodId} | - | Delete product |
| POST | /product/{categoryId} | {"productName":"X","description":"Y"} | Create product |
| PUT | /product/{productId} | {"productName":"X","description":"Y"} | Update product |

---

## 16. DATA FLOW SUMMARY

```
Request → Controller (@Valid) → Service → Repository → Database
                ↓
         DTO (Request)
                ↓
         Entity (via ModelMapper)
                ↓
         Save/Find/Delete
                ↓
         Entity (from DB)
                ↓
         DTO (via ModelMapper)
                ↓
Response ← Controller (ResponseEntity) ← Service
```

---

**End of Project Summary**
