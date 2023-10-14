package com.agileactors.product.controller;


import com.agileactors.product.dto.ProductDTO;
import com.agileactors.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Finds All Products")
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Product By ID")
    public ResponseEntity<ProductDTO> findBy(@PathVariable String id) {
        return ResponseEntity.ok(productService.findBy(id));
    }

    @PostMapping
    @Operation(summary = "Creates a Product")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
        productDTO = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }


    @PutMapping
    @Operation(summary = "Updates a Product")
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO productDTO) {
        productDTO = productService.update(productDTO);
        return ResponseEntity.ok(productDTO);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a Product")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
