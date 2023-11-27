package com.example.study.item.controller

import com.example.study.item.ItemDto
import com.example.study.item.ItemRequestDto
import com.example.study.item.ItemSearchCondition
import com.example.study.item.service.ItemService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/item")
class ItemController(private val itemService: ItemService) {

    @PostMapping
    fun saveItem(@RequestBody @Valid requestDto: ItemRequestDto): ResponseEntity<ItemDto> {
        return ResponseEntity.ok(itemService.save(requestDto))
    }

    @GetMapping("/{id}")
    fun getItem(@PathVariable("id") id:Long): ResponseEntity<ItemDto> {
        return ResponseEntity.ok(itemService.getOneItem(id))
    }

    @GetMapping
    fun getAllItems(condtion: ItemSearchCondition, pageable: Pageable): Page<ItemDto> {
        return itemService.getAllItems(condtion, pageable)
    }

    @PatchMapping("/{id}")
    fun updateItem(@PathVariable("id") id: Long,
                   @RequestBody @Valid requestDto: ItemRequestDto): ResponseEntity<ItemDto> {
        return ResponseEntity.ok(itemService.updateItem(id, requestDto))
    }
}