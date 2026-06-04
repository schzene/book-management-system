# Setup Guide for Book Management System

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- IDE (IntelliJ IDEA or VS Code)

## Installation Steps

### 1. Clone the Repository
```bash
git clone https://github.com/schzene/book-management-system.git
cd book-management-system
```

### 2. Setup MySQL Database

#### Create Database
```sql
CREATE DATABASE bookdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### Create Table (if not using JPA auto-creation)
```sql
USE bookdb;

CREATE TABLE book (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_name VARCHAR(100) NOT NULL,
    author VARCHAR(50),
    price DECIMAL(10,2),
    ISBN VARCHAR(20) UNIQUE,
    publish_time DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

#### Create User (Optional but Recommended)
```sql
CREATE USER 'bookuser'@'localhost' IDENTIFIED BY 'bookpass123';
GRANT ALL PRIVILEGES ON bookdb.* TO 'bookuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Setup Redis

#### On Windows (using WSL or native installation)
```bash
# Install Redis if not already installed
# Download from: https://github.com/microsoftarchive/redis/releases

# Start Redis
redis-server.exe
```

#### On Linux/Mac
```bash
# Install Redis
brew install redis  # macOS
# or
sudo apt-get install redis-server  # Ubuntu/Debian

# Start Redis
redis-server
```

### 4. Update Application Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root  # Change to 'bookuser' if you created a new user
    password: root  # Change to 'bookpass123' if you created a new user
  redis:
    host: localhost
    port: 6379
    password:  # Leave empty if no password is set
```

### 5. Build the Project

```bash
mvn clean install
```

### 6. Run the Application

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar target/book-management-system-1.0.0.jar
```

The application will start on `http://localhost:8080`

## Verification

### Check if the application is running
```bash
curl http://localhost:8080/api/books
```

You should get an empty array or list of books:
```json
[]
```

### Verify MySQL Connection
Logs should show:
```
2024-01-15 10:30:00 - Hibernates initiated successfully
```

### Verify Redis Connection
Logs should show:
```
2024-01-15 10:30:00 - Redis connection established
```

## Troubleshooting

### MySQL Connection Error
- Check if MySQL is running: `mysql --version`
- Verify credentials in `application.yml`
- Ensure database exists: `SHOW DATABASES;`

### Redis Connection Error
- Check if Redis is running: `redis-cli ping` (should return PONG)
- Verify Redis host and port in `application.yml`

### Port Already in Use
- Change port in `application.yml`: `server.port: 8081`

### Build Failure
- Clear cache: `mvn clean`
- Update dependencies: `mvn dependency:resolve`
- Check Java version: `java -version`

## Database Initialization

The application uses JPA/Hibernate with `ddl-auto: update`, which means:
- Tables are created automatically on first run
- Schema updates are applied automatically
- Data is preserved during updates

## Running Tests

```bash
mvn test
```

## IDE Setup

### IntelliJ IDEA
1. Open project
2. Click "Run" → "Run '...'" to start
3. Or press `Shift + F10`

### VS Code
1. Install Extension Pack for Java
2. Press `F5` to run
3. Or use terminal: `mvn spring-boot:run`

## Next Steps

1. Create some books using the API
2. Test caching with Redis
3. Monitor logs for cache hits
4. Review API documentation in `docs/API_DOCUMENTATION.md`
