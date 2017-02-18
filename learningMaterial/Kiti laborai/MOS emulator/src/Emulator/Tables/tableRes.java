package Emulator.Tables;

import javax.swing.table.AbstractTableModel;

public class tableRes extends AbstractTableModel {

	private String[] columnNames = { "ID", "Išorinis vardas", "Būsena", "Kas sukūrė?", "Kas naudoja?"};
	private String[][] data;

	public tableRes(String[][] data) {
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