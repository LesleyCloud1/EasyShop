package org.yearup.data.mysql;

import org.springframework.jdbc.core.JdbcTemplate;//Imports the JdbcTemplate class for interacting with the database
import javax.sql.DataSource;//DataSource is used to manage connections to the database
import java.sql.Connection;
import java.sql.SQLException;

// This is an abstract base class for all DAO (Data Access Object) classes that interact with a MySQL database.
public abstract class MySqlDaoBase
{
    private DataSource dataSource;// Manages the pool of connections to the database
    protected JdbcTemplate jdbcTemplate;// Allows us to run SQL queries easily

    // Constructor - gets called when a subclass (like MySqlCategoryDao) is created.
    // It receives a DataSource and uses it to initialize JdbcTemplate.
    public MySqlDaoBase(DataSource dataSource)
    {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource); // Sets up JdbcTemplate to use the same data source
    }

    //This method gives subclasses a raw SQL connection (if they ever need it).
    //It's not usually needed if you use JdbcTemplate.
    protected Connection getConnection() throws SQLException
    {
        return dataSource.getConnection(); //Returns a direct connection to the database
    }
}
