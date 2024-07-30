# Product Fetch Application

## Usage

### Fetch Products

To fetch products, use the following CURL request:

```bash
curl --location 'http://localhost:8080/v1/fetch/product' \
--header 'Content-Type: application/json' \
--data '{
    "productName": "LG 32 inch monitor",
    "sortByPrice": "desc"
}'
```

### Swagger Documentation

Swagger documentation is accessible at:

[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

### H2 Data Persistence

H2 console can be accessed at:

[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

- Database Name: `test`
- Username: `root`
- Password: `admin`

### OpenAI Integration

OpenAI is integrated to fill the description field of the product. Currently, it returns a dummy response for testing purposes.

## Known Issues

1. **Robot Detection**: CAPTCHA verification can cause Amazon scraping to fail sometimes.
2. **OpenAI**: Currently set to return a dummy response for testing purposes.

## SortByPrice

The `sortByPrice` field in the CURL request can be set to either `asc` (ascending) or `desc` (descending) to sort the products by price.
