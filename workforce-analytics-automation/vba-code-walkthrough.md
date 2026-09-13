# VBA Code Walkthrough

This page shows the kind of VBA I actually wrote for HR analytics and reporting work.

The examples are sanitized from my original code. Names, IDs, employer specific labels, facility names, and production data are not included.

I am explaining the code the way I would explain it to a coworker who understands the HR process but does not live in VBA all day.

## 1. Making the workbook set itself up safely

A lot of my macros start with the same pattern:

```vb
Option Explicit

Public Sub BuildTracker()

    Dim oldCalc As XlCalculation

    On Error GoTo CleanFail

    Application.ScreenUpdating = False
    Application.EnableEvents = False

    oldCalc = Application.Calculation
    Application.Calculation = xlCalculationManual

    BuildLists
    BuildTemplate
    RefreshDashboard

CleanExit:
    Application.Calculation = oldCalc
    Application.EnableEvents = True
    Application.ScreenUpdating = True
    Exit Sub

CleanFail:
    MsgBox "Tracker setup stopped: " & Err.Description, vbExclamation
    Resume CleanExit

End Sub
```

### What this is doing

**`Option Explicit`** makes VBA require me to declare variables before I use them.

That catches simple mistakes like typing `lastrow` in one place and `lastRow` somewhere else.

**`ScreenUpdating = False`** stops Excel from repainting the screen after every little change. If a macro is creating sheets, adding formulas, formatting cells, and building charts, constantly redrawing Excel makes it noticeably slower.

**`EnableEvents = False`** keeps Excel from triggering other event based macros while this macro is already changing cells. Without that, one automated change can accidentally trigger another macro, which changes another cell, which can trigger the first macro again.

**Manual calculation** temporarily stops Excel from recalculating the whole workbook after every edit. I save the user's original calculation setting and put it back when the macro finishes.

The `CleanExit` and `CleanFail` sections are there so Excel gets put back into a normal state even if the macro hits an error.

That part matters. A macro that fails while events are disabled can leave the workbook acting strange until Excel is restarted.

## 2. Position control: turning rows into actual status logic

One of my position control tools had three main states:

* filled
* actively being hired
* vacant

The logic followed a priority instead of trusting somebody to type the correct status manually.

A sanitized version of the decision looks like this:

```vb
If employeeName <> "" Then
    statusCell.Value = "Filled"
    statusCell.Interior.Color = RGB(0, 176, 80)

ElseIf hiringFlag = "Yes" Then
    statusCell.Value = "In Hiring Process"
    statusCell.Interior.Color = RGB(237, 125, 49)

Else
    statusCell.Value = "Vacant"
    statusCell.Interior.Color = RGB(192, 0, 0)
End If
```

### Why I used this logic

The employee name was the strongest signal.

If a valid employee was attached to the position, I did not want a stale hiring flag to make the same position appear vacant.

So the order was intentional:

```text
employee exists?
    yes -> Filled
    no
     |
     v
active hiring flag?
    yes -> In Hiring Process
    no  -> Vacant
```

The color was not the real logic. The color was just a quick visual result of the status that VBA had already calculated.

## 3. Automatically updating the row when the user changes data

I also used worksheet events so the user did not have to remember to press a refresh button every time a row changed.

The original project used logic like:

```vb
Private Sub Worksheet_Change(ByVal Target As Range)

    Dim changedRange As Range
    Dim cell As Range

    On Error GoTo SafeExit

    Set changedRange = Intersect(Target, Me.Range("A:G"))
    If changedRange Is Nothing Then Exit Sub

    Application.EnableEvents = False

    For Each cell In changedRange.Cells
        If cell.Row >= 2 Then
            UpdateOneStatus Me, cell.Row
        End If
    Next cell

    BuildDashboard Me

SafeExit:
    Application.EnableEvents = True

End Sub
```

### What I was solving

If someone changed a name, date, job classification, or hiring indicator, I wanted the status and dashboard to reflect it without making them run a separate process.

**`Intersect`** checks whether the user changed a cell I actually care about.

That prevents the macro from rebuilding everything because somebody changed an unrelated note or formatting cell.

Then I only recalculate the affected row before rebuilding the summary.

