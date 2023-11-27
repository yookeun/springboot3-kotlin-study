package com.example.study.item

import com.example.study.item.domain.Item
import com.example.study.member.enums.ItemType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ItemDto(
    var id: Long?,
    var itemName: String,
    var price: Long,
    var itemType: ItemType,
    var isUsed: String
)

data class ItemRequestDto(

    @field:NotBlank(message = "required")
    var itemName: String,

    @field:NotNull(message = "required")
    var price: Long,

    @field:NotNull(message = "required")
    var itemType: ItemType,

    @field:NotNull(message = "required")
    var isUsed: String
) {
    fun toEntity() = Item(
        itemName = itemName,
        price = price,
        itemType = itemType,
        isUsed = isUsed
    )
}

data class ItemSearchCondition(
    var searchName: String = "",
    var itemType: String = ""
)

fun Item.toDto(): ItemDto = ItemDto(
    id = id,
    itemName = itemName,
    price = price,
    itemType = itemType,
    isUsed = isUsed
)