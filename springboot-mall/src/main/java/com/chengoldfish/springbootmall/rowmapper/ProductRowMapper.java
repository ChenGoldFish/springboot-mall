package com.chengoldfish.springbootmall.rowmapper;

import com.chengoldfish.springbootmall.constant.ProductCategory;
import com.chengoldfish.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
       Product product = new Product ();

       product.setProductId(rs.getInt("product_Id"));
       product.setProductName(rs.getString("product_name"));

      /* String categoryString = rs.getString("category");
       ProductCategory category = ProductCategory.valueOf(categoryString);
       product.setCategory(category);*/

        product.setCategory(ProductCategory.valueOf(rs.getString("category")));


       product.setImageUrl(rs.getString("image_url"));
       product.setPrice(rs.getInt("price"));
       product.setStock(rs.getInt("stock"));
       product.setCreateDate(rs.getTimestamp("created_date"));
       product.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

       return product;

    }
}
