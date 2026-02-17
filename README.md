# LocalConnect

A scalable, enterprise-grade platform for booking local services with real-time availability and secure payment processing. Built with Java, Spring Boot, MongoDB, and modern cloud-native architecture.

## 🎯 Overview

LocalConnect is a full-featured service booking platform that connects service providers with customers. It provides a robust backend API for managing bookings, user authentication, service listings, real-time availability tracking, and payment processing with role-based access control.

**Key Highlights:**
- Secure JWT-based authentication and authorization
- Real-time booking confirmation and status updates
- Role-based access control (RBAC) for different user types
- MongoDB Atlas integration for scalable data storage
- REST API with comprehensive endpoint documentation
- Cloud-ready deployment with Docker support
- Code quality monitoring with SonarQube/SonarCloud

## ✨ Features

**User Management**
- Secure user registration and login
- JWT token-based authentication
- Role-based access control (Customer, Service Provider, Admin)
- User profile management and preferences

**Service Management**
- Service provider onboarding and verification
- Service listing and categorization
- Service description, pricing, and availability management
- Service ratings and reviews

**Booking System**
- Real-time availability checking
- Instant booking confirmation
- Booking history and management
- Cancellation and rescheduling support
- Status tracking (Pending, Confirmed, In-Progress, Completed, Cancelled)

**Payment Integration**
- Secure payment processing
- Multiple payment method support
- Payment status tracking
- Invoice generation

**Additional Features**
- Advanced search and filtering
- Notification system
- Analytics and reporting
- API documentation with Postman collection
- Docker containerization for easy deployment

## 🛠️ Technology Stack

**Backend**
- Java 17+
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data MongoDB
- Spring Cloud for microservices readiness

**Database**
- MongoDB Atlas (NoSQL document database)
- Mongoose-style data modeling

**API & Documentation**
- RESTful API
- OpenAPI/Swagger documentation
- Postman collection for testing

**Cloud & DevOps**
- Docker containerization
- Maven build management
- SonarQube/SonarCloud for code quality
- Git version control

**Security**
- JWT token authentication
- Spring Security
- OAuth2 support
- Encrypted password storage
- CORS configuration

## 📁 Project Structure

```
Local-Connect/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/          # REST endpoints
│   │   │   ├── service/             # Business logic layer
│   │   │   ├── repository/          # Data access layer (MongoDB)
│   │   │   ├── model/               # Entity and DTO classes
│   │   │   ├── security/            # JWT and authentication
│   │   │   ├── config/              # Application configuration
│   │   │   └── exception/           # Custom exceptions
│   │   └── resources/
│   │       └── application.yaml     # Application properties
│   └── test/
│       └── java/                    # Unit and integration tests
├── postman-collection/              # API testing collection
├── .mvn/wrapper/                    # Maven wrapper for build
├── pom.xml                          # Maven project configuration
├── Dockerfile                       # Docker image definition
├── mvnw                             # Maven wrapper script (Linux/Mac)
├── mvnw.cmd                         # Maven wrapper script (Windows)
├── .gitignore                       # Git ignore rules
└── README.md                        # Project documentation
```

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+ or use Maven wrapper
- MongoDB Atlas account (or local MongoDB instance)
- Git
- Docker (optional, for containerization)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/aryan735/Local-Connect.git
   cd Local-Connect
   ```

2. **Configure environment variables:**
   
   Create `src/main/resources/application.yaml`:
   ```yaml
   spring:
     data:
       mongodb:
         uri: mongodb+srv://username:password@cluster.mongodb.net/localconnect?retryWrites=true&w=majority
   
   jwt:
     secret: your-secret-key-here
     expiration: 86400000  # 24 hours in milliseconds
   ```

3. **Build the project:**
   
   Using Maven wrapper (no installation required):
   ```bash
   ./mvnw clean install
   ```
   
   Or with installed Maven:
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   The application will start on `http://localhost:8080`

## 🐳 Docker Deployment

### Build Docker Image

```bash
docker build -t localconnect:latest .
```

### Run with Docker

```bash
docker run -e MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/localconnect \
           -e JWT_SECRET=your-secret-key \
           -p 8080:8080 \
           localconnect:latest
```

### Docker Compose (Optional)

Create a `docker-compose.yml`:
```yaml
version: '3.8'
services:
  localconnect:
    build: .
    ports:
      - "8080:8080"
    environment:
      MONGODB_URI: mongodb://mongodb:27017/localconnect
      JWT_SECRET: your-secret-key
    depends_on:
      - mongodb

  mongodb:
    image: mongo:5.0
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db

volumes:
  mongodb_data:
```

