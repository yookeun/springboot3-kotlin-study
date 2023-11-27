package com.example.study.item.service

import com.example.study.item.ItemDto
import com.example.study.item.ItemRequestDto
import com.example.study.item.ItemSearchCondition
import com.example.study.item.repository.ItemRepository
import com.example.study.item.toDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ItemService(private val itemRepository: ItemRepository) {

    @Transactional
    fun save(itemRequestDto: ItemRequestDto): ItemDto {
        val item = itemRepository.save(itemRequestDto.toEntity())
        return item.toDto()
    }

    fun getOneItem(id: Long): ItemDto {
        val item = itemRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("This Member Item ID does not exist") }
        return item.toDto()
    }

    fun getAllItems(condition: ItemSearchCondition, pageable: Pageable): Page<ItemDto> {
        return itemRepository.getAllItems(condition, pageable).map { item -> item.toDto() }
    }

    @Transactional
    fun updateItem(id: Long, requestDto: ItemRequestDto): ItemDto {
        val item = itemRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("This Member Item ID does not exist") }
        item.updateItemName(requestDto.itemName)
        item.updateItemType(requestDto.itemType)
        item.updatePrice(requestDto.price)
        return item.toDto()
    }
}