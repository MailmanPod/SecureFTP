/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.logic;

import java.util.Calendar;

/**
 *
 * @author Quality of Service
 */
public class DateScheduler {
    private int hour;
    private int minute;
    private int second;
    private int day;
    private int month;
    private int year;
    
    public static final int JANUARY = Calendar.JANUARY + 1;
    public static final int FEBRUARY = Calendar.FEBRUARY + 1;
    public static final int MARCH = Calendar.MARCH + 1;
    public static final int APRIL = Calendar.APRIL + 1;
    public static final int MAY = Calendar.MAY + 1;
    public static final int JUNE = Calendar.JUNE + 1;
    public static final int JULY = Calendar.JULY + 1;
    public static final int AUGUST = Calendar.AUGUST + 1;
    public static final int SEPTEMBER = Calendar.SEPTEMBER + 1;
    public static final int OCTOBER = Calendar.OCTOBER + 1;
    public static final int NOVEMBER = Calendar.NOVEMBER + 1;
    public static final int DECEMBER = Calendar.DECEMBER + 1;

    public DateScheduler(int hour, int minute, int second, int day, int month, int year) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "DateScheduler{" + "hour=" + hour + ", minute=" + minute + ", second=" + second + 
                ", day=" + day + ", month=" + month + ", year=" + year + '}';
    }
}
