# Secure Lo Fi Study Cafe

> **Type:** Full stack secure web application  
> **Stack:** Node.js, Express, SQLite, EJS, Socket.IO  
> **Status:** Core backend and database source retained

I built this as a study room app where people could sign in, chat, request music, and share a synchronized player. I also wanted the security controls to be part of the app itself instead of something I added at the end.

## What the app does

* Account registration and login
* Live chat
* Shared music queue
* Music requests with moderator approval
* Synchronized YouTube playback
* Vote to move to the next track
* Temporary moderator and controller roles
* Online member presence
* Message deletion and moderation
* Audit records for privileged actions

## Security I built into it

| Control | How I used it |
|---|---|
| Password storage | bcrypt hashes passwords before they are stored |
| Sessions | Express sessions keep authenticated state on the server side |
| Authorization | Privileged routes check roles on the server |
| CSRF | State changing actions require a session token |
| SQL safety | Queries use placeholders instead of joining user input into SQL |
| Input validation | Registration, chat, titles, and media input have format or length checks |
| XSS reduction | Live chat output is treated as text instead of trusted HTML |
| Rate limiting | Authentication routes have tighter limits than normal traffic |
| Security headers | Helmet sets browser security headers and a content security policy |
| Auditability | Admin and moderation actions are written to the audit table |

## One part I paid attention to

The music request field could accept a YouTube URL, but I did not want the server to trust arbitrary iframe code.

The backend extracts the video ID, checks the host and ID format, reads the optional starting time, and then rebuilds the media target from known values.

That kept the feature useful without accepting arbitrary embed markup.

## Architecture

```text
Browser and EJS views
        |
        +--> HTTP forms and API routes
        |
        +--> Socket.IO events
                 |
                 v
          Express application
                 |
        authentication
        authorization
        CSRF checks
        validation
                 |
                 v
              SQLite
        users
        messages
        requests
        queue
        audit logs
```

## Proof in this folder

* [server.js](server.js) contains the Express routes, sessions, authorization checks, CSRF checks, rate limiting, Socket.IO logic, and player controls.
* [database.js](database.js) contains the SQLite setup and query helpers.
* [schema.sql](schema.sql) shows the database schema in plain SQL.
* [package.json](package.json) shows the main dependencies.
* [FRONTEND.md](FRONTEND.md) documents the EJS, Socket.IO, and player behavior.

## Public safety

The working database and local environment file are not published. The public repo excludes user records, passwords, session data, and local secrets.

The demo bootstrap still makes the first registered account an administrator. That is convenient for local testing, but I would not use that pattern for a public production deployment.
