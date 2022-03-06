# USE CASE: 6 View and Edit employee's details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to view and edit employee's details* so that *employee's promotion request can be supported*

### Scope

Company.

### Level

Primary task.

### Preconditions

We know the details of the employee.  Database contains current employee's details.

### Success End Condition

Employee details are viewed and their promotion request is granted.

### Failed End Condition

Employee details are not viewed and their promotion request is not granted.

### Primary Actor

HR Advisor.

### Trigger

A promotion request for the employee is sent to HR.

## MAIN SUCCESS SCENARIO

1. Employee requests for a promotion.
2. HR advisor captures employee information.
3. HR advisor extracts the employee information to review their promotion request.
4. HR advisor approves or disapproves the employee's promotion request.

## EXTENSIONS

3. **Promotion request submitted too early, or they were promoted quite recently**:
    1. HR advisor informs the employee that their request for promotion is too early, or they were promoted quite recently.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0
