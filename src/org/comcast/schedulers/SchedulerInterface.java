package org.comcast.schedulers;

import org.quartz.SchedulerException;

/**
 * Interfaz que define las operaciones basicas para que se puedan plenificar las
 * tareas.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 1.0
 */
public interface SchedulerInterface {

    /**
     * Se encarga de configurar y empezar la tarea planificada.
     *
     * @throws SchedulerException Si hay error en la planificacion de la tarea.
     */
    public void startJob() throws SchedulerException;

    /**
     * Se encarga de detener la tarea que se encuentra en ejecucion.
     *
     * @throws SchedulerException Si hay un error en la detencion de la tarea
     */
    public void stopJob() throws SchedulerException;
}
