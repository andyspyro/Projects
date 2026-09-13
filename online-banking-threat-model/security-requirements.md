# Security Requirements

These are the security requirements I pulled from the fictional MyWallet design, plus additional controls that came out of the threat model.

## Authentication

* Require a valid account before access to authenticated functions.
* Require an authenticated session before banking transactions.
* Lock or slow repeated failed login attempts.
* Use MFA for privileged users and higher risk actions.
* Store passwords with a modern adaptive password hash.
* Detect automated login attempts and credential stuffing.

## Authorization

* Check authorization on the server for every privileged action.
* Use RBAC for administrator functions.
* Check object ownership for account and transaction resources.
* Never treat a user supplied account ID as proof of authorization.

## Input and Database Handling

* Validate input server side.
* Use parameterized SQL for values.
* Allowlist dynamic SQL identifiers when they cannot be parameterized.
* Run the application with least privilege database accounts.
* Do not expose detailed database errors to users.

## Transaction Integrity

* Validate the source account, destination account, amount, ownership, and limits on the server.
* Require additional verification for high risk transfers.
* Record completed transactions.
* Keep transaction status available to the customer.

## Sensitive Data

* Use TLS for data in transit.
* Encrypt sensitive data at rest.
* Protect encryption keys separately from the protected data.
* Limit access to customer data by role and function.

## Logging and Monitoring

* Record security relevant events such as login failures, privilege changes, and high risk transactions.
* Centralize logs where practical.
* Protect audit logs from modification.
* Alert on suspicious authentication and transaction patterns.

## Error Handling

* Return simple errors to users.
* Keep detailed diagnostics in protected internal logs.
* Disable verbose production stack traces and database errors.

## Availability

* Rate limit abusive requests.
* Use DDoS protections and traffic filtering.
* Plan for load balancing/capacity needs.
* Keep recovery and incident response procedures.

## Session Handling

* End the authenticated session on logout.
* Use secure session cookies.
* Reauthenticate when an action is sensitive enough to justify it.

## Main Design Rule

No single control should carry the whole system. Authentication, authorization, validation, encryption, logging, and least privilege should back each other up.
