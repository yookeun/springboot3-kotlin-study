package com.example.study.converter

import com.example.study.handler.EncryptHandler
import com.querydsl.core.util.StringUtils
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.hibernate.query.sqm.tree.SqmNode.log

@Converter
class EncDecConverter(private val encryptHandler: EncryptHandler): AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(plainText: String): String? {
        if (StringUtils.isNullOrEmpty(plainText)) {
            return null
        }
        val encrypted: String = try {
            encryptHandler.encrypt(plainText)
        } catch (e: Exception) {
            log.error(e.toString())
            throw RuntimeException(e)
        }
        return encrypted
    }

    override fun convertToEntityAttribute(encrypted: String?): String {
        if (StringUtils.isNullOrEmpty(encrypted)) {
            return ""
        }
        val decrypted: String = try {
            encryptHandler.decrypt(encrypted)
        } catch (e: java.lang.Exception) {
            log.error(e.toString())
            throw java.lang.RuntimeException(e)
        }
        return decrypted
    }
}