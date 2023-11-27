package com.example.study.item.domain

import com.example.study.common.BaseEntity
import com.example.study.converter.YnToBooleanConverter
import com.example.study.member.enums.ItemType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "ITEM")
class Item(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    var id: Long? = null,

    @Column(name = "ITEM_NAME")
    var itemName: String,

    @Column(name = "PRICE")
    var price: Long,

    @Column(name = "ITEM_TYPE")
    @Enumerated(EnumType.STRING)
    var itemType: ItemType,

    @Column(name = "USED_COUNT")
    var usedCount: Int = 0,

    @Column(name = "IS_USED")
    @Convert(converter = YnToBooleanConverter::class)
    var isUsed: String

): BaseEntity() {

    fun updateItemName(itemName: String) {
        this.itemName = itemName
    }

    fun updatePrice(price: Long) {
        this.price = price
    }

    fun updateItemType(itemType: ItemType) {
        this.itemType = itemType
    }

}

