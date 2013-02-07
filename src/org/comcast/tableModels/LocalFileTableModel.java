package org.comcast.tableModels;

import java.util.ResourceBundle;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.commons.io.FileUtils;
import org.comcast.builder.Client;
import org.comcast.router.Message;
import org.comcast.xml.LoaderProvider;

/**
 * Clase que tiene como objetivo el modelado de los datos en una tabla.<br>
 * Representa a listados de los archivos locales.
 *
 * @author Federico Bruera.
 * @version 1.0
 * @since 1.6
 */
public class LocalFileTableModel implements TableModel {

    private ResourceBundle localFileTM_es_ES;
    Object[][] datos;
    String[] columnas;
    Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
    };
    boolean[] canEdit = new boolean[]{
        false, false, false, false, false
    };

    public LocalFileTableModel(Message[] sample) throws Exception {
        locale();
        column();
        datos = new Object[sample.length][5];
        reload(sample);
    }

    public LocalFileTableModel(Message[] sample, int rows) throws Exception {
        locale();
        column();
        datos = new Object[rows][5];
        reload(sample);
    }

    private void column() {
        this.columnas = new String[4];
        this.columnas[0] = localFileTM_es_ES.getString("NOMBRE");
        this.columnas[1] = localFileTM_es_ES.getString("TAMAÑO");
        this.columnas[2] = localFileTM_es_ES.getString("PATH");
        this.columnas[3] = localFileTM_es_ES.getString("TIPO DE ARCHIVO");
    }

    private void locale() throws Exception {
        Client c = LoaderProvider.getInstance().getClientConfiguration();

        switch (c.getLocalization()) {
            case "Español":
                this.localFileTM_es_ES = ResourceBundle.getBundle("org/comcast/locale/LocalFileTM_es_ES");
                break;
            case "Ingles":
                this.localFileTM_es_ES = ResourceBundle.getBundle("org/comcast/locale/LocalFileTM_en_US");
                break;
            default:
                this.localFileTM_es_ES = ResourceBundle.getBundle("org/comcast/locale/LocalFileTM_en_US");
                break;
        }
    }

    private void reload(Message[] sample) throws Exception {
        int i = 0;
        for (Message aux : sample) {

            for (int j = 0; j < 5; j++) {
                switch (j) {
                    case 0:
                        setValueAt(aux.getLocalFile().getName(), i, j);
                        break;

                    case 1:
                        setValueAt(FileUtils.byteCountToDisplaySize(aux.getLocalFile().length()), i, j);
                        break;

                    case 2:
                        setValueAt(aux.getLocalFile().getAbsolutePath(), i, j);
                        break;

                    case 3:
                        setValueAt(aux.getFileType(), i, j);
                        break;

                    case 4:
                        setValueAt(aux, i, j);
                        break;
                }
            }
            i++;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        datos[row][col] = value;
    }

    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }

    public int getRowCount() {
        return datos.length;
    }

    public int getColumnCount() {
        return columnas.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return datos[rowIndex][columnIndex];
    }

    public String getColumnName(int columnIndex) {
        return columnas[columnIndex];
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }
}
