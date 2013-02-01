package org.comcast.logic;

import java.util.Calendar;

/**
 * Esta clase es la encargada de realizar el manejo de las fechas y horas dentro
 * del programa.
 *
 * @author Damian Bruera.
 * @version 1.5
 * @since 2012.
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

    /**
     * Constructor de la clase.
     *
     * @param hour Hora en formato 24HS.
     * @param minute Minutos.
     * @param second Segundos.
     * @param day Dia.
     * @param month Mes.
     * @param year Año.
     */
    public DateScheduler(int hour, int minute, int second, int day, int month, int year) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Obtiene la hora en formato 24hs.
     *
     * @return int con la hora.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Modifica la hora en formato 24hs.
     *
     * @param hour Con la nueva hora
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Obtiene los minutos.
     *
     * @return int Con los minutos.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Modifica los minutos.
     *
     * @param minute Con el nuevo minuto.
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Obtiene los segundos transcurridos.
     *
     * @return int COn los minutos.
     */
    public int getSecond() {
        return second;
    }

    /**
     * Modifica los minutos transcurridos.
     *
     * @param second Con los nuevos minutos.
     */
    public void setSecond(int second) {
        this.second = second;
    }

    /**
     * Obtiene el dia.
     *
     * @return int Con el dia.
     */
    public int getDay() {
        return day;
    }

    /**
     * Modifica el dia.
     *
     * @param day Con el nuevo dia.
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Obtiene el mes.
     *
     * @return int con el mes.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Modifica el mes.
     *
     * @param month Con el nuevo mes.
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Obtiene el año.
     *
     * @return int Con el año.
     */
    public int getYear() {
        return year;
    }

    /**
     * Modifica el año.
     *
     * @param year Con el nuevo año.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * toString personalizado, para mostrar la fecha y hora almacenada en el
     * objeto.
     *
     * @return String con la cadena.
     * @overrides toString.
     */
    @Override
    public String toString() {
        return "DateScheduler{" + "hour=" + hour + ", minute=" + minute + ", second=" + second
                + ", day=" + day + ", month=" + month + ", year=" + year + '}';
    }
}
