package com.agileactors.product.document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
@EqualsAndHashCode(callSuper = false)
public class Product extends AuditDocument {
    @Id
    private String id;

    private String name;


}
