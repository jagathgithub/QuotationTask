
A Spring Boot REST API that generates random quotes with rate limiting support.

---

## 1. Setup Instructions
- Java 17 or higher  
- Maven 3.8.x or higher  
- Git  

Clone Repository:
```bash
git clone https://github.com/jagathgithub/RandomQuotesGeneration.git
cd RandomQuotesGeneration
```

---

## 2. Build & Run Steps
```bash
mvn clean install
mvn spring-boot:run
```

---

## 3. Application Configuration
Configure rate limiting in `src/main/resources/application.properties`:

```properties
# Maximum number of requests allowed per client
rate.limit.capacity=5

# Refill duration (e.g., 1M = 1 Minute, 10S = 10 Seconds)
rate.limit.refill=1M
```

- **rate.limit.capacity**: maximum allowed requests per client within the refill duration  
- **rate.limit.refill**: time window to reset the request quota  

---

## 4. Testing Examples

### Using curl
```bash
# Get a random quote
curl http://localhost:8089/api/quote

# Test rate limiting (run multiple times quickly)
for i in {1..6}; do curl http://localhost:8089/api/quote; echo ""; done
```

### Using Postman
- **Method**: GET  
- **URL**: http://localhost:8089/api/quote

**Response (Success):**
```json
{
  "quote": "The only way to do great work is to love what you do. - Steve Jobs"
}
```

**Response (Rate Limit Exceeded):**
```json
{
  "error": "Rate limit exceeded. Try again in 60 seconds."
}
```

---

## 5. Assumptions & Design Rationale
- **Rate Limiting**: Implemented using Bucket4j for reliable and thread-safe control.  
- **In-Memory Storage**: Quotes stored in a static list—no persistence required.  
- **Logging**: SLF4J with Logback set up for request/response logging.  
- **Swagger**: Enabled for documentation at `/swagger-ui.html`.  
- **Concurrency**: ConcurrentHashMap + Bucket4j used for handling multiple concurrent access.  
- **Custom Rate Limits**: Fully configurable via application.properties.  
- **Unit Tests**: Focused on rate-limiting logic and quote retrieval functionality.  

---

## 6. Running Tests
```bash
mvn test
```

This runs all unit tests, including validation of rate-limiting behavior and API responses.

---

## 7. Endpoint URL
- **Base URL**: http://localhost:8089/api/quote  
- **Swagger UI**: http://localhost:8089/swagger-ui.html  
