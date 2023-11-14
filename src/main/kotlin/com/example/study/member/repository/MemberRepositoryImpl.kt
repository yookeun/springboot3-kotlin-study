package com.example.study.member.repository

import com.example.study.member.domain.Member
import com.example.study.member.domain.QMember.member
import com.example.study.member.dto.MemberSearchCondition
import com.example.study.member.enums.Gender
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils


class MemberRepositoryImpl(private val queryFactory: JPAQueryFactory): MemberRepositoryCustom {
    override fun getAllMembers(condition: MemberSearchCondition, pageable: Pageable): Page<Member> {

        val where = BooleanBuilder()
        where.and(containsSearchName(condition.searchName))
        where.and((eqIsGender(condition.gender)))

        val result: List<Member> = queryFactory
            .select(member)
            .from(member)
            .where(where)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: JPAQuery<Long> = queryFactory
            .select(member.count())
            .from(member)
            .where(where)

        return PageableExecutionUtils.getPage(result, pageable) { count.fetchOne() !! }
    }

    private fun containsSearchName(searchName: String): BooleanExpression? {
        return if (StringUtils.isNullOrEmpty(searchName)) null else
            member.userId.contains(searchName).or(member.name.contains(searchName))
    }

    private fun eqIsGender(gender: String): BooleanExpression? {

        return if (StringUtils.isNullOrEmpty(gender)) null else
            member.gender.eq(Gender.valueOf(gender))
    }

}