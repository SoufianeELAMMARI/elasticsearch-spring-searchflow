# Elasticsearch CRUD Spring Boot Project with Swagger

## Prerequisites
- Java 17+
- Maven 3.6+
- Docker Desktop (for Elasticsearch)

## Elasticsearch Setup (Mac)

### Method 1: Docker (Recommended - Easiest)

#### Prerequisites
- Install Docker Desktop for Mac from [docker.com](https://www.docker.com/products/docker-desktop/)

#### Single Node Setup
```bash
# Pull and run Elasticsearch 8.x (recommended for development)
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -e "xpack.security.http.ssl.enabled=false" \
  -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
  docker.elastic.co/elasticsearch/elasticsearch:8.11.0
```

#### Docker Compose Setup (Better for development)
Use the provided `docker-compose.yml` file:

```bash
docker-compose up -d
```

This will start both Elasticsearch and Kibana for data visualization.

#### Docker Management Commands
```bash
# Start Elasticsearch
docker start elasticsearch

# Stop Elasticsearch
docker stop elasticsearch

# View logs
docker logs elasticsearch

# Remove container (when done)
docker rm elasticsearch
```

### Method 2: Homebrew (Alternative)

```bash
# Install Elasticsearch
brew install elasticsearch

# Start Elasticsearch service
brew services start elasticsearch

# Stop Elasticsearch service
brew services stop elasticsearch
```

### Verify Elasticsearch is Running

```bash
# Check if Elasticsearch is running
curl http://localhost:9200

# Expected response should include version info and "You Know, for Search"
```

## Project Setup

1. **Start Elasticsearch** using one of the methods above
2. **Verify Elasticsearch is running**:
```bash
curl http://localhost:9200
```
3. **Clone/download this project**
4. **Run the Spring Boot application**:
```bash
mvn spring-boot:run
```
5. **Test the application** is running:
```bash
curl http://localhost:8080/api/products
```

## Swagger Documentation

Once your application is running, you can access the interactive API documentation:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

The Swagger UI provides:
- Interactive API testing
- Detailed endpoint descriptions
- Request/response examples
- Schema definitions
- Try-it-out functionality for all endpoints

### Swagger Features Added:
- Complete API documentation with descriptions
- Request/response schema definitions
- Parameter validation details
- Example values for all fields
- Organized by tags (Product Management)
- HTTP status code documentation

## API Endpoints

### CRUD Operations
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

### Search Operations
- `GET /api/products/search?name={name}` - Search products by name
- `GET /api/products/category/{category}` - Get products by category
- `GET /api/products/price-range?minPrice={min}&maxPrice={max}` - Get products in price range
- `GET /api/products/category/{category}/max-price/{maxPrice}` - Get products by category under max price

## Sample Requests

### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
-H "Content-Type: application/json" \
-d '{
  "name": "MacBook Pro",
  "description": "Apple MacBook Pro 14-inch",
  "price": 2399.99,
  "category": "Electronics",
  "stock": 10
}'
```

### Search Products
```bash
curl "http://localhost:8080/api/products/search?name=MacBook"
```

### Get Products by Price Range
```bash
curl "http://localhost:8080/api/products/price-range?minPrice=1000&maxPrice=3000"
```

## Troubleshooting

### Common Issues

1. **Port already in use**
```bash
# Check what's using port 9200
lsof -i :9200

# Kill process if needed
kill -9 <PID>
```

2. **Elasticsearch not responding**
```bash
# Check Docker container status
docker ps

# View Elasticsearch logs
docker logs elasticsearch
```

3. **Memory issues**
```bash
# Increase Docker memory in Docker Desktop settings
# Go to Docker Desktop > Preferences > Resources > Memory
# Set to at least 4GB
```

4. **Connection refused from Spring Boot**
   - Ensure Elasticsearch is running on localhost:9200
   - Check that security is disabled (for development)
   - Verify no firewall is blocking the connection

## Quick Start Guide

1. **Install Docker Desktop**
2. **Start Elasticsearch**:
```bash
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -e "xpack.security.http.ssl.enabled=false" \
  -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
  docker.elastic.co/elasticsearch/elasticsearch:8.11.0
```
3. **Wait 30-60 seconds** for Elasticsearch to start
4. **Test connection**: `curl http://localhost:9200`
5. **Run the Spring Boot app**: `mvn spring-boot:run`
6. **Open Swagger UI**: http://localhost:8080/swagger-ui.html
7. **Test the API** using Swagger's interactive interface

## Features
- Full CRUD operations with Elasticsearch
- Advanced search capabilities (text search, filters, ranges)
- Data validation using Bean Validation
- RESTful API design following best practices
- Interactive API documentation with Swagger/OpenAPI 3
- Docker-based development environment
- Comprehensive error handling and status codes