## 4. Finding the real last row instead of hard coding it

A tracker does not always stop at row 200 or row 500.

For one project I checked several important columns and used whichever one extended farthest:

```vb
lastA = ws.Cells(ws.Rows.Count, "A").End(xlUp).Row
lastB = ws.Cells(ws.Rows.Count, "B").End(xlUp).Row
lastC = ws.Cells(ws.Rows.Count, "C").End(xlUp).Row
lastD = ws.Cells(ws.Rows.Count, "D").End(xlUp).Row

GetLastRow = Application.WorksheetFunction.Max( _
    lastA, lastB, lastC, lastD)
```

### Why not just look at one column?

Because a partially completed row might have data in one field but not another.

If I only checked column A and column A happened to be blank on the last real record, the macro could stop early and ignore valid information farther down the sheet.

Using the maximum of several important columns made the scan more tolerant of incomplete records.

## 5. Vacancy age buckets

I used the vacancy start date to calculate how long a position had been open.

The original logic looked like this:

```vb
If IsDate(vacancyDate) Then

    ageDays = Date - CDate(vacancyDate)
    totalVacancyAge = totalVacancyAge + ageDays

    If ageDays <= 30 Then
        age0_30 = age0_30 + 1

    ElseIf ageDays <= 60 Then
        age31_60 = age31_60 + 1

    ElseIf ageDays <= 90 Then
        age61_90 = age61_90 + 1

    Else
        age90Plus = age90Plus + 1
    End If

End If
```

### Why I grouped the dates

A list of 100 vacancy ages is not very useful to someone trying to decide where attention is needed.

The buckets turn individual dates into a quick workload view:

```text
0 to 30 days    newer vacancy
31 to 60 days   aging
61 to 90 days   older
90+ days        long running vacancy
```

I still kept the actual number of days. The bucket was just another way to summarize it.

## 6. Using dictionaries to count categories without knowing them ahead of time

I used `Scripting.Dictionary` when I wanted to count vacancies by a category that could change, such as organizational group or job class.

```vb
Set vacantByGroup = CreateObject("Scripting.Dictionary")

If groupName <> "" Then

    If vacantByGroup.Exists(groupName) Then
        vacantByGroup(groupName) = vacantByGroup(groupName) + 1
    Else
        vacantByGroup.Add groupName, 1
    End If

End If
```

### What that means

I did not have to write:

```text
Group A = 0
Group B = 0
Group C = 0
...
```

The dictionary builds the list as it encounters data.

If it sees a group for the first time, it creates it with a count of 1.

If it sees it again, it adds 1.

That made the dashboard work even when categories changed.

## 7. Hiring pipeline: finding columns even when the export changes

One of the more advanced macros did not assume that "Application Date" would always be in column H or that every export would use the exact same wording.

I wrote helper logic that searched the header row for possible aliases.

A simplified version:

```vb
applicationCol = FindHeaderAny( _
    ws, headerRow, _
    "Job Application Date", _
    "Application Date")

interviewCol = FindHeaderAny( _
    ws, headerRow, _
    "Interview Date", _
    "Interview Dat")
```

The search routine first normalized the text:

```vb
Private Function NormalizeHeader(ByVal txt As String) As String

    Dim i As Long
    Dim ch As String
    Dim result As String

    txt = LCase(CStr(txt))

    For i = 1 To Len(txt)
        ch = Mid$(txt, i, 1)

        If ch Like "[a-z0-9]" Then
            result = result & ch
        End If
    Next i

    NormalizeHeader = result

End Function
```

### Why I did that

Exports are messy.

A header can change from:

```text
Application Date
```

to:

```text
APPLICATION DATE
Application-Date
Application Date:
```

The business meaning is the same, but an exact text match can fail.

The normalization removes capitalization, spaces, and punctuation from the comparison.

So the macro was built around the meaning of the column instead of one fixed Excel letter.

## 8. Detecting the header row instead of assuming row 1

The hiring dashboard also searched the first part of the sheet and scored rows based on how many expected headers they contained.

A shortened version:

