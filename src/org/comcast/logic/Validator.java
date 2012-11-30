package org.comcast.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que tiene como fin presentar los metodos mas comunmente usados para validar
 * cierto tipos de datos. <br>
 * Sus metodos utilizan la validacion segun expresiones regulares, y otros son
 * validaciones que no necesariamente las utilizan.
 * @author Federico Bruera TSB 2010.
 */
public class Validator {

    /**
     * Constructor vacio por defecto.
     */
    public Validator() {
    }

    /**
     * Este metodo valida textos. <br>
     * Verifica si hay espacios en blanco al principio y al final, numeros,
     * otros signos de puntuacion, y si la cadena es vacia.
     * @param toValid String a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isText(String toValid) {
        Pattern patron = Pattern.compile("^\\s|\\d|\\p{Punct}|\\s$|^$");
        Matcher m = patron.matcher(toValid);

        if (m.find()) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida numeros. <br>
     * Verifica si hay espacios en blanco al principio y al final, si hay cualquier
     * otro caracter menos numeros, otros signos de puntuacion, y si la cadena es vacia.
     * @param toValid String a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isNumeric(String toValid) {
        Pattern patron = Pattern.compile("^\\s|\\D|\\p{Punct}|\\s$|^$");
        Matcher m = patron.matcher(toValid);

        if (m.find()) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida numeros double. <br>
     * Verifica si el numero pasado como parametro tiene la estructura de un
     * decimal.
     * @param toValid String a validar
     * @return true si es un decimal. <br>
     * false, en caso contrario.
     */
    public static boolean isDoubleFormat(String toValid) {
        Pattern patron = Pattern.compile("(^(?:\\+|-)?\\d+\\.\\d*$)");
        Matcher m = patron.matcher(toValid);

        if (m.find()) {
            return true;
        }

        return false;
    }

    /**
     * Este metodo valida numeros en general. <br>
     * Verifica si el numero pasado es positivo.
     * @param toValid long a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isPositive(long toValid) {
        if (toValid < 0) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida numeros en general. <br>
     * Verifica si el numero pasado es positivo.
     * @param toValid int a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isPositive(int toValid) {
        if (toValid < 0) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida numeros en general. <br>
     * Verifica si el numero pasado es positivo.
     * @param toValid double a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isPositive(double toValid) {
        if (toValid < 0) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida numeros en general. <br>
     * Verifica si el numero pasado es positivo.
     * @param toValid float a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isPositive(float toValid) {
        if (toValid < 0) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida la extencion de una cadena de caracteres. <br>
     * Verifica si una cadena dada tiene una longitud que se encuentre entre min
     * y max.
     * @param toValid String a validar
     * @param min el tamanio o longitud minima que debe cumplir la cadena.
     * @param max el tamanio o longitud maxima que debe cumplir la cadena.
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isInBounds(String toValid, int min, int max) {

        if (toValid.length() < min || toValid.length() > max) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida si la fecha tiene un formato del tipo dd/mm/yyyy. <br>
     * Verifica si la cadena (que representaria una fecha) cumple con dicho formato.
     * @param toValid String con la fecha a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isDateFormat(String toValid) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date valid = null;

        try {
            valid = sdf.parse(toValid);
        } catch (ParseException ex) {
            return false;
        }

        if (!sdf.format(valid).equals(toValid)) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida si una fecha es valida. <br>
     * Valida si son los dias son correctos, los mese son correctos, anios.
     * @param date con la fecha a validar
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     * @throws ParseException si la fecha dada no se puede convertir a Date.
     */
    public static boolean isCorrectDate(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date fecha = sdf.parse(date);
        Date hoy = new Date();
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(hoy);

        if (fecha.after(calendario.getTime())) {
            return false;
        }

        return true;
    }

    /**
     * Este metodo valida si el horario / hora tiene un formato correcto. <br>
     * El formato seguido para la hora, es el horario militar es decir 00:00 hasta
     * 23:59.
     * @param toValid String con la hora a validar.
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isTime(String toValid) {
        Pattern patron = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
        Matcher m = patron.matcher(toValid);

        return m.matches();
    }

    /**
     * Este metodo tiene como objetivo validar la seguridad de un contrasenia. <br>
     * Para que una contrasenia sea correcta se deben cumplir las siguientes
     * condiciones: <br>
     * 1) Debe contener al menos una letra mayuscula. <br>
     * 2) Debe contener al menos una letra minuscula. <br>
     * 3) Debe contener al menos un numero o caracter especial. <br>
     * 4) Debe tener como longitud minima 8 caracteres. <br>
     * 5) No debe contener espacios al principio, en medio y/o al final. <br>
     * 6) No debe ser una cadena vacia.
     * @param toValid String con la contrasenia a validar.
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isPassword(String toValid) {
        Pattern patron = Pattern.compile("(?=^.{8,}$)((?=.*d)|(?=.*W+))(?![.n])(?=.*[A-Z])(?=.*[a-z]).*$");
        Pattern patron2 = Pattern.compile("^\\s|\\s|\\s$|^$");
        Matcher m = patron.matcher(toValid);
        Matcher m2 = patron2.matcher(toValid);

        if (m2.find()) {
            return false;
        }

        return m.matches();
    }

    /**
     * Este metodo tiene como objetivo validar el nombre de usuario. <br>
     * Para que el nombre de usuario sea correcto se deben cumplir las siguientes
     * condiciones: <br>
     * 1) Debe empezar con letra minuscula o mayuscula. <br>
     * 2) Solo puede contener letras, numeros o guiones bajo. <br>
     * 3) La longitud del nombre de ususario es entre 8 y 24 caracteres. <br>
     * 4) El nombre de usuario no puede empezar ni terminar con guion bajo. <br>
     * @param toValid String con el nombre de usuario a validar.
     * @return true si esta todo bien. <br>
     * false, en caso contrario.
     */
    public static boolean isUserName(String toValid) {
        Pattern patron = Pattern.compile("^[a-z][\\da-z_]{6,22}[a-z\\d]$");
        Pattern patron2 = Pattern.compile("^\\s|\\s|\\s$|^$");
        Matcher m = patron.matcher(toValid);
        Matcher m2 = patron2.matcher(toValid);

        if (m2.find()) {
            return false;
        }

        return m.matches();
    }
    
}
