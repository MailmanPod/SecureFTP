package org.comcast.tableModels;

import java.util.ResourceBundle;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.commons.io.FileUtils;
import org.comcast.builder.Client;
import org.comcast.router.Message;
import org.comcast.xml.LoaderProvider;

/**
 * Clase que tiene como objetivo el modeloado de los datos en una tabla.
 *
 * @author Federico Bruera TSB 2010.
 * @version 1.0
 * @since 1.6
 */
public class LocalWizardTableModel implements TableModel {
    private ResourceBundle localWizardTM_es_ES;
    
    Object[][] datos;
    
    String[] columnas;
    
    Class[] types = new Class[]{
        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
    };
    
    boolean[] canEdit = new boolean[]{
        false, false, false, false, false, false
    };

    public LocalWizardTableModel(Message[] sample) throws Exception {
        locale();
        column();
        datos = new Object[sample.length][6];
        reload(sample);
    }

    public LocalWizardTableModel(Message[] sample, int rows) throws Exception {
        locale();
        column();
        datos = new Object[rows][6];
        reload(sample);
    }
    
    private void column(){
        this.columnas = new String[5];
        this.columnas[0] = "#";
        this.columnas[1] = localWizardTM_es_ES.getString("NOMBRE");
        this.columnas[2] = localWizardTM_es_ES.getString("TAMAÑO");
        this.columnas[3] = localWizardTM_es_ES.getString("PRIORIDAD");
        this.columnas[4] = localWizardTM_es_ES.getString("TIPO DE ARCHIVO");
    }
    
    private void locale() throws Exception{
        Client c = LoaderProvider.getInstance().getClientConfiguration();
        
        switch(c.getLocalization()){
            case "Español":
                this.localWizardTM_es_ES  = ResourceBundle.getBundle("org/comcast/locale/LocalWizardTM_es_ES");
                break;
            case "Ingles":
                this.localWizardTM_es_ES  = ResourceBundle.getBundle("org/comcast/locale/LocalWizardTM_en_US");
                break;
            default:
                this.localWizardTM_es_ES  = ResourceBundle.getBundle("org/comcast/locale/LocalWizardTM_en_US");
                break;
        }
    }

    private void reload(Message[] sample) throws Exception {
        int i = 0;
        for (Message aux : sample) {

            for (int j = 0; j < 6; j++) {
                switch (j) {
                    case 0:
                        setValueAt(i + 1, i, j);
                        break;
                        
                    case 1:
                        setValueAt(aux.getLocalFile().getName(), i, j);
                        break;

                    case 2:
                        setValueAt(FileUtils.byteCountToDisplaySize(aux.getLocalFile().length()), i, j);
                        break;

                    case 3:
                        setValueAt(aux.getPriorityString(), i, j);
                        break;

                    case 4:
                        setValueAt(aux.getFileType(), i, j);
                        break;

                    case 5:
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