```vb
For r = 1 To 100

    score = 0

    If FindHeaderAny(ws, r, "Candidate ID") > 0 Then score = score + 1
    If FindHeaderAny(ws, r, "Candidate Name") > 0 Then score = score + 1
    If FindHeaderAny(ws, r, "Application Date") > 0 Then score = score + 1
    If FindHeaderAny(ws, r, "Interview Date") > 0 Then score = score + 1
    If FindHeaderAny(ws, r, "Salary") > 0 Then score = score + 1
    If FindHeaderAny(ws, r, "Start Date") > 0 Then score = score + 1

    If score > bestScore Then
        bestScore = score
        bestRow = r
    End If

Next r
```

### Why this mattered

Some reports have title rows, blank rows, merged headings, or notes above the actual table.

Hard coding `headerRow = 1` would make the whole macro depend on one exact workbook layout.

This routine looked for the row that behaved most like the real header.

## 9. Building the hiring process as a sequence of stages

Instead of writing one giant block of special cases, I built a stage sequence.

A sanitized example:

```vb
AddStep stepCount, stepCols, stepNames, stepStage, stepSLA, _
    applicationCol, "Application", _
    "Application Received / Awaiting Interview", 7

AddStep stepCount, stepCols, stepNames, stepStage, stepSLA, _
    interviewCol, "Interview", _
    "Interview Complete / Awaiting Screening", 2

AddStep stepCount, stepCols, stepNames, stepStage, stepSLA, _
    screeningCol, "Screening", _
    "Screening Pending", 7
```

### What the arrays represented

For every step, I stored four things:

```text
which column contains the date
what the step is called
what stage the person is in after that step
how many days that stage is expected to take
```

That let the same row processing logic work across the whole pipeline.

It also made the process easier to change. I could add or remove a stage without rewriting the entire calculation engine.

## 10. Figuring out the current stage from the last completed date

The code walked the process in order and remembered the last step that had a valid date:

```vb
lastCompleted = 0

For i = 1 To stepCount
    If GetCellDate(ws, rowNumber, stepCols(i), d) Then
        lastCompleted = i
    End If
Next i
```

Then the macro used that result to determine where the person currently was.

### Plain version

If a row had:

```text
Application Date     yes
Interview Date       yes
Screening Sent       yes
Screening Returned   blank
```

then the last completed step is "Screening Sent."

That means the current stage is not "Interview" and it is not "Salary."

The row is waiting on screening to come back.

The code turned that into a consistent stage automatically.

## 11. Aging and simple SLA risk

Once the current stage was known, I calculated how many days the record had been sitting there.

The source logic used three risk bands:

```vb
aging = Date - CDate(stageStart)

If aging > sla * 1.5 Then
    risk = "Red"

ElseIf aging > sla Then
    risk = "Yellow"

Else
    risk = "Green"
End If
```

### Why the red threshold was different from yellow

Going one day past a target and going far past a target are not the same thing.

The logic separated:

```text
within target       Green
past target         Yellow
well past target    Red
```

That made the dashboard more useful than a simple "late / not late" flag.

The specific SLA values were business rules in the workbook, not universal HR standards.

## 12. Measuring every handoff instead of only total time to hire

The dashboard calculated the time between individual milestones:

```vb
applicationToInterview = DaysBetween(ws, r, applicationCol, interviewCol)
interviewToScreening = DaysBetween(ws, r, interviewCol, screeningCol)
screeningTurnaround = DaysBetween(ws, r, screeningCol, screeningReturnCol)
approvalToOffer = DaysBetween(ws, r, approvalCol, offerCol)
offerToOnboarding = DaysBetween(ws, r, offerCol, onboardingCol)
```

### Why this was more useful than one total

If total time to hire was 45 days, that alone did not tell me what actually caused the 45 days.

Stage timing let me answer:

```text
Was the delay before the interview?
Was the screening slow?
Was compensation review slow?
Was the approved offer sitting?
Was onboarding scheduling the bottleneck?
```

The macro could then compare the completed stage durations and identify the longest one for each record.

## 13. Identifying the longest completed bottleneck

The original logic kept the largest valid stage duration while it looped through the metrics:

```vb
If IsNumeric(stageDuration) Then

    If CDbl(stageDuration) >= 0 Then

        If Not hasMax Or CDbl(stageDuration) > maxValue Then
            hasMax = True
            maxValue = CDbl(stageDuration)
            maxLabel = stageName
        End If

    End If

End If
```

