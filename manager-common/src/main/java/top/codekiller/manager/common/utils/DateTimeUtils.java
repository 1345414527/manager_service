package top.codekiller.manager.common.utils;

import java.time.*;

/**
 * @author codekiller
 * @date 2020/7/6 13:59
 * @Description 时间工具类
 */
public class DateTimeUtils {

    /**
    * @Description 计算两个时间之间的毫秒数
    * @date 2020/7/6 14:00
    * @param start  开始时间
    * @param end   结束时间
    * @return java.lang.Long
    */
    public static Long calculateInterval(LocalDateTime start, LocalDateTime end){
        if(start==null||end==null){
            throw new NullPointerException("时间计算操作，时间为null");
        }

        long startMilli = Instant.from(start.atZone(ZoneId.systemDefault())).toEpochMilli();
        long endMilli = Instant.from(end.atZone(ZoneId.systemDefault())).toEpochMilli();
        return (endMilli-startMilli);
    }

    /**
    * @Description 根据给定时间和毫秒数计算下一时间
    * @date 2020/7/6 14:01
    * @param dateTime    给定时间
    * @param millis   毫秒数
    * @return java.time.LocalDateTime
    */
    public static LocalDateTime calculateLocalDateTimeWithMillis(LocalDateTime dateTime,Long millis){
        if(dateTime==null){
            throw new NullPointerException("时间计算操作，时间为null");
        }

        long timeMillis = Instant.from(dateTime.atZone(ZoneId.systemDefault())).toEpochMilli();
        return Instant.ofEpochMilli(timeMillis+millis).atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    /**
    * @Description 根据给定时间和分钟数计算下一时间
    * @date 2020/7/6 14:30
    * @param dateTime  给定时间
    * @param minutes  分钟数
    * @return java.time.LocalDateTime
    */
    public static LocalDateTime calculateLocalDateTimeWithMinute(LocalDateTime dateTime,Long minutes){
        if(dateTime==null){
            throw new NullPointerException("时间计算操作，时间为null");
        }

        return DateTimeUtils.calculateLocalDateTimeWithMillis(dateTime,minutes*60*1000);
    }

    /**
    * @Description 将毫秒数转为LocalDateTime
    * @date 2020/7/6 14:15
    * @param millis  毫秒数
    * @return java.time.LocalDateTime
    */
    public static LocalDateTime convertMillis2LocalDateTime(Long millis){
        return  Instant.ofEpochMilli(millis).atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }

    /**
    * @Description 获取LocalDateTime的毫秒数
    * @date 2020/7/6 14:17
    * @param dateTime
    * @return java.lang.Long
    */
    public static Long convertLocalDateTime2Millis(LocalDateTime dateTime){
        if(dateTime==null){
            throw new NullPointerException("指定时间转换成毫秒数异常，时间为null");
        }

        return Instant.from(dateTime.atZone(ZoneId.systemDefault())).toEpochMilli();
    }

    /**
    * @Description 获取两个时间中小的那个
    * @date 2020/7/6 14:43
    * @param first
    * @param second
    * @return java.time.LocalDateTime
    */
    public static LocalDateTime getEarlierLocalDateTime(LocalDateTime first,LocalDateTime second){
        if(first==null||second==null){
            throw new NullPointerException("两时间比较，有一时间为null");
        }

        Long interval = DateTimeUtils.calculateInterval(first, second);

        //当第二个时间大
        if(interval>=0){
            return first;
        }
        return second;
    }

    /**
    * @Description 将对应的数字转化为月份
    * @date 2020/7/13 22:43
    * @param month
    * @param isCHS
    * @return java.lang.String
    */
    public static String mapperMonthByNumber(Integer month,Boolean isCHS){
        switch (month){
            case 1:  return isCHS?"一月":"Jan";
            case 2:  return isCHS?"二月":"Feb";
            case 3:  return isCHS?"三月":"Mar";
            case 4:  return isCHS?"四月":"Apr";
            case 5:  return isCHS?"五月":"May";
            case 6:  return isCHS?"六月":"Jun";
            case 7:  return isCHS?"七月":"Jul";
            case 8:  return isCHS?"八月":"Aug";
            case 9:  return isCHS?"九月":"Sept";
            case 10: return isCHS?"十月":"Oct";
            case 11: return isCHS?"十一月":"Nov";
            case 12: return isCHS?"十二月":"Dec";
            default: throw  new DateTimeException("月份映射失败");
        }

    }

    /**
    * @Description 将对应的数字转化为星期天数
    * @date 2020/7/13 22:43
    * @param weekDay
    * @param isCHS
    * @return java.lang.String
    */
    public static String mapperWeekDayByNumber(Integer weekDay,Boolean isCHS){
        switch (weekDay) {
            case 1: return isCHS?"星期一":"Mon";
            case 2: return isCHS?"星期二":"Tues";
            case 3: return isCHS?"星期三":"Wed";
            case 4: return isCHS?"星期四":"Thur";
            case 5: return isCHS?"星期五":"Fri";
            case 6: return isCHS?"星期六":"Sat";
            case 7: return isCHS?"星期日":"Sun";
            default: throw  new DateTimeException("星期天数映射失败");
        }

    }


}
