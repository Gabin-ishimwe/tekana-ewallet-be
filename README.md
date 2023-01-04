[![Tekana-eWallet CI/CD pipeline](https://github.com/Gabin-ishimwe/tekana-ewallet-be/actions/workflows/maven.yml/badge.svg)](https://github.com/Gabin-ishimwe/tekana-ewallet-be/actions/workflows/maven.yml)
# Tekana eWallet Project

This is a backend infrastructure of an eWallet platform that is used by people worlwide to transfer money


## Backend Design Strategy

Designed and documented the backend design strategy that will be used and followed through this project, with well detailed information of what is expected and be done to launch it in production, be used to the customers.

Attached to this section a link to the desing document

[Design Strategy](https://docs.google.com/document/d/1gWmuQWmBuDcvbCcLbAKUUbRWPqDpPD1mLxfPxXMWPig/edit?usp=sharing)


## Deployment

#### Backend API
```bash
  <HEROKU_LINK>
```

#### API Documentation (Swagger)
```bash
  <HEROKU_LINK> SWAGGER
```

#### Postman Documentation
```bash
  https://documenter.getpostman.com/view/19575892/2s8Z72WCHs
```


## Database Design
![Database Design Screenshot](./Database%20Design.png)

## API endpoints

| HTTP Method | Endpoint     | Description                |
|:------------| :------- | :------------------------- |
| `POST`      | `/api/v1/auth/register` | Endpoint to create new user and account |
| `POST`      | `/api/v1/auth/login` | Endpoint to login user in the application |
| `GET`       | `/api/v1/auth/profile` | Endpoint to get user's detail informations |
| `GET`       | `/api/v1/auth/users` | Endpoint to get all users in the application (only accessible by `ADMIN`) |
| `GET`       | `/api/v1/account/balance` | Endpoint to view balance amount on user's account |
| `POST`      | `/api/v1/account/deposit` | Endpoint to deposit money on user's account |
| `POST`      | `/api/v1/account/withdraw` | Endpoint to withdraw money on user's account |
| `POST`      | `/api/v1/account/transfer` | Endpoint to transfer money from one account to another |
| `GET`       | `/api/v1/transaction` | Endpoint to get all transactions made on user's account like deposit, withdraw and transfer |
| `GET`       | `/api/v1/transaction/{transaction_id}` | Endpoint to get one transaction made on user's account |
| `POST`      | `/api/v1/role?userId={user_id}&roleId={role_id}` | Endpoint to giver a user specific role (only accessible by `ADMIN`) |
| `PUT`       | `/api/v1/role?userId={user_id}&roleId={role_id}` | Endpoint to remove a specific role from user (only accessible by `ADMIN`) |

## Features

- Sign in / Login
- Deposit Money on Account
- Withdraw Money on Account
- Transfer Money on other Account
- View all Transactions made
- View one Transaction


## Test API
To test those APIs you can use one of the deployment links provide above. There are some seed data provided to make this easy, you can use them to login and use their credentials to test out all the application features.

| Entity | Data     | Description                |
| :-------- | :------- | :------------------------- |
| `User Entity` | `{email: john@gmail.com, password: #Password123}` | This user has role `USER`, you can use them to test our almost all features|
| `User Entity` | `{email: angel@gmail.com, password: #Password567}` | This user has role `ADMIN`, you can use them to test out admin credential feature|

## Tech Stack

**Server:** Java, Spring Boot

**Database:** PostgreSQL

**Testing:** JUnit, MockMvc, RestTemplate, Test Containers


## Authors

- [Gabin Ishimwe](https://github.com/Gabin-ishimwe)

