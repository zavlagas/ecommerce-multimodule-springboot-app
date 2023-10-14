package com.agileactors.product.document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;

@Getter
public abstract class AuditDocument {


    @CreatedDate
    @Setter(AccessLevel.NONE)
    private Instant insertedAt;
    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;
    @Version
    @Setter(AccessLevel.NONE)
    private Integer version;
}
