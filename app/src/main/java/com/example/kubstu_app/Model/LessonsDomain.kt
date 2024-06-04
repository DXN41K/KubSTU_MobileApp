package com.example.kubstu_app.Model

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


data class LessonsDomain(
    val type: String,
    var audience: String,
    val name: String,
    val teacher: String,
    var pair: Int,
    var date: String
)
{
    var time: String

    var hasLeft: String
    private var durTimeStart: String
    private var durTimeEnd: String

    private val pairMap = mapOf(
        1 to "8:00",
        2 to "9:40",
        3 to "11:20",
        4 to "13:20",
        5 to "15:00",
        6 to "16:40",
        7 to "18:20")
    private val ElsePair = "00:00"

    init {

        if (audience == "Ф-Спортивный комплекс") {
            audience = "Ф"
        }
        time = pairMap[pair] ?: ElsePair



        val formater = DateTimeFormatter.ofPattern("H:mm")
        val tim = LocalTime.parse(time, formater)
        val date = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
        val dateTimeStart = LocalDateTime.of(date, tim)


        durTimeStart = dateTimeStart.format(formater)
        val dateTimeEnd = dateTimeStart.plusHours(1).plusMinutes(30)
        durTimeEnd = dateTimeEnd.format(formater)

        hasLeft = getDifferenceTime(dateTimeStart, dateTimeEnd)

        }

    //функция вывода строки времени занятия
    fun GetThisTime() = "$durTimeStart - $durTimeEnd"

    //функции выводящие оставшееся время до начала занятия
    private fun getDifferenceTime(dateTimeStart: LocalDateTime, dateTimeEnd: LocalDateTime ): String {


        val result = StringBuilder().append("Через")

        val currentTime = LocalDateTime.now()
        var duration = Duration.between(currentTime, dateTimeEnd)
        if (duration.isNegative) {
            return result.clear().append("Занятие окончено").toString()
        }
        duration = Duration.between(currentTime, dateTimeStart)

        val days = duration.toDays().toInt()
        val hours = (duration.toHours() % 24).toInt()
        val minutes = (duration.toMinutes() % 60).toInt()
        val seconds = (duration.seconds % 60).toInt()

        if (days > 0) setDays(result, days)

        else if (hours + minutes > 0) {
            if (hours > 0) {
                setHours(result, hours)
            }
            if (minutes > 0) {
                setMinutes(result, minutes)
            }
        }
        else {
                if (seconds > 0) result.append(" меньше минуты")
                else result.clear().append("Идет занятие")
            }

        return result.toString()
    }
    private fun setMinutes(result: StringBuilder, minutes: Int){
        if (minutes in 5..20) {
            result.append(" $minutes минут")
            return
        }
        val lastDigit = minutes % 10
        when {
            lastDigit == 1 -> result.append(" $minutes минута")
            lastDigit in 2..4 -> result.append(" $minutes минуты")
            else -> result.append(" $minutes минут")

        }
    }
    private fun setHours(result: StringBuilder, hours: Int){
        if (hours in 5..20) {
            result.append(" $hours часов")
            return
        }
        if (hours % 10 == 1) {
            result.append(" $hours час")
            return
        }
        result.append(" $hours часа")
    }
    private fun setDays(result: StringBuilder, days: Int){
        if (days in 5..20) {
            result.append(" $days дней")
            return
        }
        val lastDigit = days % 10
        when {
            lastDigit == 1 -> result.append(" $days день")
            lastDigit in 2..4 -> result.append(" $days дня")
            else -> result.append(" $days дней")

        }
    }
}