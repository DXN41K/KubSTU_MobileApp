package com.example.kubstu_app.Model

import java.lang.StringBuilder
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class LessonsDomain(val type: String,
                         val time: LocalDateTime,
                         val audience: String,
                         val name: String,
                         val teacher: String,
                         var altTime: String? = null) {

    var hasLeft: String
    var durTimeStart: String
    var durTimeEnd: String
    init {
        hasLeft = getDifferenceDate()

        val formater = DateTimeFormatter.ofPattern("H:mm")
        durTimeStart = time.format(formater)
        val newtime = time.plusHours(1).plusMinutes(30)
        durTimeEnd = newtime.format(formater)

        if (altTime == null) {
            altTime = when (durTimeStart) {
                ("8:00") -> "1 пара"
                ("9:40") -> "2 пара"
                ("11:20") -> "3 пара"
                ("13:20") -> "4 пара"
                ("15:00") -> "5 пара"
                ("16:40") -> "6 пара"
                ("18:20") -> "7 пара"
                else -> "Вне пар"
            }
        }
    }

    fun getTime() = "$durTimeStart - $durTimeEnd"
    private fun getDifferenceDate() :String {
        val result = StringBuilder()

        val currentTime = LocalDateTime.now()
        val duration = Duration.between(currentTime, time)

        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60

        if (days > 0) result.append("Осталось $days дней")
        else {
            if (hours > 0) {
                result.append("Осталось $hours часов")
                if (minutes > 0) result.append(", $minutes минут")
            }
            else {
                if (minutes > 0) result.append("Осталось $minutes минут")
                else result.append("Занятие началось")
            }
        }

        return result.toString()
    }
}