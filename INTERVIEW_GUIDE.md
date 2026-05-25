# Product Category Management System – Complete Interview Q&A Guide

**Final document with all project-related interview questions and complete answers**

---

# PART 1: PROJECT OVERVIEW

## Q1. Tell me about your project in 2 minutes.
**Answer:** I developed a **Product Category Management System** – a REST API using Spring Boot. It manages categories and products in a one-to-many relationship: one category can have many products. I implemented full CRUD for categories (Create, Read, Update, Delete) and Create/Update for products. The tech stack includes Spring Boot, Spring Data JPA, MySQL, ModelMapper for DTO mapping, Bean Validation, and global exception handling. The API runs on port 8082 and uses a layered architecture: Controller → Service → Repository → Entity.

---

## Q2. What is the architecture of your project?
**Answer:** It follows a **layered architecture**:

1. **Controller** – Handles HTTP requests, validates input with @Valid, returns ResponseEntity.
2. **Service** – Business logic, DTO–Entity conversion, orchestration.
3. **Repository** – Data access (JPA repositories).
4. **Entity** – Database mapping.
5. **DTO** – Request/response data transfer.
6. **Exception Handler** – Global error handling with @RestControllerAdvice.

---

## Q3. What technologies did you use?
**Answer:**
- **Spring Boot 3.5.9** – Framework
- **Spring Data JPA** – Data access
- **MySQL** – Database
- **ModelMapper** – DTO ↔ Entity mapping
- **Lombok** – Boilerplate reduction
- **Bean Validation (Jakarta)** – Input validation
- **Maven** – Build tool
- **Java 21**

---

# PART 2: ENTITY & JPA

