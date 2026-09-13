# Hiring Pipeline and Time to Hire Dashboard

I built a recruitment workflow dashboard to track how selected candidates moved from application through interview, screening, salary review, offer, onboarding, and start.

The goal was to make delays visible instead of relying on manual status checks.

## What the Dashboard Tracked

The model included milestones such as:

* application received
* interview
* screening initiated
* screening completed
* reference request
* reference completion
* salary review
* salary approval
* offer
* onboarding scheduled
* start date
* final status

## Calculated Metrics

I built logic for:

* total time to hire
* business day turnaround
* days between individual stages
* current stage
* current stage aging
* next expected step
* missing milestone dates
* date sequence validation
* process completion
* closed status
* stuck candidate flags
* monthly and quarterly trends

## Data Reliability

One part I focused on heavily was whether the dashboard should trust each record.

I added checks for:

* missing required dates
* impossible date order
* incomplete process history
* conflicting status values
* stale records

This let the dashboard distinguish a real operational delay from a record that simply had incomplete data.

## Dashboard Views

The executive view was designed to answer questions such as:

* Where are candidates sitting the longest?
* Which stage is creating the most delay?
* How many candidates are currently active?
* What step should happen next?
* Which records need data cleanup before they should be used in reporting?
* How has hiring throughput changed over time?

## Why it mattered

Time to hire is not one number.

A useful dashboard has to break the total timeline into stages so the team can tell whether the delay is in interviewing, screening, compensation review, approval, scheduling, or data entry.
