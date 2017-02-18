import javax.swing.table.AbstractTableModel;

public class memoryTable extends AbstractTableModel {

	private String[] columnNames = { "Žodis", "1b", "2b", "3b", "4b" };;
	private String[][] data;

	public memoryTable(String[][] data) {
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