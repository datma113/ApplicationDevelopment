package phantrang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import custom.MyJButtonPaging;
import custom.MyJLabel;

public class Decorator<T> {
	private DataProvider<T> dataProvider;
	private int[] pageSizes;
	private JPanel contentPanel;
	private int currentPageSize;
	private int currentPage = 1;
	private JPanel pageLinkPanel;
	private static final int MaxPagingCompToShow = 9;
	private static final String Ellipses = "...";
	private MyJLabel labelSum = new MyJLabel();

	private Decorator(DataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize) {
		this.dataProvider = dataProvider;
		this.pageSizes = pageSizes;
		this.currentPageSize = defaultPageSize;
		pageLinkPanel = new JPanel(new GridLayout(1, MaxPagingCompToShow, 5, 3));
	}

	public static <T> Decorator<T> decorate(DataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize) {
		Decorator<T> decorator = new Decorator<>(dataProvider, pageSizes, defaultPageSize);
		decorator.init();
		return decorator;
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public void reload() {
		paginate();
	}

	private void init() {
		initPaginationComponents();
	}

	private void initPaginationComponents() {
		contentPanel = new JPanel(new BorderLayout());
		JPanel paginationPanel = createPaginationPanel();
		contentPanel.add(paginationPanel, BorderLayout.CENTER);
		JPanel panelComboBox = createPanelComboBox();
		contentPanel.add(panelComboBox, BorderLayout.EAST);
		labelSum.setPreferredSize(panelComboBox.getPreferredSize());
		contentPanel.add(labelSum, BorderLayout.WEST);
	}

	private JPanel createPaginationPanel() {
		JPanel paginationPanel = new JPanel();
		paginationPanel.add(pageLinkPanel);
		return paginationPanel;
	}

	private JPanel createPanelComboBox() {
		JPanel paginationPanel = new JPanel();
		if (pageSizes != null) {
			JComboBox<Integer> pageComboBox = new JComboBox<>(Arrays.stream(pageSizes).boxed().toArray(Integer[]::new));
			pageComboBox.addActionListener((ActionEvent e) -> {
				// to preserve current rows position
				int currentPageStartRow = ((currentPage - 1) * currentPageSize) + 1;
				currentPageSize = (Integer) pageComboBox.getSelectedItem();
				currentPage = ((currentPageStartRow - 1) / currentPageSize) + 1;
				paginate();
			});
			paginationPanel.add(new MyJLabel("Kích thước trang: "));
			paginationPanel.add(pageComboBox);
			pageComboBox.setSelectedItem(currentPageSize);
		}
		return paginationPanel;
	}

	private void refreshPageButtonPanel() {
		pageLinkPanel.removeAll();
		int totalRows = dataProvider.getTotalRowCount();
		int pages = (int) Math.ceil((double) totalRows / currentPageSize);
		ButtonGroup buttonGroup = new ButtonGroup();
		if (pages > MaxPagingCompToShow) {
			addPageButton(pageLinkPanel, buttonGroup, 1);
			if (currentPage > (pages - ((MaxPagingCompToShow + 1) / 2))) {
				// case: 1 ... n->lastPage
				pageLinkPanel.add(createEllipsesComponent());
				addPageButtonRange(pageLinkPanel, buttonGroup, pages - MaxPagingCompToShow + 3, pages);
			} else if (currentPage <= (MaxPagingCompToShow + 1) / 2) {
				// case: 1->n ...lastPage
				addPageButtonRange(pageLinkPanel, buttonGroup, 2, MaxPagingCompToShow - 2);
				pageLinkPanel.add(createEllipsesComponent());
				addPageButton(pageLinkPanel, buttonGroup, pages);
			} else {// case: 1 .. x->n .. lastPage
				pageLinkPanel.add(createEllipsesComponent());// first ellipses
				// currentPage is approx mid point among total max-4 center links
				int start = currentPage - (MaxPagingCompToShow - 4) / 2;
				int end = start + MaxPagingCompToShow - 5;
				addPageButtonRange(pageLinkPanel, buttonGroup, start, end);
				pageLinkPanel.add(createEllipsesComponent());// last ellipsis
				addPageButton(pageLinkPanel, buttonGroup, pages);// last page link
			}
		} else {
			addPageButtonRange(pageLinkPanel, buttonGroup, 1, pages);
		}
		pageLinkPanel.getParent().validate();
		pageLinkPanel.getParent().repaint();
	}

	private Component createEllipsesComponent() {
		return new JLabel(Ellipses, SwingConstants.CENTER);
	}

	private void addPageButtonRange(JPanel parentPanel, ButtonGroup buttonGroup, int start, int end) {
		for (; start <= end; start++) {
			addPageButton(parentPanel, buttonGroup, start);
		}
	}

	private void addPageButton(JPanel parentPanel, ButtonGroup buttonGroup, int pageNumber) {
		MyJButtonPaging toggleButton = new MyJButtonPaging(Integer.toString(pageNumber));
		toggleButton.setPreferredSize(new Dimension(35, 35));
		toggleButton.setToolTipText("Trang " + pageNumber);
		toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		toggleButton.setMargin(new Insets(0, -4, 0, -4));
		toggleButton.setFocusable(false);
		buttonGroup.add(toggleButton);
		parentPanel.add(toggleButton);
		if (pageNumber == currentPage) {
			toggleButton.setForeground(Color.WHITE);
		}
		toggleButton.addActionListener(ae -> {
			currentPage = Integer.parseInt(ae.getActionCommand());
			paginate();
		});
	}

	private void paginate() {
		refreshPageButtonPanel();
		int size = dataProvider.getTotalRowCount();
		int startIndex = (currentPage - 1) * currentPageSize;
		int endIndex = currentPageSize;
		if (endIndex > size) {
			endIndex = size;
		}
		dataProvider.addDataToTable(startIndex, endIndex);
		labelSum.setText("Tổng: " + size);
	}
}