## Q4. Explain your Category entity. What annotations did you use?
**Answer:** Category is a JPA entity mapped to the `categories` table. I used:
- `@Entity` – Marks it as a JPA entity
- `@Table(name = "categories")` – Table name
- `@Id` – Primary key
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` – Auto-increment
- `@Column(unique = true)` – categoryName must be unique
- `@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)` – One category has many products
- `@JsonManagedReference` – Avoids infinite loop when serializing to JSON

---

## Q5. Explain your Product entity. What is @JsonBackReference?
**Answer:** Product has:
- `@ManyToOne` – Many products belong to one category
- `@JoinColumn(name = "categoryId")` – FK column in products table
- `@JsonBackReference` – Prevents infinite recursion in JSON. With `@JsonManagedReference` on Category and `@JsonBackReference` on Product, Jackson knows which side to serialize.

---

## Q6. What is OneToMany and ManyToOne? Explain the relationship.
**Answer:** 
- **OneToMany** (Category → Products): One category has many products.
- **ManyToOne** (Product → Category): Many products belong to one category.
- It’s a **bidirectional** relationship. Category has `List<Product> products`; Product has `Category category`. `mappedBy = "category"` tells JPA that Product owns the relationship (holds the FK).

---

## Q7. What is cascade = CascadeType.ALL?
**Answer:** Cascade means operations on the parent propagate to children. `CascadeType.ALL` = persist, merge, remove, refresh, detach. So saving a Category also saves its Products; deleting a Category deletes its Products.

---

## Q8. What is orphanRemoval = true?
**Answer:** When a product is removed from the category’s product list, JPA deletes it from the DB. Without it, the product would just be orphaned (no category reference) but still exist. With orphanRemoval = true, removing from the collection = delete from DB.

---

## Q9. What is @JoinColumn? Why do we use it?
**Answer:** `@JoinColumn(name = "categoryId")` defines the FK column in the products table. The owning side (Product) has the FK, so we put @JoinColumn there. JPA uses it to map the relationship.

---

## Q10. Difference between @Entity and @Table?
**Answer:** 
- `@Entity` – Marks the class as a JPA entity.
- `@Table(name = "categories")` – Specifies the table name. Without it, the table name would default to the class name.

---

# PART 3: DTO (Data Transfer Object)

## Q11. What is DTO? Why do we use it?
**Answer:** A DTO is a simple class that carries data between layers. We use it to:
1. **Decouple** API from DB structure
2. **Validate** only what the client sends
3. **Security** – avoid exposing entity internals
4. **Flexibility** – change API without changing entities
5. **Reduce payload** – return only needed fields

---

## Q12. Explain CategoryDto. What is each field for?
**Answer:** CategoryDto has:
- `categoryId` – Filled from DB on response (ignored on create)
- `categoryName` – Required, validated with @NotBlank
- `description` – Optional
- `products` – List of ProductDto when returning category with its products

---

## Q13. Explain ProductDto. Why is there no category field?
**Answer:** ProductDto has productId, productName, description. There is no category field because the category is taken from the URL path: `POST /product/{categoryId}`. The service sets the category in the entity before saving.

---

## Q14. Why use List<ProductDto> instead of List<Product> in CategoryDto?
**Answer:** To avoid exposing entities and circular references (Product → Category → Product). DTOs keep the response simple and under our control.

---

## Q15. Entity vs DTO – when to use which?
**Answer:** Use Entity in the repository/service for DB operations. Use DTO in the controller for request/response. Never expose entities directly in APIs.

---

# PART 4: CONTROLLER & REST API

## Q16. What is @RestController? Difference from @Controller?
**Answer:** `@RestController` = @Controller + @ResponseBody. Every method’s return value is serialized to JSON. `@Controller` is for views; @RestController for REST APIs.

---

## Q17. What is @RequestMapping? What does it do?
**Answer:** `@RequestMapping("/category")` sets the base path for all methods in the controller. For example, @GetMapping("/{categoryId}") becomes /category/{categoryId}.

---

## Q18. Explain @PostMapping, @GetMapping, @PutMapping, @DeleteMapping.
**Answer:**
- `@PostMapping` – Create (POST)
- `@GetMapping` – Read (GET)
- `@PutMapping` – Update (PUT)
- `@DeleteMapping` – Delete (DELETE)

---

## Q19. What is @PathVariable? When do you use it?
**Answer:** `@PathVariable` binds a path segment to a method parameter. Example: `@GetMapping("/{categoryId}")` with `@PathVariable Long categoryId` binds the value from the URL to categoryId.

---

## Q20. What is @RequestBody? What is @Valid?
**Answer:** 
- `@RequestBody` – Deserializes the JSON body into the parameter object.
- `@Valid` – Triggers Bean Validation. If validation fails (e.g. @NotBlank), Spring returns 400 Bad Request before the method runs.

---

## Q21. Why use ResponseEntity?
**Answer:** ResponseEntity lets us set HTTP status and body. For example: 201 CREATED for create, 200 OK for read/update, 404 NOT FOUND for missing resource.

---

## Q22. When to use POST vs PUT?
**Answer:** POST creates a new resource (e.g. new category). PUT updates an existing resource identified by ID. POST = create, PUT = update.

---

## Q23. List all your API endpoints.
**Answer:**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /category | Create category |
| GET | /category | Get all categories |
| GET | /category/{categoryId} | Get category by ID |
| PUT | /category/{categoryId} | Update category |
| DELETE | /category/{categoryId}/product/{productId} | Delete product from category |
| POST | /product/{categoryId} | Create product under category |
| PUT | /product/{productId} | Update product |

---

# PART 5: SERVICE LAYER

## Q24. What is the role of the Service layer?
**Answer:** The Service layer holds business logic, coordinates repositories, converts DTO ↔ Entity, handles validation and exceptions. Controllers stay thin and delegate to services.

---

## Q25. Explain the flow when creating a category.
**Answer:**
1. Controller receives CategoryDto with @Valid.
2. Service gets the DTO.
3. ModelMapper converts DTO → Category entity.
4. Repository saves the entity.
5. ModelMapper converts saved entity → CategoryDto.
6. Controller returns ResponseEntity with 201 and the DTO.

---

## Q26. How do you implement partial update (update only some fields)?
**Answer:** I use `modelMapper.getConfiguration().setSkipNullEnabled(true)` and then `modelMapper.map(dto, entity)`. Only non-null fields from the DTO are copied to the entity, so other fields stay unchanged.

---

## Q27. When you delete a product, how does the parent category get updated?
**Answer:** I call `productRepo.delete(product)`. Because of `orphanRemoval = true` on Category’s products and the bidirectional mapping, JPA keeps the category’s product list in sync. The product is removed from the DB and from the category’s collection.

---

## Q28. Why do you validate that the product belongs to the category before delete?
**Answer:** To avoid deleting a product that belongs to another category. The client might send wrong categoryId and productId. The check ensures data integrity and security.

---

## Q29. What is @RequiredArgsConstructor? Why use it?
**Answer:** Lombok annotation that generates a constructor for all final fields. Used for dependency injection – Spring injects CategoryRepo, ModelMapper, etc. Avoids writing constructors manually.

---

# PART 6: REPOSITORY LAYER

## Q30. What is JpaRepository? What does it provide?
**Answer:** JpaRepository extends CrudRepository and adds JPA-specific features. It gives methods like `findById()`, `save()`, `findAll()`, `delete()`, `count()` without writing SQL.

---

## Q31. What methods from JpaRepository did you use?
**Answer:** `findById()`, `findAll()`, `save()`, `delete()`. All are built-in.

---

## Q32. How does findById work? What does orElseThrow do?
**Answer:** `findById(id)` returns `Optional<Entity>`. `orElseThrow(() -> new ResourceNotFoundException(...))` returns the entity if present, or throws the exception if empty. It replaces manual null checks.

---

# PART 7: EXCEPTION HANDLING

## Q33. What is @RestControllerAdvice?
**Answer:** A global exception handler that applies to all controllers. Methods annotated with @ExceptionHandler catch exceptions and return a consistent response. No need for try-catch in every controller.

---

## Q34. What is @ExceptionHandler?
**Answer:** Marks a method that handles a specific exception type. When that exception is thrown anywhere in the app, Spring calls this method and uses its return value as the response.

---

## Q35. How do you handle ResourceNotFoundException?
**Answer:** I throw `ResourceNotFoundException` when a resource is not found. GlobalExceptionHandler catches it and returns `ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage())` (404).

---

## Q36. Why use custom exception instead of RuntimeException directly?
**Answer:** Custom exception lets us handle it specifically in the advice. We can return 404 for “not found” instead of 500. Different custom exceptions can map to different status codes and error formats.

---

# PART 8: MODELMAPPER & CONFIGURATION

## Q37. What is ModelMapper? Why use it?
**Answer:** ModelMapper maps between objects using reflection and field name matching. It avoids writing manual getter/setter conversion code for DTO ↔ Entity.

---

## Q38. How do you configure ModelMapper?
**Answer:** In ModelMapperConfig:
- `setMatchingStrategy(MatchingStrategies.STRICT)` – Fields must match by name
- `setSkipNullEnabled(true)` – Don’t copy null values when mapping to existing objects (for updates)

---

## Q39. How do you convert DTO to Entity and Entity to DTO?
**Answer:**
- DTO → Entity: `modelMapper.map(dto, Entity.class)`
- Entity → DTO: `modelMapper.map(entity, Dto.class)`

---

## Q40. What is @Configuration and @Bean?
**Answer:** 
- `@Configuration` – Marks a class as a source of bean definitions.
- `@Bean` – Marks a method that creates a Spring bean. The returned object is managed by Spring (e.g. ModelMapper).

---

# PART 9: VALIDATION

## Q41. What is @NotBlank? Difference from @NotNull?
**Answer:**
- `@NotNull` – Value must not be null.
- `@NotBlank` – Must not be null, empty string, or whitespace. Used for strings that must have content.

---

## Q42. What is Bean Validation? Which dependency provides it?
**Answer:** Bean Validation (JSR 380) validates objects using annotations like @NotBlank, @NotNull, @Size. We use `spring-boot-starter-validation`, which includes Hibernate Validator.

---

## Q43. When does validation run? What happens if it fails?
**Answer:** When the controller method is called with @Valid. If validation fails, Spring throws MethodArgumentNotValidException and returns 400 Bad Request before the method body runs.

---

# PART 10: DATABASE & APPLICATION CONFIG

## Q44. Explain your application.yaml configuration.
**Answer:**
- `spring.datasource.url` – MySQL connection URL
- `spring.datasource.username/password` – DB credentials
- `spring.jpa.hibernate.ddl-auto: update` – Schema is updated on startup (creates/alters tables)
- `spring.jpa.show-sql: true` – Log SQL
- `hibernate.dialect` – MySQL dialect
- `server.port: 8082` – App port

---

## Q45. What does ddl-auto: update mean?
**Answer:** Hibernate updates the DB schema to match entities. It creates new tables and columns but does not drop existing ones. Good for development; for production, use validate or none.

---

## Q46. What is Hibernate Dialect?
**Answer:** Dialect tells Hibernate which SQL dialect to use (MySQL, PostgreSQL, etc.). Each DB has different SQL syntax; the dialect handles that.

---

# PART 11: SPRING BOOT BASICS

## Q47. What is @SpringBootApplication?
**Answer:** Combines:
- `@Configuration` – Bean definitions
- `@EnableAutoConfiguration` – Auto-config based on classpath
- `@ComponentScan` – Scans for components in the package and sub-packages

---

## Q48. What is Dependency Injection? How does Spring do it?
**Answer:** DI means objects receive their dependencies from outside instead of creating them. Spring injects dependencies via constructor (with @RequiredArgsConstructor), setter, or field injection. Constructor injection is preferred.

---

## Q49. What is @Service, @Repository, @RestController?
**Answer:**
- `@Service` – Service layer bean
- `@Repository` – Data access bean (adds exception translation)
- `@RestController` – REST API controller

---

# PART 12: LOMBOK & MAVEN

## Q50. What Lombok annotations did you use?
**Answer:**
- `@Getter` / `@Setter` – Getters and setters
- `@AllArgsConstructor` – Constructor with all fields
- `@NoArgsConstructor` – No-arg constructor (needed by JPA)
- `@RequiredArgsConstructor` – Constructor for final fields (for DI)

---

## Q51. What dependencies are in your pom.xml?
**Answer:**
- spring-boot-starter-data-jpa – JPA/Hibernate
- spring-boot-starter-web – REST API
- spring-boot-starter-validation – Bean Validation
- modelmapper – Object mapping
- mysql-connector-j – MySQL driver
- lombok – Boilerplate reduction

---

# PART 13: REQUEST/RESPONSE EXAMPLES

## Q52. Give a sample request and response for creating a category.
**Answer:**
```
POST /category
Content-Type: application/json

