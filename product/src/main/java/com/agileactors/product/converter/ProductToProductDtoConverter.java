package com.agileactors.product.converter;

import com.agileactors.product.document.Product;
import com.agileactors.product.dto.ProductDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductDtoConverter implements Converter<Product, ProductDTO> {


    @Override
    public ProductDTO convert(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setName(product.getName());
        dto.setId(product.getId());
        return dto;
    }
}
