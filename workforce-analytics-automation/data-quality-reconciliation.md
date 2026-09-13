# HR Data Quality and Cross System Reconciliation

A large part of my HR analytics work involved figuring out why two systems or reports did not agree.

This project area covered recurring data quality checks and discrepancy investigation across HR, recruiting, payroll, position, and tracking systems.

## Common Issues

I reviewed problems such as:

* missing records
* duplicate records
* mismatched status values
* inconsistent dates
* different salary values
* position status discrepancies
* stale workflow records
* completed actions still appearing open
* records existing in one source but not another

## Reconciliation Process

My normal approach was:

1. identify the field or record that does not match
2. compare the available source systems
3. determine which source is authoritative for that field
4. trace when the value changed
5. identify whether the issue is data entry, timing, workflow, or reporting logic
6. correct the report or route the source issue for resolution
7. document the result

## Audit Controls

I also used recurring review checks to catch issues before they reached leadership reports.

This included validating:

* record completeness
* position information
* salary and step information
* hiring status
* workflow stage
* effective dates
* closed records
* reporting population

## Why it mattered

Data reconciliation is part technical troubleshooting and part business process analysis.

Two values can both be technically correct if they represent different points in the workflow. The job is to understand the process well enough to know whether the difference is expected or an actual data quality problem.