Request:
{
  "categoryName": "Electronics",
  "description": "Electronic devices"
}

Response (201 Created):
{
  "categoryId": 1,
  "categoryName": "Electronics",
  "description": "Electronic devices",
  "products": null
}
```

---

## Q53. Give a sample request for creating a product.
**Answer:**
```
POST /product/1
Content-Type: application/json

Request:
{
  "productName": "Laptop",
  "description": "Gaming laptop"
}

Response (201 Created):
{
  "productId": 1,
  "productName": "Laptop",
  "description": "Gaming laptop"
}
```
(1 in the URL is categoryId)

---

## Q54. What happens if we send empty categoryName?
**Answer:** @NotBlank fails. Spring returns 400 Bad Request with validation error details before the service method runs.

---

## Q55. What happens if we request category with non-existent ID?
**Answer:** findById returns empty Optional. orElseThrow throws ResourceNotFoundException. GlobalExceptionHandler catches it and returns 404 NOT FOUND with the message.

---

# PART 14: COMPLETE CODE REFERENCE

## CategoryDto
```java
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CategoryDto {
    private long categoryId;
    @NotBlank(message = "category name Is required.")
    private String categoryName;
    private String description;
    private List<ProductDto> products;
}
```

## ProductDto
```java
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProductDto {
    private Long productId;
    @NotBlank(message = "Product name Is Required")
    private String productName;
    private String description;
}
```

## Category Entity
```java
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;
    @NotBlank
    @Column(unique = true)
    private String categoryName;
    private String description;
    @JsonManagedReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
}
```

## Product Entity
```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank
    @Column(unique = true)
    private String productName;
    private String description;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoryId")
    @JsonBackReference
    private Category category;
}
```

## API Endpoints Summary
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /category | Create category |
| GET | /category | Get all categories |
| GET | /category/{categoryId} | Get category by ID |
| PUT | /category/{categoryId} | Update category |
| DELETE | /category/{categoryId}/product/{productId} | Delete product |
| POST | /product/{categoryId} | Create product |
| PUT | /product/{productId} | Update product |

---

**Total: 55 Interview Questions with Complete Answers**
