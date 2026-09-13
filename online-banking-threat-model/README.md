# Online Banking Security Architecture and Threat Model

> **Type:** Threat modeling and security architecture coursework  
> **System:** Fictional MyWallet online banking application  
> **Tool:** IriusRisk

I started from a supplied software requirements document and data flow diagram, rebuilt the model in IriusRisk, reviewed the generated threats, and mapped controls back to the system.

No real bank or customer data was used.

## What the assignment required

The retained project instructions specifically required three parts:

1. Design the data flow diagram in IriusRisk.
2. Analyze the threats.
3. Generate the threat model report.

That is the scope I use on this page.

## Architecture work

I mapped users, application processes, SQL data stores, data flows, and trust boundaries.

The supplied banking requirements included functions such as authentication, transfers, account management, bill payment, profile updates, online chat, administrator settings, and logout.

## How I worked through it

```text
software requirements
        |
        v
data flow diagram
        |
        v
IriusRisk components
        |
        v
trust boundaries
        |
        v
generated threats
        |
        v
review and prioritization
        |
        v
security controls
```

## STRIDE review

I also organized the risks with STRIDE.

| Category | Examples I reviewed |
|---|---|
| Spoofing | Credential compromise and user impersonation |
| Tampering | SQL injection and transfer manipulation |
| Repudiation | Disputed transactions and log tampering |
| Information disclosure | Sensitive account data and verbose errors |
| Denial of service | Authentication abuse and transfer service disruption |
| Elevation of privilege | Broken administrator RBAC and object level authorization |

The detailed notes are in [stride-threat-assessment.md](stride-threat-assessment.md).

## Controls I mapped

The control set includes MFA, password hashing, rate limiting, secure sessions, reauthentication for sensitive actions, parameterized SQL, server side validation, least privilege database access, TLS, encryption at rest, RBAC, object level authorization, protected audit logging, monitoring, safer errors, and availability controls.

## Proof in this folder

* [STRIDE threat assessment](stride-threat-assessment.md)
* [Security requirements](security-requirements.md)
* [Threat modeling method](threat-modeling-methodology.md)

These are sanitized notes from the work. I do not publish private course submission files or instructor material.