Run with: `docker-compose up`

## 📚 API Documentation

### Authentication

All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

### Core Endpoints

**User Management**
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile

**Services**
- `GET /api/services` - List all services
- `POST /api/services` - Create new service (Provider only)
- `GET /api/services/{id}` - Get service details
- `PUT /api/services/{id}` - Update service
- `DELETE /api/services/{id}` - Delete service

**Bookings**
- `POST /api/bookings` - Create new booking
- `GET /api/bookings` - List user bookings
- `GET /api/bookings/{id}` - Get booking details
- `PUT /api/bookings/{id}` - Update booking status
- `DELETE /api/bookings/{id}` - Cancel booking

**Payments**
- `POST /api/payments` - Process payment
- `GET /api/payments/{id}` - Get payment details

**Search & Filter**
- `GET /api/services/search?category=&location=` - Search services

### Postman Collection

Import the Postman collection from `postman-collection/` folder to test all API endpoints with pre-configured requests and environment variables.

## 🧪 Testing

### Run Tests

```bash
./mvnw test
```

### Run Specific Test

```bash
./mvnw test -Dtest=UserServiceTest
```

### Test Coverage

```bash
./mvnw jacoco:report
```

## 🔒 Security Features

- **JWT Authentication**: Stateless token-based authentication
- **Password Encryption**: Spring Security's BCrypt password encoding
- **CORS Protection**: Configurable cross-origin resource sharing
- **SQL Injection Prevention**: Spring Data prevents injection attacks
- **Rate Limiting**: Request throttling for API endpoints
- **HTTPS Support**: Production deployment should use HTTPS

## 📊 Code Quality

The project integrates with SonarQube/SonarCloud for continuous code quality monitoring:

```bash
# Run SonarQube analysis (requires SonarQube server)
./mvnw sonar:sonar -Dsonar.projectKey=local-connect -Dsonar.host.url=http://localhost:9000
```

**Quality Gates:**
- Code coverage > 80%
- Maximum code duplication: 3%
- Security vulnerabilities: 0
- Technical debt: < 5%

## 🔧 Configuration

### Application Properties

Key configurations in `application.yaml`:

```yaml
spring:
  application:
    name: localconnect
  data:
    mongodb:
      uri: ${MONGODB_URI}
      auto-index-creation: true

jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:86400000}

server:
  servlet:
    context-path: /api
  port: ${SERVER_PORT:8080}
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | - |
| `JWT_SECRET` | JWT signing secret | - |
| `JWT_EXPIRATION` | Token expiration time (ms) | 86400000 |
| `SERVER_PORT` | Application port | 8080 |
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | dev |

## 🚦 Development Workflow

1. **Create feature branch:**
   ```bash
   git checkout -b feature/feature-name
   ```

2. **Make changes and commit:**
   ```bash
   git add .
   git commit -m "feat: add new feature"
   ```

3. **Run tests before pushing:**
   ```bash
   ./mvnw clean test
   ```

4. **Push and create pull request:**
   ```bash
   git push origin feature/feature-name
   ```

## 📈 Performance Optimization

- Connection pooling for MongoDB
- Caching strategies with Spring Cache
- Async processing for non-blocking operations
- Database indexing on frequently queried fields
- Load balancing ready architecture

## 🐛 Troubleshooting

### MongoDB Connection Issues

```
Error: MongoTimeoutException
```
- Verify MongoDB URI is correct
- Check network connectivity to MongoDB Atlas
- Ensure IP whitelist includes your IP address

### JWT Token Expired

```
Error: JWT expired
```
- Request a new token using login endpoint
- Refresh token endpoint (if implemented)

### Build Fails

```bash
# Clean Maven cache
./mvnw clean

# Update dependencies
./mvnw dependency:resolve

# Rebuild
./mvnw install
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a pull request

## 📝 License

This project is provided for educational and assessment purposes.

## 📞 Support & Contact

For issues, feature requests, or questions:
- Open an issue on GitHub
- Check existing documentation
- Review Postman collection for API examples

## 🎓 Learning Resources

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [MongoDB University](https://university.mongodb.com/)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8949)
- [RESTful API Design](https://restfulapi.net/)
- [Docker Documentation](https://docs.docker.com/)

## 📅 Project Updates

- **Last Updated:** February 2026
- **Latest Release:** v1.0.0
- **Commits:** 15+

---

**Built with ❤️ for scalable service booking**