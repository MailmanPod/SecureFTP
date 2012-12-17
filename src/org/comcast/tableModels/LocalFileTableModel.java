package org.comcast.tableModels;

import java.io.IOException;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.commons.io.FileUtils;
import org.comcast.router.Message;

/**
 * Clase que tiene como objetivo el modeloado de los datos en una tabla.
 * @author Federico Bruera TSB 2010.
 * @version 1.0
 * @since 1.6
 */
public class LocalFileTableModel implements TableModel {

    Object[][] datos;
    final String[] columnas = {
        "Nombre", "Tamaño", "Path", "Tipo de Archivo"
    };
    Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
    };
    boolean[] canEdit = new boolean[]{
        false, false, false, false
    };

    public LocalFileTableModel(Message[] sample) throws IOException {
        datos = new Object[sample.length][4];
        reload(sample);
    }

    public LocalFileTableModel(Message[] sample, int rows) throws IOException {
        datos = new Object[rows][4];
        reload(sample);
    }
    
    private void reload(Message[] sample) throws IOException {
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
