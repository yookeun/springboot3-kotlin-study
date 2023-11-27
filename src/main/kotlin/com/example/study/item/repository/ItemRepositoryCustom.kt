package com.example.study.item.repository

import com.example.study.item.ItemSearchCondition
import com.example.study.item.domain.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ItemRepositoryCustom {

    fun getAllItems(condition: ItemSearchCondition, pageable: Pageable): Page<Item>
}