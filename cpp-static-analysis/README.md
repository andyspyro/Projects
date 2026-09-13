# C and C++ Static Analysis

> **Type:** Secure coding and static analysis coursework  
> **Tool:** Flawfinder  
> **Status:** Analysis retained; original course source is not published

I ran Flawfinder against a C++ program and then reviewed the findings instead of treating every warning as a confirmed vulnerability.

## Verified scan result

The retained Flawfinder output shows:

| Item | Result |
|---|---:|
| Lines analyzed | 122 |
| Physical source lines | 86 |
| Findings at risk level 1 or higher | 31 |
| Highest reported risk level | 4 |

The scan flagged functions and patterns including `strcat`, `strcpy`, `memcpy`, fixed size character buffers, `strlen`, `strncpy`, `fopen`, and `system`.

A sanitized scan summary is here: [flawfinder-summary.txt](flawfinder-summary.txt).

## What I reviewed

### Buffer handling

The report flagged several places where fixed size buffers or copy operations could become unsafe if the destination size was not checked.

### String handling

Functions such as `strcat`, `strcpy`, `strlen`, and `strncpy` needed context. Some warnings were lower risk because the source value was constant, while others depended on termination and size assumptions.

### Command execution

A `system` call was one of the higher risk findings because user controlled input reaching a shell can turn into command execution.

### File handling

The `fopen` warning raised path and race condition questions rather than proving an exploit on its own.

## CWE categories that appeared in the retained output

* CWE 120 for unsafe buffer copy behavior
* CWE 119 for memory boundary problems
* CWE 126 for possible over reads
* CWE 78 for command execution risk
* CWE 362 for race condition concerns

## The part that mattered

Flawfinder finds suspicious code quickly, but it does not know the full runtime context.

For each result I still had to ask whether input was controlled, whether the buffer could actually overflow, whether the string was terminated, and whether the warning was a real problem or a false positive.

I do not publish the original course source file because it was not my standalone codebase.
