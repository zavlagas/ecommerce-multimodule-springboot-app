package com.agileactors.product.converter;

import com.agileactors.product.document.Product;
import com.agileactors.product.dto.ProductDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoToProductConverter implements Converter<ProductDTO, Product> {


    @Override
    public Product convert(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setId(dto.getId());
        return product;
    }
}
