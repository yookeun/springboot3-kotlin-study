package com.example.study.converter

import com.querydsl.core.util.StringUtils
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter


@Converter
class YnToBooleanConverter: AttributeConverter<String, Boolean> {
    override fun convertToDatabaseColumn(attribute: String?): Boolean {
        if (StringUtils.isNullOrEmpty(attribute)) {
            return false
        }
        return attribute.equals("Y")
    }

    override fun convertToEntityAttribute(dbData: Boolean): String {
        return if (dbData) "Y" else "N"
    }


}