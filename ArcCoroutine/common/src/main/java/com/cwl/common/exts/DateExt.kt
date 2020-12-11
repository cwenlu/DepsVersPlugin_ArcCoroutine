package com.cwl.common.exts

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Long.toDate()=Date(this)

fun String.toDate(format: String = "yyyy-MM-dd HH:mm:ss")=SimpleDateFormat(format, Locale.getDefault()).parse(this)

/**
 * 日期对应的毫秒值
 */
fun String.toDateMills(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).parse(this).time

/**
 * 时间戳转日期格式
 */
fun Long.toDateString(format: String = "yyyy-MM-dd HH:mm:ss") = Date(this).toDateString(format)

fun Date.toDateString(format: String = "yyyy-MM-dd HH:mm:ss")=SimpleDateFormat(format, Locale.getDefault()).format(this)
/**
 * 格式转化
 * @ifmt 输入格式
 * @ofmt 输出格式
 */
fun String.fmtConvert(ifmt: String="yyyy-MM-dd HH:mm:ss",ofmt: String="yyyy-MM-dd HH:mm")=toDateMills(ifmt).toDateString(ofmt)

fun Pair<String,String>.fmtConvert(ofmt: String="yyyy-MM-dd HH:mm")=first.fmtConvert(second,ofmt)

/**
 * 格林尼治（EEE, dd MMM y HH:mm:ss ‘GMT’）转化为毫秒
 */
fun String.toGMTMills(): Long {
    val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    var date = Date()
    try {
        date = formatter.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return date.time
}

fun Long.toGMLString()=Date(this).toGMLString()

fun Date.toGMLString():String{
    var formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter.format(this)
}

/**
 * @day 加/减天数
 */
fun Long.plusDays(days:Long):Date{
    var daysMills=days*24*60*60*1000
    return Date(this+daysMills)
}

fun Date.plusDays(days:Long):Date=time.plusDays(days)

fun String.plusDays(format: String = "yyyy-MM-dd HH:mm:ss",days:Long)=toDateMills(format).plusDays(days)

/**
 * 2个日期相减，得到天数，不足一天（24h）的不算
 */
operator fun Date.minus(date:Date):Long=(time-date.time)/(24*60*60*1000)

/**
 * 相差天数
 * @date 日期
 * @format 格式
 */
fun Date.distanceDays(date:String,format:String="yyyy-MM-dd HH:mm:ss")=(time-date.toDateMills(format))/(24*60*60*1000)

/**
 * @date 减去的日期
 * @dfmt 减去日期格式
 * @ifmt 被减日期格式
 */
fun String.distanceDays(date:String,dfmt:String="yyyy-MM-dd HH:mm:ss",ifmt:String="yyyy-MM-dd HH:mm:ss")=(this.toDateMills(ifmt)-date.toDateMills(dfmt))/(24*60*60*1000)

/**
 * [pair.first] 日期
 * [pair.second] 格式
 */
fun Pair<String,String>.distanceDays(pair: Pair<String,String>)=first.distanceDays(pair.first,pair.second,second)

fun Pair<String,String>.distanceDays(date: String,format: String="yyyy-MM-dd HH:mm:ss")=first.distanceDays(date,format,second)

/**
 * 指定天开始时间
 * @ofmt 输出格式
 */
fun Date.startTimeOfTheDay(ofmt:String="yyyy-MM-dd"):String=toDateString("$ofmt 00:00:00")

/**
 * @ofmt 如果为null则采用ifmt的格式
 */
fun String.startTimeOfTheDay(ifmt: String="yyyy-MM-dd",ofmt:String?=null):String=fmtConvert(ifmt,"${ofmt?:ifmt} 00:00:00")

fun Pair<String,String>.startTimeOfTheDay(ofmt:String?=null)=first.startTimeOfTheDay(second,ofmt)

/**
 * 指定天结束时间
 */
fun Date.endTimeOfTheDay(ofmt:String="yyyy-MM-dd"):String=toDateString("$ofmt 23:59:59")

fun String.endTimeOfTheDay(ifmt: String="yyyy-MM-dd",ofmt:String?=null):String=fmtConvert(ifmt,"${ofmt?:ifmt} 23:59:59")

fun Pair<String,String>.endTimeOfTheDay(ofmt:String?=null)=first.endTimeOfTheDay(second,ofmt)

/**
 * 指定时间周的第一天
 */
fun Date.startTimeOfTheWeek(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
//    //一周第一天是否为星期天
//    val isFirstSunday = calendar.getFirstDayOfWeek() === Calendar.SUNDAY
//    if(isFirstSunday){
//        //先减1是要修正周一，再减1是计算周一的时间
//        var weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 2
//        calendar.add(Calendar.DATE,-weekDay)
//    }else{
//        var weekDay=calendar.get(Calendar.DAY_OF_WEEK)-1
//        calendar.add(Calendar.DATE,-weekDay)
//    }

    //换种写法
    calendar.firstDayOfWeek=Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)//calendar.firstDayOfWeek
    return calendar.time.toDateString("$ofmt 00:00:00")
}

