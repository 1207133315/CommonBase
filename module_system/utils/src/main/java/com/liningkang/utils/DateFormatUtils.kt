package com.liningkang.utils


import android.content.Context
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

val dateFormat = SimpleDateFormat("yyyy-MM-dd")
val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
fun Date.formatTime() = dateTimeFormat.format(this)!!

// 转换成年月日格式
fun Date.format() = dateFormat.format(this)!!

// 转换成年月格式
fun Date.formatMonth() = SimpleDateFormat("yyyy-MM").format(this)!!

// 转换成月日格式
fun Date.formatMonthAndDay(): String {
    var calendar = Calendar.getInstance()
    calendar.timeInMillis = this.time
    return SimpleDateFormat("MM-dd").format(calendar.time)
}


// 转换成月日格式
fun Date.formatString(context: Context, format: String): String {
    return SimpleDateFormat(format, context.resources.configuration.locale).format(this.time)
}

fun String.formatDate(context: Context, format: String, string: String): Date? {
// 使用SimpleDateFormat将字符串转换为Date
    val sdf = SimpleDateFormat(format)
    var date: Date? = null
    try {
        date = sdf.parse(string)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date
}


fun Date.formatDateWithDayOfWeek(context: Context): String {
    val currentLocale: Locale = context.resources.configuration.locale
    val monthDayFormat = SimpleDateFormat("MMM d", currentLocale)
    val dayOfWeekFormat = SimpleDateFormat("EEEE", currentLocale)

    val monthDayString = monthDayFormat.format(this)
    val dayOfWeekString = dayOfWeekFormat.format(this)

    return "$monthDayString,$dayOfWeekString"
}


fun Date.formatResult(context: Context): String {
    // 获取当前语言环境
    val currentLocale: Locale = context.resources.configuration.locale
    // 创建 SimpleDateFormat 实例并使用当前语言环境
    val sdf = SimpleDateFormat("MMM.d HH:mm", currentLocale)
    return sdf.format(this)
}


object DateFormatUtils {

    fun formatEnglishByYearMonth(year: Int, month: Int): String? {
        val sdf = SimpleDateFormat("MMM yyyy", Locale.ENGLISH) // 指定英文月份格式和英语区域
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return sdf.format(calendar.time)
    }

    fun formatEnglishMonth(context: Context, month: Int): String {
        val sdf = SimpleDateFormat("MMMM", context.resources.configuration.locale)
        val date = Date(0, month - 1, 1)
        return sdf.format(date)
    }


    fun formatEnglishByMonth(month: Int): String {
        // 获取月份的英文名
        // 获取月份的英文名
        val monthNames: Array<String> = DateFormatSymbols(Locale.ENGLISH).getMonths()
        return monthNames[month - 1]

    }

    fun formatMonthByEnglish(englishMonth: String): Int {
        val monthNames = DateFormatSymbols(Locale.ENGLISH).months
        var month = -1 // 初始化为 -1，表示未找到
        var englishMonth1 = englishMonth.toLowerCase()

        for (i in monthNames.indices) {
            if (englishMonth1.equals(monthNames[i].toLowerCase())) {
                month = i
                break
            }
        }
        return month
    }


    fun formatResult(context: Context, date: Date?): String {
        if (date == null) {
            return ""
        }
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("MMM d, HH:mm", currentLocale)
        return sdf.format(date)
    }

    fun formatYear(context: Context, date: Date?): String {
        if (date == null) {
            return ""
        }
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("MMM d,yyyy", currentLocale)
        return sdf.format(date)
    }

    fun formatTime(context: Context, date: Date?): String {
        if (date == null) {
            return ""
        }
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("HH:mm", currentLocale)
        return sdf.format(date)
    }

    fun formatTime(context: Context, time: Long): String {
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("yyyy-MM-dd", currentLocale)
        return sdf.format(Date(time))
    }

    fun convertTimeStampToCustomFormat(timeStamp: Long): String {
        val dateFormat = SimpleDateFormat("d MMMM EEEE", Locale.ENGLISH)
        return dateFormat.format(Date(timeStamp))
    }

    fun formatEnglishByYearMonth(timeStamp: Long): String {
        val sdf = SimpleDateFormat("MMM yyyy", Locale.ENGLISH) // 指定英文月份格式和英语区域

        return sdf.format(timeStamp)
    }

    fun formatReportChart(context: Context, time: Long): String {
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("dd MMM", currentLocale)
        return sdf.format(Date(time))
    }

    fun formatMonthChart(context: Context, time: Long): String {
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("MMMM yyyy", currentLocale)
        return sdf.format(Date(time))
    }

    fun formatYearChart(context: Context, time: Long): String {
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("yyyy", currentLocale)
        return sdf.format(Date(time))
    }


    fun formatDate(context: Context, date: Date?): String {
        if (date == null) {
            return ""
        }
        // 获取当前语言环境
        val currentLocale: Locale = context.resources.configuration.locale
        // 创建 SimpleDateFormat 实例并使用当前语言环境
        val sdf = SimpleDateFormat("M.d", currentLocale)
        return sdf.format(date)
    }

    // 转换成月日格式
    fun formatString(context: Context, date: Date, format: String): String {
        return SimpleDateFormat(format, context.resources.configuration.locale).format(date.time)
    }

    // 判断两个时间戳是否是同一天
    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = timestamp1

        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = timestamp2

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    fun isToday(timestamp: Long): Boolean {
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        return date == today
        /* val calendar = Calendar.getInstance()
         calendar.timeInMillis = timestamp
         var timeYear=calendar.get(Calendar.YEAR)
         var timeDayOfYear=calendar.get(Calendar.DAY_OF_YEAR)

         calendar.timeInMillis = System.currentTimeMillis()
         var todayYear=calendar.get(Calendar.YEAR)
         var todayDayOfYear=calendar.get(Calendar.DAY_OF_YEAR)

         return timeYear==todayYear &&
                timeDayOfYear==todayDayOfYear*/
    }
}

