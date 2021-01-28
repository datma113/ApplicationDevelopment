package phantrang;

public interface DataProvider<T> {

	/**
	 * lấy tổng số dòng dữ liệu để chia trang
	 * 
	 * @return
	 */
	int getTotalRowCount();

	/**
	 * lấy dữ liệu từ dòng startIndex đến dòng endIndex trong database để đưa vào
	 * model của bảng
	 * 
	 * @param startIndex
	 * @param endIndex
	 */
	void addDataToTable(int startIndex, int endIndex);

}