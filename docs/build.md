## Build production image

```sh
docker build -t esperandio/architecture-patterns-with-java -f Dockerfile.production .
```

## Run container

```sh
docker run -it --rm -p 8000:8080 \
    esperandio/architecture-patterns-with-java
```
