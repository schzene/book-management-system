# Book Management System API Documentation

## Overview
This is a RESTful API for managing books with MySQL database and Redis caching.

## Base URL
```
http://localhost:8080/api
```

## Endpoints

### 1. Get All Books
**GET** `/books`

**Description:** Retrieve all books from the database (cached)

**Response:**
```json
[
  {
    "id": 1,
    "bookName": "Spring in Action",
    "author": "Craig Walls",
    "price": 39.99,
    "isbn": "978-1-617-29-464-0",
    "publishTime": "2018-10-16"
  }
]
```

### 2. Get Book by ID
**GET** `/books/{id}`

**Description:** Retrieve a specific book by its ID (cached)

**Parameters:**
- `id` (path parameter): Book ID (required)

**Response:**
```json
{
  "id": 1,
  "bookName": "Spring in Action",
  "author": "Craig Walls",
  "price": 39.99,
  "isbn": "978-1-617-29-464-0",
  "publishTime": "2018-10-16"
}
```

**Error Response:**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 999",
  "path": "/api/books/999"
}
```

### 3. Get Book by ISBN
**GET** `/books/isbn/{isbn}`

**Description:** Retrieve a book by its ISBN (cached)

**Parameters:**
- `isbn` (path parameter): Book ISBN (required)

### 4. Get Books by Author
**GET** `/books/author/{author}`

**Description:** Retrieve all books by a specific author (cached)

**Parameters:**
- `author` (path parameter): Author name (required)

### 5. Create Book
**POST** `/books`

**Description:** Create a new book

**Request Body:**
```json
{
  "bookName": "Clean Code",
  "author": "Robert C. Martin",
  "price": 45.99,
  "isbn": "978-0-132-35088-2",
  "publishTime": "2008-08-01"
}
```

**Response:** (201 Created)
```json
{
  "id": 2,
  "bookName": "Clean Code",
  "author": "Robert C. Martin",
  "price": 45.99,
  "isbn": "978-0-132-35088-2",
  "publishTime": "2008-08-01"
}
```

**Validation Errors:** (400 Bad Request)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "fieldErrors": {
    "bookName": "Book name cannot be blank",
    "price": "Price must be greater than 0"
  },
  "path": "/api/books"
}
```

### 6. Update Book
**PUT** `/books/{id}`

**Description:** Update an existing book

**Parameters:**
- `id` (path parameter): Book ID (required)

**Request Body:** (same as Create Book)
```json
{
  "bookName": "Clean Code (2nd Edition)",
  "author": "Robert C. Martin",
  "price": 49.99,
  "isbn": "978-0-132-35088-2",
  "publishTime": "2008-08-01"
}
```

**Response:** (200 OK)
```json
{
  "id": 2,
  "bookName": "Clean Code (2nd Edition)",
  "author": "Robert C. Martin",
  "price": 49.99,
  "isbn": "978-0-132-35088-2",
  "publishTime": "2008-08-01"
}
```

### 7. Delete Book
**DELETE** `/books/{id}`

**Description:** Delete a book by ID

**Parameters:**
- `id` (path parameter): Book ID (required)

**Response:** (204 No Content)

### 8. Clear Cache
**POST** `/books/cache/clear`

**Description:** Clear all cached books

**Response:** (200 OK)
```json
"Cache cleared successfully"
```

## HTTP Status Codes

| Status | Description |
|--------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Successful deletion |
| 400 | Bad Request - Validation error |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

## Validation Rules

### Book Fields

- **bookName**: Required, 1-100 characters
- **author**: Optional, max 50 characters
- **price**: Optional, must be > 0 and < 999999.99
- **isbn**: Optional, must be unique
- **publishTime**: Optional, date format YYYY-MM-DD

## Caching Strategy

- Books are cached for 10 minutes (600,000 ms)
- Cache is invalidated on create/update/delete operations
- Individual cache keys: `books::{id}`, `books::all`, `books::author_{author}`, `books::isbn_{isbn}`

## Example Requests

### Using curl

**Get all books:**
```bash
curl -X GET http://localhost:8080/api/books
```

**Create a book:**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "bookName": "Spring Boot in Action",
    "author": "Craig Walls",
    "price": 39.99,
    "isbn": "978-1-617-29-464-0",
    "publishTime": "2018-10-16"
  }'
```

**Update a book:**
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "bookName": "Spring Boot in Action (2nd Edition)",
    "author": "Craig Walls",
    "price": 45.99
  }'
```

**Delete a book:**
```bash
curl -X DELETE http://localhost:8080/api/books/1
```
