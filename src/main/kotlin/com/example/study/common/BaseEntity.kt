package com.example.study.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "CREATE_DATE")
    var createDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "UPDATE_DATE")
    var updateDate: LocalDateTime? = null
}



