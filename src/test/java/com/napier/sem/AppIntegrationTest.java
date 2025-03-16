package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33061", 30000);
    }

    /**
     * Test for getEmployee
     */
    @Test
    void testGetEmployee()
    {
        Employee emp = app.getEmployee(255530);
        assertEquals(emp.emp_no, 255530);
        assertEquals(emp.first_name, "Ronghao");
        assertEquals(emp.last_name, "Garigliano");
    }

    /**
     * Test if returned allSalaries
     */
    @Test
    void testGetSalaries()
    {
        ArrayList<Employee> emps = app.getAllSalaries ();
        assertFalse ( emps.isEmpty ( ) );
    }

    /**
     * Check if returned Salaries by role
     */
    @Test
    void testGetSalariesByRole()
    {
        ArrayList<Employee> emps = app.getAllSalariesByRole ("Engineer");

        Employee emp = emps.get ( 0 );
        assertEquals ( emp.title, "Engineer" );
    }

    /**
     * Check if returned Department
     */
    @Test
    void testGetDepartment()
    {
        Department dept = app.getDepartment ("Sales");
        assertEquals ( dept.dept_name, "Sales" );
    }

    /**
     * Check if returned Salaries by Department
     */
    @Test
    void testGetSalariesByDepartment()
    {
        Department dep = new Department ();
        dep.dept_name = "Sales";
        ArrayList<Employee> emps = app.getSalariesByDepartment (dep);

        Employee emp = emps.get ( 0 );
        assertEquals ( emp.dept.dept_name, "Sales" );
    }

    /**
     * Test for Adding a new Employee
     */
    @Test
    void testAddEmployee()
    {
        Employee emp = new Employee();
        emp.emp_no = 6912345;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        app.addEmployee(emp);
        Employee e = app.getEmployeeSimple(6912345);
        assertEquals(e.emp_no, emp.emp_no);
        assertEquals(e.first_name, emp.first_name);
        assertEquals(e.last_name, emp.last_name);
    }

    /**
     * Test for deleting an employee
     */
    @Test
    void testDeleteEmployee()
    {
        // First add an employee to delete
        Employee emp = new Employee();
        emp.emp_no = 9999999;
        emp.first_name = "Test";
        emp.last_name = "Delete";
        app.addEmployee(emp);
        
        // Verify employee was added
        Employee addedEmp = app.getEmployeeSimple(9999999);
        assertNotNull(addedEmp);
        
        // Delete the employee
        boolean result = app.deleteEmployee(9999999);
        assertTrue(result);
        
        // Verify employee was deleted
        Employee deletedEmp = app.getEmployeeSimple(9999999);
        assertNull(deletedEmp);
    }
    
    /**
     * Test for deleting a non-existent employee
     */
    @Test
    void testDeleteNonExistentEmployee()
    {
        // Try to delete an employee that doesn't exist
        boolean result = app.deleteEmployee(99999999);
        assertFalse(result);
    }
}