fun String.startTimeOfTheWeek(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().startTimeOfTheWeek(ofmt?:ifmt)

fun Pair<String,String>.startTimeOfTheWeek(ofmt:String?=null)=first.startTimeOfTheWeek(second,ofmt)
/**
 * 某年某周的第一天
 *
 */
fun Pair<Int,Int>.startTimeOfTheYearWeek(ofmt: String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR,first)
    calendar.set(Calendar.WEEK_OF_YEAR, second)
    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
    return  calendar.time.toDateString("$ofmt 00:00:00")
}

/**
 * 某年某周最后一天
 */
fun Pair<Int,Int>.endTimeOfTheYearWeek(ofmt: String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.firstDayOfWeek=Calendar.MONDAY
    calendar.set(Calendar.YEAR,first)
    calendar.set(Calendar.WEEK_OF_YEAR, second)
    calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
    return calendar.time.toDateString("$this 23:59:59")
}

/**
 * 指定时间周的最后一天
 */
fun Date.endTimeOfTheWeek(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
//    //一周第一天是否为星期天
//    val isFirstSunday = calendar.getFirstDayOfWeek() === Calendar.SUNDAY
//    if(isFirstSunday){
//        //星期六正好是7换成我们的星期天正好8-7  (8-weekDay)
//        var weekDay=calendar.get(Calendar.DAY_OF_WEEK)
//        calendar.add(Calendar.DATE,8-weekDay)
//    }else{
//        var weekDay=calendar.get(Calendar.DAY_OF_WEEK)
//        calendar.add(Calendar.DATE,7-weekDay)
//    }

    calendar.firstDayOfWeek=Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
    return calendar.time.toDateString("$ofmt 23:59:59")
}

