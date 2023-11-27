package com.example.study.item.repository

import com.example.study.item.domain.Item
import org.springframework.data.repository.Repository
import java.util.*

@org.springframework.stereotype.Repository
interface ItemRepository: Repository<Item, Long>, ItemRepositoryCustom {

    fun findById(id: Long): Optional<Item>

    fun save(item: Item): Item
}