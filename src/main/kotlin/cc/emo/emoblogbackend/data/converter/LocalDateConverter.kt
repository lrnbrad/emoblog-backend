package cc.emo.emoblogbackend.data.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.sql.Date
import java.time.LocalDate

@Converter(autoApply = true)
class LocalDateConverter : AttributeConverter<LocalDate, Date> {
    override fun convertToDatabaseColumn(attribute: LocalDate?): Date? {
        return attribute?.let { Date.valueOf(it) }
    }

    override fun convertToEntityAttribute(dbData: Date?): LocalDate? {
        return dbData?.toLocalDate()
    }
}