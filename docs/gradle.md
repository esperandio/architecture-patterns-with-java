## View the structure of a multi-project build

```sh
gradle -q projects
```

## Run the application

```sh
gradle -q run
```

## Run tests

```sh
./gradlew test
./gradlew -p persistence test --tests "OptimisticConcurrencyTest*" --parallel
```