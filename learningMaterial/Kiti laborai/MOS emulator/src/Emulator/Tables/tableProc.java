package Emulator.Tables;

import javax.swing.table.AbstractTableModel;

public class tableProc extends AbstractTableModel {

	private String[] columnNames = { "ID", "Išorinis vardas", "Būsena" };
	private String[][] data;

	public tableProc(String[][] data) {
		setData(data);
	}

	public void setData(String[][] data) {
		this.data = data;
		fireTableDataChanged();
	}

	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}
}