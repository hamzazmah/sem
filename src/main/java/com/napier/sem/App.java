package com.napier.sem;

//Imports
import java.sql.*;
import java.util.ArrayList;

//Main APp class
public class App
{
    /**
     * The Main method of application that retrieves and displays reports
     * @param args
     */
    public static void main(String[] args)
    {
        //Create new Application
        App a = new App();

        //Connect to db
        if (args.length < 1)
        {
            a.connect ( "localhost:33061", 3000 );
        }
        else
        {
            a.connect( args[0], Integer.parseInt ( args[1] ));
        }

        // Get Employee
        Employee emp = a.getEmployee(255530);
        // Display results
        System.out.println("Employee (255530) Details: \n");
        a.displayEmployee(emp);

        //Get all employee's salary info
        ArrayList<Employee> employees = a.getAllSalaries();
        //Print out all employee's salary info
        System.out.println("All Employee's Salaries Details: \n");
        a.printSalaries(employees);

        //Get all employee's salary info by role
        ArrayList<Employee> employeesByRole = a.getAllSalariesByRole("Engineer");
        //Print out all employee's salary info
        System.out.println("Employee's by role (Engineer) Salaries Details: \n");
        a.printSalaries(employeesByRole);

        //Get all employee's salary info by their Department
        Department dep = new Department();
        dep.dept_name = "Sales";
        ArrayList<Employee> employeesByDepartment = a.getSalariesByDepartment(dep);
        //Print out all employee's salary info
        System.out.println("Employee's by Department (Sales) Salaries Details: \n");
        a.printSalaries(employeesByDepartment);

        //Adding a new Employee
//        Employee addEmp = new Employee();
//        addEmp.emp_no = 500000;
//        addEmp.first_name = "Kevin";
//        addEmp.last_name = "Chalmers";
//        a.addEmployee(addEmp);
//        Employee nEmp = a.getEmployee(500000);
//        System.out.println("Employee (500000) Details: \n");
//        a.displayEmployee(nEmp);

        // Test updating an employee
        System.out.println("\nTesting employee update functionality:");
        Employee empToUpdate = a.getEmployee(255530);
        if (empToUpdate != null) {
            System.out.println("Before update:");
            a.displayEmployee(empToUpdate);
            
            // Update employee details
            empToUpdate.first_name = "Updated";
            empToUpdate.last_name = "Employee";
            empToUpdate.salary = empToUpdate.salary + 5000; // Increase salary
            
            // Update the employee
            boolean updateSuccess = a.updateEmployee(empToUpdate);
            
            if (updateSuccess) {
                // Get the updated employee
                Employee updatedEmp = a.getEmployee(255530);
                System.out.println("\nAfter update:");
                a.displayEmployee(updatedEmp);
            }
        }

        //Disconnect from db
        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String loc, int delay)
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + loc + "/employees?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + i );
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Get Employee from db
     * @param ID
     * @return Employee
     */
    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT e.emp_no AS emp_no, " +
                            "e.first_name As first_name, " +
                            "e.last_name AS last_name, " +
                            "t.title AS title, " +
                            "s.salary AS salary, " +
                            "d.dept_name AS dept_name, " +
                            "me.first_name, " +
                            "me.last_name " +
                            "FROM employees e " +
                            "JOIN dept_emp de on e.emp_no = de.emp_no " +
                            "JOIN dept_manager dm on de.dept_no = dm.dept_no " +
                            "JOIN salaries s on e.emp_no = s.emp_no " +
                            "JOIN departments d on de.dept_no = d.dept_no " +
                            "JOIN titles t on e.emp_no = t.emp_no " +
                            "JOIN employees me ON me.emp_no = dm.emp_no " +
                            "WHERE DATEDIFF(s.to_date, CURRENT_DATE()) > 0 " +
                            "AND DATEDIFF(dm.to_date, CURRENT_DATE()) > 0 " +
                            "AND e.emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");

                Department dept = new Department();
                dept.dept_name = rset.getString("dept_name");

                emp.salary = Integer.parseInt(rset.getString("salary"));
                emp.title = rset.getString("title");

                Employee manager = new Employee();
                manager.first_name = rset.getString("me.first_name");
                manager.last_name = rset.getString("me.last_name");

                emp.dept = dept;
                emp.manager = manager;

                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Get Simple Employee from db
     * @param ID
     * @return Employee
     */
    public Employee getEmployeeSimple(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT e.emp_no AS emp_no, " +
                            "e.first_name As first_name, " +
                            "e.last_name AS last_name " +
                            "FROM employees e " +
                            "WHERE e.emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");

                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Display Employee details
     * @param emp
     */
    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + "\n"
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + (emp.dept != null ? emp.dept.dept_name + "\n" : "" )
                            + "Manager: " + (emp.manager != null ? (emp.manager.first_name + " " + emp.manager.last_name) : "") + "\n");
        }
        else
        {
            System.out.println ( "No Employee!" );
        }
    }

    /**
     * Gets all the current employees and salaries.
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary, titles.title "
                            + "FROM employees, salaries, titles "
                            + "WHERE employees.emp_no = salaries.emp_no AND employees.emp_no = titles.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                emp.title = rset.getString("titles.title");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Gets all the current employees and salaries.
     * @param role - The employee's role
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalariesByRole(String role)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary, titles.title "
                             + "FROM employees, salaries, titles "
                             + "WHERE employees.emp_no = salaries.emp_no "
                             + "AND employees.emp_no = titles.emp_no "
                             + "AND titles.to_date = '9999-01-01' "
                             + "AND titles.title = '" + role + "' "
                             + "AND salaries.to_date = '9999-01-01' "
                             + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                emp.title = rset.getString("titles.title");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }


    /**
     * Prints a list of employees salaries information.
     * @param employees The list of employees salaries to print.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        //Check if Employee is not null
        if (employees == null)
        {
            System.out.println ( "No Employees!" );
            return;
        }

        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s %-10s %-10s", "Emp No", "First Name", "Last Name", "Salary", "Title", "Department"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            //Skip null employee
            if (emp == null)
                continue;

            //Salary add Dollar sign
            String salary = "$" + emp.salary;

            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s %-10s %-10s",
                            emp.emp_no, emp.first_name, emp.last_name, salary, emp.title, (emp.dept != null ? emp.dept.dept_name : ""));
            System.out.println(emp_string);
        }
    }

    /**
     * Method to get the Department given the dept_no
     * @param dept_name The department we want to retrieve
     * @return Department
     */
    public Department getDepartment(String dept_name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT d.dept_no, d.dept_name, e.first_name, e.last_name "
                    + "FROM departments d "
                    + "JOIN dept_manager dm on d.dept_no = dm.dept_no "
                    + "JOIN employees e on e.emp_no = dm.emp_no "
                    + "WHERE d.dept_name = '" + dept_name + "' ";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract Department information
            if (rset.next())
            {
                Department department = new Department();
                department.dept_no = rset.getString("d.dept_no");
                department.dept_name = rset.getString("d.dept_name");

                Employee depMan = new Employee ();
                depMan.first_name = rset.getString("e.first_name");
                depMan.last_name = rset.getString("e.last_name");

                department.manager = depMan;
                return department;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get Department details");
            return null;
        }
    }

    public ArrayList<Employee> getSalariesByDepartment(Department dept)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary, departments.dept_name "
                             + "FROM employees, salaries, dept_emp, departments "
                             + "WHERE employees.emp_no = salaries.emp_no "
                             + "AND employees.emp_no = dept_emp.emp_no "
                             + "AND dept_emp.dept_no = departments.dept_no "
                             + "AND salaries.to_date = '9999-01-01' "
                             + "AND departments.dept_name = '" + dept.dept_name + "' "
                             + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                dept.dept_name = rset.getString("departments.dept_name");

                emp.dept = dept;
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Method to add a new Employee to db
     * @param emp Employee to add
     */
    public void addEmployee(Employee emp)
    {
        try
        {
            Statement stmt = con.createStatement();
            String strUpdate = " INSERT INTO employees (emp_no, first_name, last_name, birth_date, gender, hire_date) " +
                                "VALUES (" + emp.emp_no + ", '" + emp.first_name + "', '" + emp.last_name + "', " +
                                "'9999-01-01', 'M', '9999-01-01')";
            stmt.execute(strUpdate);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to add employee");
        }
    }

    /**
     * Method to update an existing Employee's details in the database
     * @param emp Employee with updated details
     * @return boolean indicating if the update was successful
     */
    public boolean updateEmployee(Employee emp)
    {
        try
        {
            // Check if employee exists
            Employee existingEmp = getEmployeeSimple(emp.emp_no);
            if (existingEmp == null)
            {
                System.out.println("Employee does not exist");
                return false;
            }

            // Update employee basic information
            Statement stmt = con.createStatement();
            String strUpdate = "UPDATE employees " +
                               "SET first_name = '" + emp.first_name + "', " +
                               "last_name = '" + emp.last_name + "' " +
                               "WHERE emp_no = " + emp.emp_no;
            stmt.executeUpdate(strUpdate);

            // Update salary if provided
            if (emp.salary > 0)
            {
                // Set the end date of the current salary record
                String endCurrentSalary = "UPDATE salaries " +
                                         "SET to_date = CURRENT_DATE() " +
                                         "WHERE emp_no = " + emp.emp_no + " " +
                                         "AND to_date = '9999-01-01'";
                stmt.executeUpdate(endCurrentSalary);

                // Insert new salary record
                String insertNewSalary = "INSERT INTO salaries (emp_no, salary, from_date, to_date) " +
                                        "VALUES (" + emp.emp_no + ", " + emp.salary + ", " +
                                        "CURRENT_DATE(), '9999-01-01')";
                stmt.executeUpdate(insertNewSalary);
            }

            // Update title if provided
            if (emp.title != null && !emp.title.isEmpty())
            {
                // Check if the title has changed
                String checkTitle = "SELECT title FROM titles " +
                                   "WHERE emp_no = " + emp.emp_no + " " +
                                   "AND to_date = '9999-01-01'";
                ResultSet rset = stmt.executeQuery(checkTitle);
                
                if (rset.next() && !rset.getString("title").equals(emp.title))
                {
                    // Set the end date of the current title record
                    String endCurrentTitle = "UPDATE titles " +
                                            "SET to_date = CURRENT_DATE() " +
                                            "WHERE emp_no = " + emp.emp_no + " " +
                                            "AND to_date = '9999-01-01'";
                    stmt.executeUpdate(endCurrentTitle);

                    // Insert new title record
                    String insertNewTitle = "INSERT INTO titles (emp_no, title, from_date, to_date) " +
                                           "VALUES (" + emp.emp_no + ", '" + emp.title + "', " +
                                           "CURRENT_DATE(), '9999-01-01')";
                    stmt.executeUpdate(insertNewTitle);
                }
            }

            // Update department if provided
            if (emp.dept != null && emp.dept.dept_name != null && !emp.dept.dept_name.isEmpty())
            {
                // Get the department number
                String getDeptNo = "SELECT dept_no FROM departments " +
                                  "WHERE dept_name = '" + emp.dept.dept_name + "'";
                ResultSet rset = stmt.executeQuery(getDeptNo);
                
                if (rset.next())
                {
                    String dept_no = rset.getString("dept_no");
                    
                    // Check if the employee is already in this department
                    String checkDept = "SELECT dept_no FROM dept_emp " +
                                      "WHERE emp_no = " + emp.emp_no + " " +
                                      "AND to_date = '9999-01-01'";
                    ResultSet deptRset = stmt.executeQuery(checkDept);
                    
                    if (deptRset.next() && !deptRset.getString("dept_no").equals(dept_no))
                    {
                        // Set the end date of the current department record
                        String endCurrentDept = "UPDATE dept_emp " +
                                               "SET to_date = CURRENT_DATE() " +
                                               "WHERE emp_no = " + emp.emp_no + " " +
                                               "AND to_date = '9999-01-01'";
                        stmt.executeUpdate(endCurrentDept);

                        // Insert new department record
                        String insertNewDept = "INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) " +
                                              "VALUES (" + emp.emp_no + ", '" + dept_no + "', " +
                                              "CURRENT_DATE(), '9999-01-01')";
                        stmt.executeUpdate(insertNewDept);
                    }
                }
                else
                {
                    System.out.println("Department not found: " + emp.dept.dept_name);
                }
            }

            System.out.println("Employee updated successfully");
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to update employee");
            return false;
        }
    }
}