At the end, the row received:

```text
Longest Completed Bottleneck
Longest Bottleneck Days
```

That was my way of turning a lot of dates into something a manager could immediately understand.

## 14. Data quality checks were part of the calculation

I did not want a broken date sequence to quietly become a valid looking metric.

The source code included checks like:

```vb
quality = "OK"

If candidateName = "" Then
    quality = AddQualityFlag(quality, "Missing Candidate Name")
End If

If Not GetCellDate(ws, r, applicationCol, d) Then
    quality = AddQualityFlag(quality, "Missing Application Date")
End If

For i = 1 To durationCount

    If IsNumeric(durationValues(i)) Then

        If CDbl(durationValues(i)) < 0 Then
            quality = AddQualityFlag(quality, _
                "Date Error - Negative Duration")
            Exit For
        End If

    End If

Next i
```

### Why check for negative days?

A negative duration usually means the dates are out of order.

For example:

```text
Interview Date:    June 12
Application Date:  June 20
```

The math can still produce a number, but the number is telling me there is a data problem.

So I treated data quality as part of the dashboard instead of assuming every date was correct.

## 15. Making date handling tolerant of real Excel data

Excel dates can show up as actual date values or as the serial number Excel uses underneath.

The helper handled both cases:

```vb
If IsDate(value) Then

    outDate = DateValue(CDate(value))
    GetCellDate = True

ElseIf IsNumeric(value) Then

    If CDbl(value) > 0 And CDbl(value) < 60000 Then
        outDate = DateSerial(1899, 12, 30) + CLng(CDbl(value))
        GetCellDate = True
    End If

End If
```

### Why I bothered with this

Two cells can look like dates in Excel but arrive differently depending on how the workbook was exported or formatted.

The helper gave the rest of the macro one consistent answer:

```text
valid date -> yes
usable date value -> return it
anything else -> no
```

That kept date cleanup out of every individual metric calculation.

## 16. Weekly tracker: creating a new report from a template

The weekly reporting tool copied a hidden template, renamed it for the current week, and cleared only the fields that people were supposed to reenter.

A sanitized version:

```vb
weekStart = Date - (Weekday(Date, vbMonday) - 1)
newName = "Week of " & Format(weekStart, "mm-dd-yyyy")

templateSheet.Copy After:=ThisWorkbook.Sheets(ThisWorkbook.Sheets.Count)

Set newSheet = ActiveSheet
newSheet.Name = newName

newSheet.Range("A2:C500").ClearContents
newSheet.Range("E2:F500").ClearContents
newSheet.Range("I2:K500").ClearContents
```

### Why copy a template instead of creating a blank sheet

The template already contained:

* the correct headers
* formulas
* dropdown validation
* formatting
* conditional formatting
* calculated columns

I only cleared the fields that should be fresh for the new week.

That reduced setup mistakes and kept every weekly report structured the same way.

## 17. Carrying unfinished records into the next week

I did not want completed items following the team forever, but open work needed to survive the weekly rollover.

The original logic checked the prior week's statuses and copied only records that were not fully complete.

```vb
For sourceRow = 2 To 500

    If Trim$(CStr(oldSheet.Cells(sourceRow, "A").Value)) <> "" Then

        systemStatus = Trim$(CStr(oldSheet.Cells(sourceRow, "E").Value))
        trackerStatus = Trim$(CStr(oldSheet.Cells(sourceRow, "F").Value))

        If Not (systemStatus = "Completion" And _
                trackerStatus = "Completion") Then

            'Copy the fields needed for the new week.

        End If

    End If

Next sourceRow
```

### Why both statuses had to be complete

The point of the tracker was partly reconciliation.

If one source said the record was complete but the other did not, I still wanted the item carried forward so somebody could resolve the mismatch.

Only records that were complete in both places dropped out of the active weekly list.

## 18. Letting VBA write formulas instead of calculating everything itself

Some logic was easier to keep visible in worksheet formulas.

For example, the weekly tracker used VBA to place a formula down hundreds of rows:

