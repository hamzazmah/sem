package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    /**
     * Before all initiate App
     */
    @BeforeAll
    static void init()
    {
        app = new App();
    }

    /**
     * Test Method when Employee's is null
     */
    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }

    /**
     * Test method when Employee is Empty
     */
    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        app.printSalaries(employess);
    }

    /**
     * Test when an employee is null
     */
    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employess = new ArrayList<Employee>();
        employess.add(null);
        app.printSalaries(employess);
    }

    /**
     * Test when employee is not null
     */
    @Test
    void printSalaries()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }

    /**
     * Test for displayEmployee when null
     */
    @Test
    void displayEmployeeTestNull()
    {
        app.displayEmployee ( null );
    }

    /**
     * Test for displayEmployee when Empty And Contains Null Manager or Department
     */
    @Test
    void displayEmployeeTestEmpty()
    {
        app.displayEmployee ( new Employee () );
    }

    /**
     * Test for displayEmployee when not null
     */
    @Test
    void displayEmployee()
    {
        Employee emp = new Employee ();
        emp.first_name = "Jim";
        emp.last_name = "Smith";
        emp.title = "Engineer";
        emp.emp_no = 123;
        emp.salary = 120000;

        app.displayEmployee ( emp );
    }
}