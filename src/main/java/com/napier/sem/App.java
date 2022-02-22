package com.napier.sem;

import java.sql.*;

public class App
{
    public static void main(String[] args)
    {
        //Create new Application
        App a = new App();

        //Connect to db
        a.connect();

        // Get Employee
        Employee emp = a.getEmployee(255530);
        // Display results
        a.displayEmployee(emp);

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
    public void connect()
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
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/employees?useSSL=false", "root", "example");
                //con = DriverManager.getConnection("jdbc:mysql://localhost:33070/employees?useSSL=false", "root", "example"); //For local testing
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
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
                            "CONCAT(me.first_name, ' ', me.last_name) AS manager " +
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
                emp.dept_name = rset.getString("dept_name");
                emp.salary = Integer.parseInt(rset.getString("salary"));
                emp.title = rset.getString("title");
                emp.manager = rset.getString("manager");
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
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
    }
}