fun String.endTimeOfTheWeek(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().endTimeOfTheWeek(ofmt?:ifmt)

fun Pair<String,String>.endTimeOfTheWeek(ofmt:String?=null)=first.endTimeOfTheWeek(second,ofmt)

/**
 * 月第一天
 */
fun Date.startTimeOfTheMonth(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
    calendar.set(Calendar.DATE,1)
    return calendar.time.toDateString("$ofmt 00:00:00")
}

fun String.startTimeOfTheMonth(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().startTimeOfTheMonth(ofmt?:ifmt)

fun Pair<String,String>.startTimeOfTheMonth(ofmt:String?=null)=first.startTimeOfTheMonth(second,ofmt)


/**
 * 月最后一天
 */
fun Date.endTimeOfTheMonth(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
    calendar.set(Calendar.DATE,1)//设置到月第一天
    calendar.add(Calendar.MONTH,1)//加一个月
    calendar.add(Calendar.DATE,-1)//减一天则为上月最后一天
    return calendar.time.toDateString("$ofmt 23:59:59")
}

fun String.endTimeOfTheMonth(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().endTimeOfTheMonth(ofmt?:ifmt)

fun Pair<String,String>.endTimeOfTheMonth(ofmt:String?=null)=first.endTimeOfTheMonth(second,ofmt)

/**
 * 年第一天
 */
fun Date.startTimeOfTheYear(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
    calendar.set(Calendar.MONTH, 0)
    calendar.set(Calendar.DATE, 1)
    return calendar.time.toDateString("$ofmt 00:00:00")
}

fun String.startTimeOfTheYear(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().startTimeOfTheYear(ofmt?:ifmt)

fun Pair<String,String>.startTimeOfTheYear(ofmt:String?=null)=first.startTimeOfTheYear(second,ofmt)

/**
 * 年最后一天
 */
fun Date.endTimeOfTheYear(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
    calendar.set(Calendar.MONTH, 11)
    calendar.set(Calendar.DATE,31)
    return calendar.time.toDateString("$ofmt 23:59:59")
}

fun String.endTimeOfTheYear(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().endTimeOfTheYear(ofmt?:ifmt)

fun Pair<String,String>.endTimeOfTheYear(ofmt:String?=null)=first.endTimeOfTheYear(second,ofmt)

/**
 * 季度第一天
 */
fun Date.startTimeOfTheQuarter(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
    var currMonth=calendar.get(Calendar.MONTH)
    if(currMonth<=2){//0,1,2
        calendar.set(Calendar.MONTH,0)
    }else if(currMonth<=5){//3,4,5
        calendar.set(Calendar.MONTH,3)

    }else if(currMonth<=8){//6,7,8
        calendar.set(Calendar.MONTH,6)
    }else{//9,10,11
        calendar.set(Calendar.MONTH,9)
    }
    return calendar.time.toDateString("$ofmt 00:00:00")
}

fun String.startTimeOfTheQuarter(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().startTimeOfTheQuarter(ofmt?:ifmt)

fun Pair<String,String>.startTimeOfTheQuarter(ofmt:String?=null)=first.startTimeOfTheQuarter(second,ofmt)


/**
 * 季度最后一天
 */
fun Date.endTimeOfTheQuarter(ofmt:String="yyyy-MM-dd"):String{
    var calendar = Calendar.getInstance()
    calendar.time=this
    var currMonth=calendar.get(Calendar.MONTH)
    if(currMonth<=2){//0,1,2
        calendar.set(Calendar.MONTH,2)
        calendar.set(Calendar.DATE,31)
    }else if(currMonth<=5){//3,4,5
        calendar.set(Calendar.MONTH,5)
        calendar.set(Calendar.DATE,30)

    }else if(currMonth<=8){//6,7,8
        calendar.set(Calendar.MONTH,8)
        calendar.set(Calendar.DATE,30)

    }else{//9,10,11
        calendar.set(Calendar.MONTH,11)
        calendar.set(Calendar.DATE,31)
    }
    return calendar.time.toDateString("$ofmt 23:59:59")
}

fun String.endTimeOfTheQuarter(ifmt: String="yyyy-MM-dd",ofmt:String?=null)=toDateMills(ifmt).toDate().endTimeOfTheQuarter(ofmt?:ifmt)

fun Pair<String,String>.endTimeOfTheQuarter(ofmt:String?=null)=first.endTimeOfTheQuarter(second,ofmt)

/**
 * 日期属于当年第几周
 */
fun String.weekTheYear(ifmt:String="yyyy-MM-dd"):Int{
    var calendar = Calendar.getInstance()
    calendar.firstDayOfWeek=Calendar.MONDAY
    calendar.time=toDate()
    return calendar.get(Calendar.WEEK_OF_YEAR)
}

/**
 * 当年第几天
 */
fun String.dayTheYear(ifmt:String="yyyy-MM-dd"):Int{
    var calendar = Calendar.getInstance()
    calendar.time=toDate()
    return calendar.get(Calendar.DAY_OF_YEAR)
}