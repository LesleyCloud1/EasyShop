package org.yearup.data.mysql;

import org.springframework.jdbc.core.RowMapper;
import org.yearup.models.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

//This class maps rows from the SQL result set into Category objects
public class CategoryMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();

        //Set values from database columns to the Category model
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));

        return category;
    }
}
