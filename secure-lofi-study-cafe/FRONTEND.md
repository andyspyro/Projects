# Frontend and real time Design

The original application included EJS templates, a responsive CSS interface, and browser-side JavaScript for Socket.IO and the YouTube IFrame API.

## EJS Views

The UI included separate registration and login views plus the main café view.

The café page displayed:

* the authenticated username and role
* synchronized room playback
* queue and history
* pending music requests for moderators
* online members
* live chat
* temporary moderator/controller state
* CSRF values on state changing actions

EJS escaped normal output by default, which helped keep stored chat content from being interpreted as executable markup.

## real time Client Logic

The browser client used Socket.IO for:

* member presence
* chat messages
* message deletion events
* queue/player state
* controller updates
* vote updates
* pending request updates
* playback synchronization

The player logic tracked a server-time offset and estimated the current room position so users could resynchronize after drift or after returning from another browser tab.

## Safer DOM Updates

Live chat messages were created with DOM methods and assigned with `textContent` instead of inserting the message as HTML.

That mattered because chat content came from users and had to be treated as untrusted.

## Music Synchronization

The client tracked:

* current track
* shared queue
* recent history
* room controller
* vote state
* server time
* local playback position

Users could temporarily listen independently and then rejoin the controller with a sync action.

The implementation also attempted to resynchronize after the browser returned from the background.

## Public Scope

The original local archive also contained the runtime SQLite database and local environment configuration. Those are intentionally excluded from GitHub.
