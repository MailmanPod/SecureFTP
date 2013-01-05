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
public class RemoteWizardTableModel implements TableModel {
    private ResourceBundle remoteWizardTM_es_ES;
    
    Object[][] datos;
    
    final String[] columnas = {
        "#", remoteWizardTM_es_ES.getString("NOMBRE"), remoteWizardTM_es_ES.getString("TAMAÑO"), remoteWizardTM_es_ES.getString("PRIORIDAD"), remoteWizardTM_es_ES.getString("TIPO DE ARCHIVO")
    };
    
    Class[] types = new Class[]{
        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
    };
    
    boolean[] canEdit = new boolean[]{
        false, false, false, false, false, false
    };

    public RemoteWizardTableModel(Message[] sample) throws Exception {
        datos = new Object[sample.length][6];
        reload(sample);
    }

    public RemoteWizardTableModel(Message[] sample, int rows) throws Exception {
        datos = new Object[rows][6];
        reload(sample);
    }

    private void locale() throws Exception{
        Client c = LoaderProvider.getInstance().getClientConfiguration();
        
        switch(c.getLocalization()){
            case "Español":
                this.remoteWizardTM_es_ES = ResourceBundle.getBundle("org/comcast/locale/RemoteWizardTM_es_ES");
                break;
            case "Ingles":
                this.remoteWizardTM_es_ES = ResourceBundle.getBundle("org/comcast/locale/RemoteWizardTM_en_US");
                break;
            default:
                this.remoteWizardTM_es_ES = ResourceBundle.getBundle("org/comcast/locale/RemoteWizardTM_en_US");
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
                        setValueAt(aux.getFtpFile().getName(), i, j);
                        break;

                    case 2:
                        setValueAt(FileUtils.byteCountToDisplaySize(aux.getFtpFile().getSize()), i, j);
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
