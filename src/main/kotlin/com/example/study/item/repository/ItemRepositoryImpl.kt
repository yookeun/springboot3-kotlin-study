package com.example.study.item.repository

import com.example.study.item.ItemSearchCondition
import com.example.study.item.domain.Item
import com.example.study.item.domain.QItem.item
import com.example.study.member.enums.ItemType
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.util.*


class ItemRepositoryImpl(private val queryFactory: JPAQueryFactory): ItemRepositoryCustom {

    override fun getAllItems(condition: ItemSearchCondition, pageable: Pageable): Page<Item> {

        val where = BooleanBuilder()
        where.and(containSearchName(condition.searchName))
        where.and(inItemTypes(condition.itemType))

        val result: List<Item> = queryFactory
            .select(item)
            .from(item)
            .where(where)
            .fetch()

        val count: JPAQuery<Long> = queryFactory
            .select(item.count())
            .from(item)
            .where(where)
        return PageableExecutionUtils.getPage(result, pageable) { count.fetchOne()!! }
    }

    private fun containSearchName(searchName: String): BooleanExpression? {
        return if (StringUtils.isNullOrEmpty(searchName)) null else
            item.itemName.contains(searchName)
    }

    private fun inItemTypes(itemType: String): BooleanExpression? {
        if (StringUtils.isNullOrEmpty(itemType)) {
            return null
        }

        val itemTypes: Array<String> = itemType.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val itemTypeList: MutableList<ItemType> = ArrayList()
        Arrays.stream(itemTypes).forEach { c: String ->
            itemTypeList.add(
                ItemType.valueOf(c.trim())
            )
        }
        return item.itemType.`in`(itemTypeList)
    }
}