package com.mvrc.viewapp.core

import android.content.Context
import android.util.Log
import com.mvrc.viewapp.R
import java.util.Date

/**
 * Utility object class for managing logging and providing common methods such as extensions.
 */
object Utils {

    /**
     * Log a message with a specified tag and message.
     *
     * @param tag The tag to identify the log entry.
     * @param message The message to be logged.
     */
    fun logMessage(tag: String, message: String) = Log.e(tag, message)

    /**
     * Estimates the read time of a text based on the average reading speed.
     *
     * The function assumes an average reading speed of 200 words per minute
     * and calculates the estimated reading time in minutes. If the estimated
     * time is less than 1 minute, it returns "Less than a minute." If the time
     * is less than 60 minutes, it returns the time in minutes. Otherwise, it
     * returns the time in hours.
     *
     * @param text The input text for which to estimate the read time.
     * @param context The context to get the texts from the resources.
     * @return A string representing the estimated read time.
     */
    fun estimateReadTime(text: String, context: Context): String {
        val wordsPerMinute = 200

        // Calculate the number of words in the text
        val wordCount = text.noEmptyWordsCount()

        // Calculate the estimated reading time in minutes.
        var minutes = wordCount / wordsPerMinute
        if (minutes == 0) {
            minutes = 1
        }
        val hours = minutes / 60

        return when {
            minutes < 60 -> "$minutes ${context.resources.getString(R.string.min)}"
            hours == 1 -> "$minutes ${context.resources.getString(R.string.hour)}"
            else -> "$minutes ${context.resources.getString(R.string.hours)}"
        }
    }

    /**
     * Returns a difference string based on the provided [dateTime].
     *
     * The function calculates the time difference between the [dateTime] and the current time.
     * It formats and returns a string such as "1 minute ago," "3 hours ago," "3 days ago," or "3 years ago."
     *
     * @param dateTime The input date time for which to calculate the time ago string.
     * @param context The context to get the texts from the resources.
     * @return A string representing the time ago string.
     */
    fun calculateTimeAgoString(dateTime: Date, context: Context): String {
        val now = Date()
        val difference = (now.time - dateTime.time) / 1000
        val res = context.resources

        return when {
            difference < 60 -> "$difference ${
                if (difference.toInt() == 1) res.getString(R.string.second) else res.getString(
                    R.string.seconds
                )
            } ${res.getString(R.string.ago)}"

            difference < 60 * 60 -> {
                val minutes = (difference / 60).toInt()
                "$minutes ${if (minutes == 1) res.getString(R.string.minute) else res.getString(R.string.minutes)} ${
                    res.getString(
                        R.string.ago
                    )
                }"
            }

            difference < 60 * 60 * 24 -> {
                val hours = (difference / (60 * 60)).toInt()
                "$hours ${if (hours == 1) res.getString(R.string.hour) else res.getString(R.string.hours)} ${
                    res.getString(
                        R.string.ago
                    )
                }"
            }

            difference < 60 * 60 * 24 * 30 -> {
                val days = (difference / (60 * 60 * 24)).toInt()
                "$days ${if (days == 1) res.getString(R.string.day) else res.getString(R.string.days)} ${
                    res.getString(
                        R.string.ago
                    )
                }"
            }

            difference < 60 * 60 * 24 * 365 -> {
                val months = (difference / (60 * 60 * 24 * 30)).toInt()
                "$months ${if (months == 1) res.getString(R.string.month) else res.getString(R.string.months)} ${
                    res.getString(
                        R.string.ago
                    )
                }"
            }

            else -> {
                val years = (difference / (60 * 60 * 24 * 365)).toInt()
                "$years ${if (years == 1) res.getString(R.string.year) else res.getString(R.string.years)} ${
                    res.getString(
                        R.string.ago
                    )
                }"
            }
        }
    }

    /**
     * Abbreviates a large number for concise display.
     *
     * @param numberToAbbreviate The number to be abbreviated.
     * @return The abbreviated string representation of the number.
     */
    fun abbreviateNumber(numberToAbbreviate: Int): String {
        return when {
            numberToAbbreviate >= 1000000 -> "${numberToAbbreviate / 1000000}M"
            numberToAbbreviate >= 1000 -> "${numberToAbbreviate / 1000}K"
            else -> "$numberToAbbreviate"
        }
    }
}

/**
 * Extension to calculate the number of non-empty words inside a String.
 */
fun String.noEmptyWordsCount(): Int {
    // Return the number of non-empty words in the string
    return split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .size
}