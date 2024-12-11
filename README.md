# RecettApp

## Start Docker
```
cd ./api_recettapp
docker-compose up
```
Check that port 5432 is not already in use (by a local postgresql, for example)

## Setup Instructions for Keycloak with Docker and Database

### 1. Launch Docker and Backend

- Use the following command to start Docker:
  ```bash
  docker-compose up
  ```
- Launch the backend so that JPA generates the application database.

### 2. Create the Database Role and Schema

- Run the following script to create the database role and schema:
  ```sql
  CREATE ROLE keycloak WITH
    SUPERUSER
    CREATEDB
    CREATEROLE
    INHERIT
    LOGIN
    PASSWORD 'keycloak';
  CREATE DATABASE keycloak
    WITH OWNER = keycloak;
  GRANT ALL on database keycloak to keycloak;
  GRANT CONNECT ON DATABASE keycloak TO keycloak;
  GRANT ALL PRIVILEGES ON DATABASE keycloak to keycloak;
  ```

### 3. Create Keycloak Schema

- Access the `keycloak` database and run this script:
  ```sql
  CREATE SCHEMA keycloak;
  ```

### 4. Restart Docker

- Stop Docker and restart it to ensure Keycloak detects its database.

### 5. Access Keycloak Administration

- Open your browser and go to: [http://localhost:8081](http://localhost:8081).
- Log in with the following credentials:
  - **Username:** `admin`
  - **Password:** `admin`

### 6. Configure a New Realm

- Create a new realm.
- Import the configuration file located in the backend's `keycloak_export` folder under `main resources`.

### 7. Check Frontend Keycloak Versions

- Open the `package.json` file in the frontend and verify the following dependencies:
  - **keycloak-js:** Ensure it is version `25.0.6`. If not, run:
    ```bash
    npm uninstall keycloak-js
    npm install keycloak-js@25.0.6
    ```
  - **keycloak-angular:** Ensure it is version `15.3.0`. If not, run:
    ```bash
    npm uninstall keycloak-angular
    npm install keycloak-angular@15.3.0
    
