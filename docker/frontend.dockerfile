### Build the application
FROM node:24.8.0-bookworm-slim AS builder

WORKDIR /build
# Download the dependencies
COPY frontend/package.json frontend/package-lock.json .
RUN --mount=type=cache,target=/root/.npm npm ci

# Build the application
COPY frontend/ .
RUN npm run build

### Run the application on Apache (aka httpd)
FROM httpd:2.4

# Add the compiled Angular application
COPY --from=builder /build/dist/openrunning/* /usr/local/apache2/htdocs/

# Configuration files of the httpd server
COPY docker/files/frontend-htaccess /usr/local/apache2/htdocs/.htaccess
COPY docker/files/frontend-httpd.conf /usr/local/apache2/conf/httpd.conf