```vb
ws.Range("G2:G500").FormulaR1C1 = _
    "=IF(RC[-6]="""",""""," & _
    "IF(OR(RC[-2]="""",RC[-1]=""""),""MISSING""," & _
    "IF(TRIM(RC[-2])=TRIM(RC[-1]),""YES"",""NO"")))"
```

### Why I mixed VBA and formulas

I did not need VBA to do everything.

The formula could stay visible in Excel so another analyst could inspect the rule.

VBA handled putting the formula in the right place every time the template was rebuilt.

That gave me repeatability without hiding all of the business logic inside the VBA editor.

## 19. Follow up flags

The weekly tracker also generated action oriented messages instead of only reporting status.

The original formula logic handled cases such as:

```text
missing start date
compliance item overdue
item due today
two systems disagree
status missing
orientation needs to be scheduled
```

The idea was simple:

```text
data problem -> say what needs attention
deadline problem -> say what needs attention
status mismatch -> say what needs attention
otherwise -> leave it alone
```

That changed the spreadsheet from a passive list into a work queue.

## 20. Conditional formatting was tied to the business rule

I used VBA to create the conditional formatting rules too.

For example:

```vb
Set fc = riskRange.FormatConditions.Add( _
    Type:=xlCellValue, _
    Operator:=xlEqual, _
    Formula1:="=""Red""")

fc.Interior.Color = RGB(255, 199, 206)
fc.Font.Color = RGB(156, 0, 6)
```

The important part is not that a cell turns red.

The important part is that the code calculates the risk first, then the formatting makes that result easy to scan.

I used the same approach for:

* good / bad system matches
* compliant / late items
* due soon dates
* urgent follow ups
* data quality warnings

## 21. Rebuilding the dashboard instead of manually maintaining charts

Several of my tools cleared the old dashboard and recreated the tables and charts from the current data.

Example:

```vb
Do While dashboard.ChartObjects.Count > 0
    dashboard.ChartObjects(1).Delete
Loop

Do While dashboard.Buttons.Count > 0
    dashboard.Buttons(1).Delete
Loop
```

Then VBA rebuilt the current KPI cards, trend tables, and charts.

### Why delete and rebuild?

It avoided leaving old series, old labels, or stale buttons connected to yesterday's layout.

For my use case, rebuilding a known dashboard structure was more predictable than trying to patch every old chart in place.

## 22. KPI cards were reusable code

Instead of repeating the formatting for every number on the dashboard, I put it into one helper:

```vb
Private Sub CreateKPICard( _
    ByVal ws As Worksheet, _
    ByVal labelRange As String, _
    ByVal valueRange As String, _
    ByVal labelText As String, _
    ByVal formulaText As String, _
    ByVal fillColor As Long)

    ws.Range(labelRange).Merge
    ws.Range(valueRange).Merge

    ws.Range(labelRange).Value = labelText
    ws.Range(valueRange).Formula = formulaText

End Sub
```

Then the dashboard could call it for different metrics.

### Why make a helper?

If I changed the card design later, I only had to change it once.

This is one of the places where the VBA started becoming less like a recorded macro and more like a small application.

## 23. Vacancy trend dashboard: cleaning percentages before charting them

Vacancy data did not always arrive in exactly the same format.

I wrote a conversion helper that could handle values like:

```text
15%
15
0.15
```

The source logic:

```vb
Private Function ConvertPercent(ByVal inputValue As Variant) As Double

    Dim textValue As String
    Dim numberValue As Double

    textValue = Trim(CStr(inputValue))
    textValue = Replace(textValue, "%", "")
    textValue = Replace(textValue, ",", "")
    textValue = Replace(textValue, " ", "")

    If IsNumeric(textValue) Then

        numberValue = CDbl(textValue)

        If Abs(numberValue) > 1 Then
            numberValue = numberValue / 100
        End If

        ConvertPercent = numberValue

    Else
        ConvertPercent = 0
    End If

End Function
```

### Why normalize first?

Charts should not have to guess whether `15` means 15 percent or 1500 percent.

I standardized the input before it reached the chart.

## 24. Using actual dates on the vacancy trend chart

The vacancy dashboard used an XY scatter line chart instead of treating the x axis as plain text categories.

