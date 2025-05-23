package com.chengoldfish.springbootmall.controller;

import com.chengoldfish.springbootmall.constant.ProductCategory;
import com.chengoldfish.springbootmall.dto.ProductRequest;
import com.chengoldfish.springbootmall.model.Product;
import com.chengoldfish.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //查詢商品列表 //category不是必要的值 -> requird=false
    @GetMapping("/products")
    public  ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search
    ){
        List<Product> productList=productService.getProducts(category,search);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        if(product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(product);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> CreateProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId= productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);

       return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProdut(@PathVariable Integer productId,
                                                @RequestBody @Valid ProductRequest productRequest) {
        //檢查查詢商品是否存在
        Product product=productService.getProductById(productId);

        if(product==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //商品存在，修改商品數據
        productService.updateProduct(productId,productRequest);

        Product updatedProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId) {
       productService.deletProductById(productId);

       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}
