package org.comcast.tableModels;

/**
 * Esta clase muestra la forma de crear filtros para nombres de archivos para un JFileChooser. 
 * Filtra todos los tipos de archivos, excepto aquellos cuyas extensiones conoce. Las extensiones
 * son de la forma ".xxx" (para cualquier valor que asuma "xxx"). Se ignora la diferencia entre 
 * minusculas y mayusculas.
 *
 * Ejemplo: El siguiente segmento crea un nuevo filtro que filtra todos los archivos excepto los
 * que lleven extension gif y jpg:
 *
 *     JFileChooser chooser = new JFileChooser();
 *     SimpleFileFilter filter = new SimpleFileFilter(new String{"gif", "jpg"}, "JPEG & GIF Images");
 *     chooser.addChoosableFileFilter(filter);
 *     chooser.showOpenDialog(this);
 *
 * @author Jeff Dinkins - Sun Microsystems, Inc. All Rights Reserved. Copyright (c) 2004.
 * @version 1.16 04/07/26
 */


import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.filechooser.*;

public class SimpleFileFilter extends FileFilter {

    private static String TYPE_UNKNOWN = "Tipo desconocido";
    private static String HIDDEN_FILE = "Archivo oculto";

    private Hashtable filters = null;
    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    /**
     * Crea un filtro. Si no se agregan filtros, entonces todos los archivos son aceptados.
     * @see #addExtension
     */
    public SimpleFileFilter() {
	this.filters = new Hashtable();
    }

    /**
     * Crea un filtro que acepta archivos con la extensi�n dada.
     * Ejemplo: new SimpleFileFilter("jpg");
     * @see #addExtension
     */
    public SimpleFileFilter(String extension) {
	this(extension,null);
    }

    /**
     * Crea un filtro que acepta el tipo de archivo dado.
     * Ejemplo: new SimpleFileFilter("jpg", "JPEG Image Images");
     *
     * Note que el "." antes de la extensi�n no es necesario. Si se coloca, ser� ignorado. 
     * @see #addExtension
     */
    public SimpleFileFilter(String extension, String description) {
	this();
	if(extension!=null) addExtension(extension);
 	if(description!=null) setDescription(description);
    }

    /**
     * Crea un filtro a partir de un arreglo de cadenas.
     * Ejemplo: new SimpleFileFilter(String {"gif", "jpg"});
     *
     * Note que el "." antes de la extensi�n no es necesario. Si se coloca, ser� ignorado. 
     * @see #addExtension
     */
    public SimpleFileFilter(String[] filters) {
	this(filters, null);
    }

    /**
     * Crea un filtro a partir de un arreglo de cadenas y una descripci�n.
     * Ejemplo: new ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
     *
     * Note que el "." antes de la extensi�n no es necesario. Si se coloca, ser� ignorado. 
     * @see #addExtension
     */
    public SimpleFileFilter(String[] filters, String description) {
	this();
	for (int i = 0; i < filters.length; i++) {
	    // agrega los filtros uno por uno
	    addExtension(filters[i]);
	}
 	if(description!=null) setDescription(description);
    }

    /**
     * Retorna true si este archivo deberia mostrarse en el panel de directorios,
     * o false en caso contrario. Los archivos que comiencen con "." se ignoran.
     * @see #getExtension
     * @see FileFilter#accepts
     */
    public boolean accept(File f) {
	if(f != null) {
	    if(f.isDirectory()) {
		return true;
	    }
	    String extension = getExtension(f);
	    if(extension != null && filters.get(getExtension(f)) != null) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Retorna la extensi�n del nombre de un archivo. 
     * @see #getExtension
     * @see FileFilter#accept
     */
     public String getExtension(File f) {
	if(f != null) {
	    String filename = f.getName();
	    int i = filename.lastIndexOf('.');
	    if(i>0 && i<filename.length()-1) {
		return filename.substring(i+1).toLowerCase();
	    };
	}
	return null;
    }

    /**
     * Agrega un tipo de archivo al filtro.
     * Ejemplo: el siguiente c�digo crear� un filtro que fitlra todos los archivos excepto aquellos
     * con extensi�n ".jpg" y ".tif":
     *
     *   SimpleFileFilter filter = new SimpleFileFilter();
     *   filter.addExtension("jpg");
     *   filter.addExtension("tif");
     */
    private void addExtension(String extension) {
	if(filters == null) {
	    filters = new Hashtable(5);
	}
	filters.put(extension.toLowerCase(), this);
	fullDescription = null;
    }


    /**
     * Retorna una descripci�n gen�rica, apta para comprensi�n humana de este filtro.
     * Por ejemplo: "JPEG and GIF Image Files (*.jpg, *.gif)"
     *
     * @see setDescription
     * @see setExtensionListInDescription
     * @see isExtensionListInDescription
     * @see FileFilter#getDescription
     */
    public String getDescription() {
	if(fullDescription == null) {
	    if(description == null || isExtensionListInDescription()) {
 		fullDescription = description==null ? "(" : description + " (";
		// crea la descripci�n desde la lista de extensiones 
		Enumeration extensions = filters.keys();
		if(extensions != null) {
		    fullDescription += "." + (String) extensions.nextElement();
		    while (extensions.hasMoreElements()) {
			fullDescription += ", ." + (String) extensions.nextElement();
		    }
		}
		fullDescription += ")";
	    } else {
		fullDescription = description;
	    }
	}
	return fullDescription;
    }

    /**
     * Configura la descripci�n general. 
     * Ejemplo: filter.setDescription("Gif and JPG Images");
     *
     * @see setDescription
     * @see setExtensionListInDescription
     * @see isExtensionListInDescription
     */
    private void setDescription(String description) {
	this.description = description;
	fullDescription = null;
    }

    /**
     * Determina si la extensi�n deber�a aparecer en la descripci�n general.
     * S�lo es relevante si una descripci�n fue provista en el cosntructor o usando
     * setDescription();
     *
     * @see getDescription
     * @see setDescription
     * @see isExtensionListInDescription
     */
    public void setExtensionListInDescription(boolean b) {
	useExtensionsInDescription = b;
	fullDescription = null;
    }

    /**
     * Retorna un boolean que indica si la lista de extensiones est� en la descripci�n general.
     * S�lo es relevante si una descripci�n fue provista en el cosntructor o usando
     * setDescription();
     *
     * @see getDescription
     * @see setDescription
     * @see setExtensionListInDescription
     */
    public boolean isExtensionListInDescription() {
	return useExtensionsInDescription;
    }
}
