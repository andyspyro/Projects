Option Explicit

' Sanitized public example based on an HR tracker automation I built.
' Real employee, applicant, position, facility, and employer identifiers are excluded.

Public Sub ArchiveCompletedRecords()

    Dim wsActive As Worksheet
    Dim wsArchive As Worksheet
    Dim tblActive As ListObject
    Dim tblArchive As ListObject
    Dim i As Long
    Dim destRow As ListRow
    Dim archiveFlag As String
    Dim recordStatus As String
    Dim movedCount As Long

    Set wsActive = ThisWorkbook.Worksheets("Active Tracker")
    Set wsArchive = ThisWorkbook.Worksheets("Archive History")
    Set tblActive = wsActive.ListObjects("ActiveData")
    Set tblArchive = wsArchive.ListObjects("ArchiveData")

    Application.ScreenUpdating = False

    ' Loop backward because deleting rows while moving forward can skip records.
    For i = tblActive.ListRows.Count To 1 Step -1

        archiveFlag = Trim$(CStr( _
            tblActive.DataBodyRange.Cells( _
                i, tblActive.ListColumns("Archive Flag").Index).Value))

        recordStatus = Trim$(CStr( _
            tblActive.DataBodyRange.Cells( _
                i, tblActive.ListColumns("Record Status").Index).Value))

        If archiveFlag = "Yes" _
           Or recordStatus = "Closed" _
           Or recordStatus = "Completed" _
           Or recordStatus = "Archive" Then

            Set destRow = tblArchive.ListRows.Add

            destRow.Range.Cells(1, 1) _
                .Resize(1, tblActive.ListColumns.Count).Value = _
                tblActive.DataBodyRange.Rows(i).Value

            destRow.Range.Cells(1, tblActive.ListColumns.Count + 1).Value = Now
            destRow.Range.Cells(1, tblActive.ListColumns.Count + 2).Value = _
                "Archived from active tracker"

            tblActive.ListRows(i).Delete
            movedCount = movedCount + 1

        End If

    Next i

    Application.ScreenUpdating = True

    MsgBox movedCount & " record(s) moved to Archive History.", _
           vbInformation, "Archive Complete"

End Sub
