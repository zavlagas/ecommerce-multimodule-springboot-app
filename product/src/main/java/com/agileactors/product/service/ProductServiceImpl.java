package com.agileactors.product.service;


import com.agileactors.product.document.Product;
import com.agileactors.product.dto.ProductDTO;
import com.agileactors.product.exception.custom.ProductNotFoundException;
import com.agileactors.product.repository.ProductRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@Transactional
class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ConversionService conversionService;
    private final Function<ProductDTO, Product> convertProductDtoToProduct;
    private final Function<Product, ProductDTO> convertProductToProductDto;

    public ProductServiceImpl(ProductRepository productRepository,
                              ConversionService conversionService) {
        this.productRepository = productRepository;
        this.conversionService = conversionService;
        this.convertProductDtoToProduct = productDTO -> this.conversionService.convert(productDTO, Product.class);
        this.convertProductToProductDto = product -> this.conversionService.convert(product, ProductDTO.class);
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = convertProductDtoToProduct.apply(productDTO);
        product = productRepository.save(product);
        return convertProductToProductDto.apply(product);
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .parallel()
                .map(convertProductToProductDto)
                .toList();
    }

    @Override
    public ProductDTO findBy(String id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No Product found with id " + id));
        return convertProductToProductDto.apply(product);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        String id = productDTO.getId();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("No Product found with id " + id));
        product.setName(productDTO.getName());
        product = productRepository.save(product);
        return convertProductToProductDto.apply(product);
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
