# USE CASE: 3 Produce a Report on the Salary of Employees in Primary Actor's current department

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *Department Manager* I want *to produce a report on the salary of employees in my current department* so that *I can support financial reporting for my department.*

### Scope

Department.

### Level

Primary task.

### Preconditions

We use the Department Manager's department.  Database contains current employee salary data.

### Success End Condition

A report is available for Department Manager to provide to finance.

### Failed End Condition

No report is produced.

### Primary Actor

Department Manager.

### Trigger

A request for finance information is sent to Department Manager.

## MAIN SUCCESS SCENARIO

1. Finance request salary information for the Department Manager's current department.
2. Department Manager captures name of the department to get salary information for.
3. Department Manager extracts current salary information of all employees of the given department.
4. Department Manager provides report to finance.

## EXTENSIONS

None.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0