```vb
chart.ChartType = xlXYScatterLines

series.XValues = dashboard.Range("U2:U" & helperLastRow)
series.Values = dashboard.Range("W2:W" & helperLastRow)
```

### Why that choice

The data represented a timeline.

Using actual Excel date values meant spacing on the x axis reflected real time instead of simply placing every observation the same distance apart.

I also kept hidden helper columns for the normalized dates and percentages. The user saw the dashboard, while the chart had a clean data block behind it.

## 25. Making the chart scale itself

Instead of hard coding the top of the vacancy percentage axis, the macro scanned the data for the largest value:

```vb
maxValue = GetMaxPercent( _
    dashboard, "W", "Z", 2, helperLastRow)

With chart.Axes(xlValue)
    .MinimumScale = 0
    .MaximumScale = RoundUpToTenth( _
        Application.Max(maxValue + 0.05, 0.1))
End With
```

### Why this mattered

A fixed 100 percent axis can flatten small differences so much that the chart becomes hard to read.

The dynamic scale left some space above the highest point while still keeping zero as the bottom.

## 26. Adding events to the vacancy timeline

The vacancy dashboard did more than draw vacancy percentages.

I also built logic to load dated events, sort them, calculate where they belong on the chart, and draw labels or marker lines.

That required turning an event date into a screen position based on the chart's minimum date, maximum date, and plot width.

Conceptually:

```text
event date
    |
    v
where does this date fall between chart start and chart end?
    |
    v
convert that percentage into an X coordinate
    |
    v
draw the event marker at that position
```

This let the chart show context around changes in vacancy trends instead of showing the numbers by themselves.

## 27. Archiving closed records without skipping rows

I also wrote a macro that moved completed or marked records from an active table to an archive table.

The important detail was looping **backward**:

```vb
For i = activeTable.ListRows.Count To 1 Step -1

    archiveFlag = Trim(CStr( _
        activeTable.DataBodyRange.Cells( _
            i, activeTable.ListColumns("Archive Flag").Index).Value))

    recordStatus = Trim(CStr( _
        activeTable.DataBodyRange.Cells( _
            i, activeTable.ListColumns("Record Status").Index).Value))

    If archiveFlag = "Yes" _
       Or recordStatus = "Closed" _
       Or recordStatus = "Completed" Then

        Set destinationRow = archiveTable.ListRows.Add

        destinationRow.Range.Cells(1, 1) _
            .Resize(1, activeTable.ListColumns.Count).Value = _
            activeTable.DataBodyRange.Rows(i).Value

        destinationRow.Range.Cells( _
            1, activeTable.ListColumns.Count + 1).Value = Now

        activeTable.ListRows(i).Delete

    End If

Next i
```

### Why loop backward?

Deleting rows while moving forward can make Excel skip records.

Simple example:

```text
row 5 gets deleted
old row 6 becomes row 5
loop moves to row 6
the old row 6 never gets checked
```

Going from the bottom upward avoids that problem.

The archive receives the row first, then the source row is deleted.

I also added an archive timestamp so the history showed when the record left the active tracker.

## 28. The code was modular on purpose

The larger projects were split into smaller procedures such as:

```text
BuildDashboard
NormalizeData
DetectHeaderRow
FindHeaderAny
GetCellDate
DaysBetween
ApplyConditionalFormatting
CarryForwardOpenRecords
CreateKPICard
BuildVacancyChart
DrawEventItems
ArchiveClosedRecords
```

I did that because one 1,000 line macro is difficult to test and difficult to change.

A helper like `GetCellDate` has one job.

A helper like `CreateKPICard` has one job.

The main macro then reads more like the workflow itself.

## 29. What the overall design says about my VBA work

The code was not just:

```text
copy cell A to cell B
change color
save file
```

The projects included:

* event driven workbook behavior
* reusable helper functions
* dynamic header detection
* flexible column mapping
* dictionary based aggregation
* date normalization
* duration calculations
* business rule based status logic
* SLA style risk classification
* data quality checks
* template generation
* weekly rollover
* record archiving
* dynamic charts
* timeline annotations
* conditional formatting
* error handling
* performance controls

The main thing I was trying to do was turn HR process rules into consistent spreadsheet logic.

That is where most of the value came from.
