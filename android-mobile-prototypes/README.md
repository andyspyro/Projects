# Android Mobile Prototypes

> **Type:** Native Android prototypes  
> **Tools:** Kotlin, Jetpack Compose, Material 3, Android Studio  
> **Status:** Reply Time Tracker source retained; HR Tracker source not retained

I built these to get out of browser only projects and work directly with Android state, forms, timers, and device testing.

## Reply Time Tracker

This is the project I can verify from retained source.

The app lets a user create multiple message timers, record when a message was sent, optionally record a reply, and keep a live timer running when there is no reply yet.

### What the retained Kotlin source shows

* `ComponentActivity` with `setContent`
* Jetpack Compose UI
* Material 3 cards, buttons, and text fields
* `mutableStateListOf` and Compose state
* a `LazyColumn` for multiple entries
* input validation for dates and times
* a coroutine timer that refreshes every second
* average response time
* longest wait or response time
* delete actions for individual trackers
* several accepted date and time formats

I published a sanitized core sample here: [ReplyTimeTrackerCore.kt](ReplyTimeTrackerCore.kt).

## HR Tracker Dashboard

I also built and tested a mobile HR workflow prototype.

The prototype modeled candidate intake, hiring stages, archive handling, summary counts, and bottleneck visibility.

I do **not** have the original HR Tracker source anymore, so I am not publishing reconstructed code and calling it original. The public page only documents the interface and workflow I tested.

## Why I kept both here

The Reply Time Tracker proves the native Android work with retained code. The HR Tracker shows how I tried to move a workflow I already understood from spreadsheets and dashboards into a smaller mobile interface.

No real employee, applicant, or personal contact data is included in the public project.
