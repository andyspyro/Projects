# How I Built the Threat Model

I used the software requirements as the starting point and worked outward from there.

## 1. Identify the Main Parts of the System

I listed the main actors, processes, and data stores.

That included:

* customers
* administrators
* support users
* authentication
* account management
* transfers
* bill payment
* transaction logging
* profile/settings updates
* SQL databases

The first question was simple: **where does sensitive data enter, move, and get stored?**

## 2. Draw the Data Flow

I used a DFD to separate:

* external users,
* application processes,
* data stores,
* data flows,
* trust boundaries.

Trust boundaries were the most important part for me because they show where the system should stop trusting input and enforce a control.

## 3. Apply STRIDE

For each major part of the design, I asked:

| STRIDE | Question |
|---|---|
| Spoofing | Can someone pretend to be another user? |
| Tampering | Can someone change data or system state? |
| Repudiation | Could an action happen without reliable evidence of who did it? |
| Information Disclosure | Could sensitive data leak? |
| Denial of Service | Could someone prevent normal use of the system? |
| Elevation of Privilege | Could a user gain permissions they should not have? |

## 4. Prioritize the Risks

I paid more attention to threats that could lead to:

* account takeover,
* unauthorized transfers,
* PII exposure,
* admin compromise,
* loss of audit logs,
* or service outages.

The point was not to give every theoretical threat the same weight.

## 5. Map Each Risk to a Control

Examples:

* credential theft → MFA and rate limiting
* SQL injection → prepared statements and least privilege DB access
* IDOR → object level authorization
* admin abuse → RBAC and privilege reviews
* log tampering → centralized/tamper resistant logging
* information leakage → safer error handling
* DDoS → traffic filtering and capacity controls

## 6. Check the Controls Against Each Other

A control does not automatically solve the whole problem.

For example:

* authentication does not replace authorization,
* input validation does not replace parameterized SQL,
* encryption does not stop privilege abuse,
* logging is weak if the same attacker can modify the logs.

That was probably the most useful part of the exercise. Security makes more sense when the controls are treated as layers instead of isolated checkboxes.
