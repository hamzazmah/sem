# USE CASE: 7 Update an employee's details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to update employee's details* so that *employee's details are kept up-to-date.*

### Scope

Company.

### Level

Primary task.

### Preconditions

We know the current details of the employee and also the details to update. Database contains current employee details.

### Success End Condition

Employee's details are successfully updated.

### Failed End Condition

Employee's details are not updated.

### Primary Actor

HR Advisor.

### Trigger

A request for finance information is sent to HR.

## MAIN SUCCESS SCENARIO

1. Employee reports of change in their details.
2. HR advisor captures the new details and their previous details.
3. HR advisor updates the employee details.
4. HR advisor confirms the updated of the details with the employee.

## EXTENSIONS

3. **Invalid or Incorrect update details**:
    1. HR advisor informs the employee that they would have to submit valid details.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0
