# Demo: Spring Boot E-commerce Backend

A REST backend for a small online store, built from scratch as a hands-on way to learn
Spring Boot. Every concept in here (security, JPA relations, transactions, Stripe, scheduling)
was learned first and then implemented, so some parts are intentionally unfinished; see
[Known limitations](#known-limitations).

## Features

- **Auth**: signup with email verification (5-minute token), form login, roles `USER` / `ADMIN`, BCrypt passwords, CSRF protection
- **Catalog**: products and categories; public reads, admin-only writes
- **Cart**: add items per user, quantities merge for the same product
- **Orders**: created from the cart with a stock check; price is captured at purchase time
- **Concurrency**: `@Version` optimistic locking on `Product` to prevent overselling
- **Payments**: Stripe Checkout session per order, plus a signature-verified webhook that marks the order `PAID`
- **Ownership checks**: users can only pay for and view their own orders
- Global exception handling, DTOs, mappers

## Tech stack

Java 17, Spring Boot 3.5, Spring Security, Spring Data JPA (Hibernate), MySQL, Stripe Java SDK, Spring Mail (Gmail SMTP), Lombok, Maven

## Getting started

### Prerequisites
- JDK 17+
- MySQL running locally with an empty database (tables are created by Hibernate: `ddl-auto=update`)
- A Stripe account (test mode) and the [Stripe CLI](https://stripe.com/docs/stripe-cli)
- A Gmail account with an [app password](https://support.google.com/accounts/answer/185833) for sending verification emails

### Configure
`src/main/resources/application.properties` ships with placeholders. Fill in your own values
locally and **do not commit them**:

| Property | What to put |
|---|---|
| `spring.datasource.url` | e.g. `jdbc:mysql://localhost:3306/productsdb` |
| `spring.datasource.username` / `password` | your MySQL credentials |
| `stripe.secret.key` | Stripe test secret key (`sk_test_...`) |
| `stripe.webhook.secret` | printed by `stripe listen` (`whsec_...`) |
| `spring.mail.username` / `password` | Gmail address and app password |

### Run
```bash
./mvnw spring-boot:run
```
The API starts on `http://localhost:8080`.

### Stripe webhook (local)
```bash
stripe listen --forward-to localhost:8080/stripe/webhook
```
Copy the `whsec_...` it prints into `stripe.webhook.secret`. Pay with the test card `4242 4242 4242 4242`.

## API overview

| Method | Path | Access | Purpose |
|---|---|---|---|
| POST | `/auth/signup` | public | Register (`username`, `email`, `password`); sends verification email |
| GET | `/auth/verify?token=` | public | Verify email |
| POST | `/login` | public | Form login (`username`, `password`) |
| GET | `/csrf` | public | Get CSRF token for state-changing requests |
| GET | `/product/getall`, `/product/getbyid/{id}` | public | Browse products |
| POST | `/product/create` | admin | Create product (`name`, `description`, `stock`, `price`, `categoryId`) |
| PUT | `/product/update/{id}`, `/product/stockupdate/{id}` | admin | Update product / restock |
| DELETE | `/product/delete/{id}` | admin | Delete product |
| POST | `/category` | logged in | Create category (see limitations) |
| POST | `/cart/add` | user | Add to cart (`productId`, `quantity`) |
| GET | `/cart/get` | user | View own cart |
| POST | `/order/create` | user | Turn the cart into an order |
| GET | `/order/getbyid/{id}` | owner or admin | View an order |
| GET | `/order/getall` | admin | List all orders |
| PUT | `/order/update/{id}` | admin | Change order status |
| DELETE | `/order/delete/{id}` | admin | Delete an order |
| POST | `/stripe/payment/{orderId}` | owner | Returns a Stripe Checkout URL |
| POST | `/stripe/webhook` | Stripe only | Payment result callback |
| GET/POST/PUT/DELETE | `/user`, `/user/{id}` | admin | User management |

## Typical flow

1. `POST /auth/signup`, then open the verification link from the email
2. `POST /login`
3. `POST /cart/add`, then `POST /order/create`
4. `POST /stripe/payment/{orderId}`, open the returned URL, pay with the test card
5. The webhook marks the order `PAID`

## Known limitations

- **Pending-order sweeper is disabled.** `SchedulingTrigger` and `OrderCancellationService`
  are commented out, along with `@EnableScheduling` in `DemoApplication`. Uncommenting them
  cancels `PENDING` orders older than 30 minutes and restocks them, but it has a bug: if a
  customer pays after the sweep has cancelled the order, the webhook marks the cancelled order
  `PAID` even though its stock was already returned. While it is off, unpaid orders keep their
  stock reserved until cancelled manually.
- No frontend in this repo (CORS is set up for `http://localhost:5173`).
- No automated tests yet.
- No password reset or resend-verification endpoint.
- No shipping address, refunds, user-initiated cancellation or "my orders" list.
- No pagination on list endpoints.
- Localhost URLs are hardcoded for the Stripe redirect and the verification email link.
- Stripe success/failure redirects point at placeholder `/test/...` pages.
- A few `System.out` calls are used instead of a logger.

## Possible next steps

Tests (signup/verify, order ownership, stock concurrency), fix and re-enable the sweeper,
externalize base URLs, password reset, pagination, a frontend.
Handle checkout.session.expired and remove the scheduler.
