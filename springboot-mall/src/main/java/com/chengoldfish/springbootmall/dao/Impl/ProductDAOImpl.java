package com.chengoldfish.springbootmall.dao.Impl;

import com.chengoldfish.springbootmall.constant.ProductCategory;
import com.chengoldfish.springbootmall.dao.ProductDao;
import com.chengoldfish.springbootmall.dto.ProductQueryParams;
import com.chengoldfish.springbootmall.dto.ProductRequest;
import com.chengoldfish.springbootmall.model.Product;
import com.chengoldfish.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDAOImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> getProducts(ProductQueryParams params) {
        String sql="SELECT product_id,product_name, category, image_url, price, stock, description, " +
                "created_date,last_modified_date FROM product WHERE 1=1";

        Map<String,Object> map = new HashMap<>();

        //catrgory
        if(params.getCategory() != null){
            sql=sql+" AND category = :category";
            map.put("category",params.getCategory().name());
        }

        //search
        if(params.getSearch() != null){
            sql=sql+" AND product_name LIKE :search";
            map.put("search","%"+params.getSearch()+"%");
        }

        //orderby只能透過拼接使用
        //不判斷null是因為有給default值
        sql = sql + " ORDER BY " + params.getOrderBy() + " " + params.getSort();

        List<Product> productList=namedParameterJdbcTemplate.query(sql,map,new ProductRowMapper());

        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {
        String sql="SELECT product_id,product_name, category, image_url, price, stock, description, " +
                "created_date,last_modified_date FROM product WHERE product_id=:productId";

        Map<String,Object> map = new HashMap<> ();
        map.put("productId",productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (productList.size()>0){
            return productList.get(0);
        }else{
            return null;
        }

    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql="INSERT INTO product(product_name, category, image_url, price, stock," +
                "description, created_date, last_modified_date) " +
                "VALUES (:productName, :category,:imageUrl,:price,:stock,:description," +
                ":createDate,:lastModifiedDate)";

        Map<String,Object> map = new HashMap<> ();

        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().name());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        Date now = new Date();
        map.put("createDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map), keyHolder);

        int productId=keyHolder.getKey().intValue();

        return productId;

    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql="UPDATE product SET product_name=:productName, category=:category, image_url=:imageUrl," +
                "price=:price,stock=:stock,description=:description,last_modified_date=:lastModifiedDate "+
                " WHERE product_id=:productId";

        Map<String,Object> map = new HashMap<> ();
        map.put("productId",productId);

        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().name());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        map.put("lastModifiedDate",new Date()); //小細節注意

        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
    public void deletProductById(Integer productId) {

        String sql="DELETE FROM product WHERE product_id=:productId";

        Map<String,Object> map = new HashMap<> ();
        map.put("productId",productId);

        namedParameterJdbcTemplate.update(sql,map);
    }
}
