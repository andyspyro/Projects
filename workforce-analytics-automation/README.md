# HR Analytics and Automation

> **Type:** Sanitized professional work portfolio  
> **Focus:** Reporting, VBA, Power BI, workflow tracking, reconciliation, and workforce analytics

This section is based on work I actually do with HR data and reporting.

I removed names, IDs, real facilities, employer references, internal email addresses, production exports, and confidential records. The public pages focus on the logic, reporting design, automation, and data quality work.

## Project index

| Project | What I worked on |
|---|---|
| [Hiring Pipeline and Time to Hire](hiring-pipeline-dashboard.md) | Stage timing, current stage, aging, missing dates, next step logic, data reliability, and bottleneck analysis |
| [Vacancy and Workforce Intelligence](vacancy-workforce-intelligence.md) | Filled and vacant positions, vacancy age, trend reporting, hiring activity, and leadership views |
| [VBA Reporting Automation](vba-reporting-automation.md) | Workbook setup, cleanup, refresh, formulas, status logic, dashboards, weekly rollover, and archiving |
| [VBA Code Walkthrough](vba-code-walkthrough.md) | Sanitized code with plain language explanations of why I used each pattern |
| [Data Quality and Reconciliation](data-quality-reconciliation.md) | Missing records, status mismatches, date conflicts, duplicates, salary differences, and source comparison |
| [Compensation and Salary Analysis](compensation-salary-analysis.md) | Salary review, placement checks, approval status, step logic, and internal equity context |
| [Position Control and Classification](position-control-classification.md) | Filled and vacant state, classification checks, recruitment activity, and reporting population validation |
| [Progression and Step Tracking](progression-step-tracking.md) | Current step, next action, expected timing, exceptions, and completed item cleanup |
| [Workflow and Approval Tracking](workflow-approval-tracking.md) | Smartsheet and SharePoint tracking, ownership, approvals, corrections, documents, and follow up |
| [Recurring Reporting and Audits](recurring-reporting-audits.md) | Weekly reports, recurring checks, changed records, closed items, standard definitions, and archive control |
| [Onboarding and Hiring Workflow](onboarding-hiring-workflow.md) | Recruiting stages, screening, references, salary, approval, onboarding, handoffs, and start readiness |

## Actual source proof

I published one sanitized VBA example here:

[archive_completed_records.bas](examples/archive_completed_records.bas)

The larger [VBA Code Walkthrough](vba-code-walkthrough.md) also explains patterns I used in my original macros, including:

* event driven worksheet updates
* dynamic header detection
* flexible column mapping
* dictionary based aggregation
* date and duration logic
* status rules
* data quality checks
* weekly rollover
* archiving
* dashboard rebuilds
* dynamic charts
* error handling and performance controls

## Example data model I verified

One retained hiring tracker includes fields for applicant stage, screening milestones, salary review, approvals, start date, current stage, days in current stage, aging, action needed, and record status.

That is the kind of process the public pages are describing.

## Tools

Excel, VBA, Power BI, Smartsheet, SharePoint, HRIS exports, applicant tracking data, payroll and personnel data, and reporting tools.

## Common workflow

```text
source data
    |
    v
clean and organize
    |
    v
validate
    |
    v
reconcile
    |
    v
calculate metrics
    |
    v
dashboard or tracker
    |
    v
HR action or leadership reporting
```

The hard part is usually not the chart. It is getting the population, status, dates, and business rules right before the chart is built.
