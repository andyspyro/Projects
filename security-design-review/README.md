# Private Data Logging Security Design Review

> **Type:** Secure software architecture review  
> **Scope:** Design review, not a code audit  
> **Status:** Completed coursework project

I reviewed a private data logging design before implementation and looked for architecture problems that could weaken the privacy model.

The retained report confirms the system had separate recorder and viewer components, schema based logging, session based API access, encrypted communication, token based sessions, filtered and unfiltered log views, retention controls, and secure deletion.

## What I found

I focused on two design risks.

### 1. Schema definition abuse

The schema controls how data is classified and stored. If a malicious or compromised client can submit an oversized, recursive, malformed, or badly classified schema, the privacy model can break before normal logging even starts.

I tied that problem to complete mediation, least privilege, and keeping the design simple enough to enforce consistently.

#### Change I proposed

A dedicated schema validation gatekeeper before the recorder.

The gatekeeper would:

* allow only approved field types
* enforce size and structure limits
* reject malformed definitions
* check sensitive and nonsensitive classifications
* make accepted schemas immutable

I marked this as a **Must** in the original review.

### 2. Weak central control for unfiltered access

The design allowed filtered and approved unfiltered views, but the authorization decision was spread across configuration and workflow rules.

#### Change I proposed

A central policy enforcement point and policy decision point for sensitive retrieval.

That design would check role, data classification, approval rules, and time limited access before the unfiltered data is returned.

I also recommended a separate tamper evident audit trail for grants, denials, approvals, and unfiltered views.

I marked this as an **Ought** in the original review.

## Risk table

| Risk | Design change | Priority |
|---|---|---|
| Schema definition injection or storage manipulation | Validation gatekeeper and immutable schema store | Must |
| Weak authorization and audit around unfiltered retrieval | Central policy enforcement and separate audit log | Ought |

## Architecture changes

### Schema path

```text
Application
    |
    v
API
    |
    v
Schema Validation Gatekeeper
    |
    v
Recorder
    |
    v
Immutable Schema and Log Storage
```

### Sensitive retrieval path

```text
Authorized User
      |
      v
Logger Viewer
      |
      v
Policy Enforcement Point
      |
      +--> Policy Decision
      |
      +--> Time Limited Access
      |
      +--> Tamper Evident Audit Log
      |
      v
Filtered or approved unfiltered data
```

## Evidence I checked

I still have the retained Security Design Review report. It includes the same two findings, the Must and Ought priorities, the schema validation recommendation, and the central policy enforcement recommendation.

I am not publishing the original design document or instructor material. This page is the sanitized version of my own review.
