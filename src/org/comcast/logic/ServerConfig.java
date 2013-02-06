package org.comcast.logic;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Clase que representa a todos los parametros de coneccion que se utilizan para
 * la coneccion hacia el servidor ftp.
 *
 * @author Damian Bruera.
 * @since Java 7
 * @version 2.1.
 */
public class ServerConfig {

    private String ipAddress;
    private String hostName;
    private String userLogin;
    private String passLogin;

    /**
     * Constructor de la clase.
     *
     * @param hostName Con el nombre de la maquina (servidor) en la red.<br>
     * Ademas del nombre puede ser tambien que se inserte directamente la
     * direccion ip del servidor.
     */
    public ServerConfig(String hostName) {
        this.hostName = hostName;
        this.ipAddress = getIPAddress();
    }

    /**
     * Este metodo retorna la direccion ip de la maquina (servidor) en la red.
     * <br> En caso de error se procede a la coneccion en el equipo local.
     *
     * @return String Con la direccion IP.
     */
    private String getIPAddress() {
        try {
            InetAddress localhost = InetAddress.getByName(hostName);
            return localhost.getHostAddress();
        } catch (UnknownHostException ex) {
            return "localhost";
        }
    }

    /**
     * Metodo toString personalizado, para mostrar los datos de configuracion.
     *
     * @return String con la cadena con todos los datos.
     * @overrides toString
     */
    @Override
    public String toString() {
        return "ServerConfig{" + "ipAddress=" + getIpAddress() + ", hostName=" + getHostName() + ", userLogin=" + getUserLogin() + '}';
    }

    /**
     * Retorna la direccion ip.
     *
     * @return String con la IP.
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Modifica la direccion ip.
     *
     * @param ipAddress Con la nueva direccion ip.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Retorna el nombre del host. Puede ser el nombre o la ip.
     *
     * @return String con el nombre o ip.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Modifica el nombre del host. Puede ser el nombre o la ip.
     *
     * @param hostName Con el nuevo nombre o ip.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Retorna el nombre de usuario registrado en el servidor.
     *
     * @return String con el nombre de usuario.
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * Modifica el nombre de usuario registrado en el servidor.
     *
     * @param userLogin Con el nuevo nombre de usuario.
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * Retorna la contraseña asociada al nombre de usuario.
     *
     * @return String con la contraseña.
     */
    public String getPassLogin() {
        return passLogin;
    }

    /**
     * Modifica la contraseña asociada al nombre de usuario.
     *
     * @param passLogin Con la nueva contraseña.
     */
    public void setPassLogin(String passLogin) {
        this.passLogin = passLogin;
    }
}
