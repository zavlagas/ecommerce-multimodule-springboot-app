package com.agileactors.product.service;


import com.agileactors.product.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO save(ProductDTO productDTO);

    List<ProductDTO> findAll();

    ProductDTO findBy(String id);

    ProductDTO update(ProductDTO productDTO);

    void delete(String id);
}
