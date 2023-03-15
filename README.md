# World API

### Start the project
- Start the database and frontend in a container: `docker-compose -f docker/docker-compose.yml up -d --build`
- Run the API: `sbt run`
- Visit the frontend at `http://localhost:8080`

### Send requests via cURL
Check if the application is up and running:
```
curl -k -v -X GET http://localhost:9000/keepalive
```

#### Cities
Add a city:
```
curl -k -v -X POST http://localhost:9000/add-city \
-H 'Content-Type: text/json' \
-d @- << EOF
{
  "id": "1",
  "name": "Nijmegen",
  "countryCode": "NLD",
  "district": "Gelderland",
  "population": "152463"
}
EOF
```

#### Countries
Get all countries:
```
curl -k -v -X GET http://localhost:9000/get-all-countries
```

Find countries:
```
curl -k -v -X POST http://localhost:9000/countries \
-H 'Content-Type: text/json' \
-d @- << EOF
{
  "country": "U%"
}
EOF
```