# Crypto-API

## How to run
All the necessary scripts are located under the `scripts` directory.

### Prerequisites

- JDK 11
- Docker (only required to run it with Docker)

### Manually
Simply run `run_local.sh`. By default, Tomcat will run on the port 8080.

### Docker
Run `build_deploy_docker.sh` this will automatically build and run the image on a container and will print the port 
number. This requires that you first run `generate_jar.sh` This script needs to be run from the project root.

### Generating Jar
Run `generate_jar.sh`. 

## Sample requests

### Place order

```
curl -XPOST 'localhost:8080/orders/' \
-H 'Content-Type: application/json' \
-d '{
    "user_id": 1,
    "coin": {
        "currency_id": 1,
        "quantity": "2.0000"
    },
    "price_per_coin": 70,
    "type": "sell"
}' -v
```

This request has the -v option so it's easier to see the Location header with the URI of the placed order (including its id)

### Cancel order
```shell
curl -XDELETE 'localhost:8080/orders/b4ddfca4-5636-3bdd-8ed9-c769e27a9941'
```

### Get orders for live dashboard
```
curl 'localhost:8080/orders/'
```

### List available currencies
```
curl 'localhost:8080/currencies/'
```


## Points left out of design / assumptions

### Balance validation

Usually when adding a transaction to a ledger there needs to be a counterpart for each operation (i.e.: when buying coin, 
there needs to be currency extracted from the wallet to pay for that). This was not part of the original requirements but 
there's an initial attempt in that direction by including the attribute `transaction`. Considering this, there's no balance validation
to check if the user actually has the necessary funds to place the order.

### Concurrency checks 
The order placement service doesn't contemplate race conditions (for example, price changes are not checked or insufficient balance)

### Permissions
This service doesn't contain a security layer. If it had, two changes should take place: the userId should be extracted from the 
provided credentials (instead of the request body like it's being taken now), and there should be a @PreAuthorize annotation
to validate the user has the necessary authorities to place the order (i.e.: `order:create`)

### Error handling

Currently this API is using Spring's default error payload, which for the exercise seems like a suitable option, but the downside of it
is that it exposes internal classes names, stack trace that could result in undesired information disclosure. This should be 
replaced by a custom layer. This would also allow to improve integration tests and validate payload/error messages.

### Response payload upon creation

The order placement doesn't return a payload upon completion. This could be easily changed, I simply didn't add it because I
couldn't find any use for it with the current scope. 

### Order lifecycle validations

Through an `EntityListener` the order transitions between status could be validated in a central point and avoid undesired 
states (i.e.: a cancelled order cannot be completed) 

### Live order dashboard

Regarding this endpoint a few changes should take place with a real application: 
- Depending on the throughput this it receives, it could be convenient to extract reports to a different 
database in a denormalised form (now the calculations are done on the fly)
- Once an order is placed/cancelled, a message should be published on a events bus, this would enable the first option to work 
optimally.
- It'd possibly offer filtering capabilities to avoid querying too much data (i.e.: based on creation date)
- Separation of concerns between backend and frontend: I wouldn't return the info already grouped, but give that option of 
querying the data instead in different ways to allow the frontend to change without requiring bespoke services in the backend. 
- I'd also probably query the data directly instead of using a Hibernate `@Entity` with a `@Subselect` (since it uses 
reflection to instantiate the classes which is not a good option considering performance)  

### Extra endpoints

I decided to add coins list endpoint since this information will be necessary to select the type of coin desired to buy (and the currency to pay for it)