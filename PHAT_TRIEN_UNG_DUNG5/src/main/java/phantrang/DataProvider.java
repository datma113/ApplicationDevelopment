package phantrang;

public interface DataProvider<T> {
	int getTotalRowCount();

	void addDataToTable(int startIndex, int endIndex);
}