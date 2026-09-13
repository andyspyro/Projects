# STRIDE Threat Assessment

These are the 12 risks I selected for the fictional MyWallet banking system: two from each STRIDE category.

## Spoofing

### Credential compromise during login

**Impact**

* account takeover
* fraudulent transfers
* exposure of personal/financial data

**Controls**

* MFA
* rate limiting
* credential stuffing detection
* strong password hashing
* sensible lockout/backoff rules

### User impersonation during transaction activity

**Impact**

* fraudulent actions appear to come from a legitimate user
* harder incident investigation
* customer disputes

**Controls**

* secure session handling
* reauthentication for high risk actions
* Secure/HttpOnly cookies
* appropriate device/session telemetry

## Tampering

### SQL injection against account data

**Impact**

* unauthorized balance or record changes
* deleted/corrupted data
* broader database compromise

**Controls**

* prepared statements
* server side validation
* least privilege DB access
* WAF only as an extra layer, not the primary fix

### Transfer manipulation

**Impact**

* changed amount or recipient
* unauthorized movement of funds

**Controls**

* server side transaction validation
* TLS
* confirmation controls for higher risk transfers

## Repudiation

### User denies performing a transaction

**Impact**

* disputes
* poor auditability
* compliance/legal problems

**Controls**

* protected audit logs
* timestamps and request metadata
* stronger verification for sensitive actions

### Transaction logs are modified

**Impact**

* fraud can be hidden
* forensic evidence is weakened

**Controls**

* centralized logging
* restricted log access
* tamper resistant storage
* monitoring for unexpected changes

## Information Disclosure

### Sensitive account settings are exposed

**Impact**

* PII exposure
* identity theft risk
* regulatory/reputation impact

**Controls**

* encryption at rest
* TLS
* key management
* least privilege access

### Internal errors leak system details

**Impact**

* database/schema information becomes visible
* attackers get better reconnaissance

**Controls**

* generic user facing errors
* detailed internal logging
* no production stack traces

## Denial of Service

### Authentication abuse

**Impact**

* service disruption
* account lockouts
* increased credential attack pressure

**Controls**

* rate limiting
* bot detection
* careful lockout design
* suspicious IP monitoring

### Transfer service DDoS

**Impact**

* customers cannot transfer funds
* operational/reputational impact

**Controls**

* upstream DDoS protection
* load balancing
* filtering
* incident response procedures

## Elevation of Privilege

### Broken administrator RBAC

**Impact**

* unauthorized admin functions
* configuration or data changes
* possible full compromise

**Controls**

* strict RBAC
* server side authorization checks
* least privilege
* regular privilege review

### Broken object level authorization

**Impact**

* one user can access or change another user's data

**Controls**

* check object ownership on every request
* do not trust direct identifiers alone
* test authorization separately from authentication

## Takeaway

STRIDE helped me turn the architecture into concrete questions. The useful part was tying each threat to a real asset, an impact, and a control instead of just labeling components with categories.
