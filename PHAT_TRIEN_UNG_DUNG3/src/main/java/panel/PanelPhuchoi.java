package panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.toedter.calendar.JDateChooser;

import custom.MyJButton;
import custom.MyJScrollPane;

public class PanelPhuchoi extends JPanel {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private JTextArea textMess;
	private String dictionary = "";
	private MyJButton btnRestore;
	private JDateChooser dateRestore;
	private JFileChooser fileChooser;
	private JProgressBar progressBar;
	private static final String DATABASE_NAME = "Nhom14_QLMuabanLinhkienMayvitinh";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	public PanelPhuchoi(boolean droped) {
		setSize(600, 300);
		setLayout(null);

		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Vui lòng chọn thư mục chứa File đã sao lưu");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		textMess = new JTextArea("Vui lòng chọn ngày cần phục hồi lại dữ liệu.");
		textMess.append("\nSau đó chọn thư mục có chứa File đã sao lưu trước đó.");
		textMess.append("\n=====================");
		textMess.setFont(new Font("SansSerif", Font.PLAIN, 18));
		textMess.setBounds(10, 11, 562, 192);
		textMess.setWrapStyleWord(true);
		textMess.setLineWrap(true);
		textMess.setEditable(false);

		MyJScrollPane jscrollPane = new MyJScrollPane(textMess, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jscrollPane.setLocation(10, 11);
		jscrollPane.setSize(562, 180);
		jscrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		add(jscrollPane);

		dateRestore = new JDateChooser(new Date());
		dateRestore.setBounds(10, 214, 384, 30);
		add(dateRestore);

		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(6, 176, 37));
		progressBar.setBorderPainted(false);
		progressBar.setBounds(10, 198, 562, 10);
		add(progressBar);

		btnRestore = new MyJButton("Phục Hồi");
		btnRestore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRestore.setIcon(new ImageIcon("img\\restore.png"));
		btnRestore.addActionListener(e -> {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				java.io.File file = fileChooser.getSelectedFile();
				dictionary = file.getPath();
				PhuchoiTask task = new PhuchoiTask(this, progressBar, textMess, droped);
				task.execute();
			}
		});
		btnRestore.setBounds(411, 214, 161, 35);
		add(btnRestore);
	}

	protected boolean phucHoi(boolean droped) {
		Date chooseDay = dateRestore.getDate();
		if (chooseDay.getTime() < (new Date().getTime())) {
			Calendar chooseCalendar = Calendar.getInstance();
			chooseCalendar.setFirstDayOfWeek(Calendar.MONDAY);
			chooseCalendar.setTime(chooseDay);
			int thu = chooseCalendar.get(Calendar.DAY_OF_WEEK);
			int week = chooseCalendar.get(Calendar.WEEK_OF_YEAR);
			switch (thu) {
			case Calendar.MONDAY:
			case Calendar.TUESDAY:
			case Calendar.WEDNESDAY:
			case Calendar.THURSDAY:
				week -= 1;
				break;
			default:
				break;
			}
			String path = "'" + dictionary + "\\week" + week + ".bak'";
			String restoreHead = "";
			String restoreDatbase = "";
			String query = "";
			if (new File(dictionary + "\\week" + week + ".bak").exists()) {
				textMess.append("\nĐã tìm thấy file sao lưu: " + dictionary + "\\week" + week + ".bak");
				textMess.append("\nĐang phục hồi lại dữ liệu. Vui lòng đợi...");
				restoreHead = "'RESTORE HEADERONLY  FROM DISK='" + path + "''";
				restoreDatbase = "RESTORE DATABASE " + DATABASE_NAME + " FROM DISK=" + path;
			} else {
				textMess.append("\nPhục hồi không thành công.");
				textMess.append("\nKhông tìm thấy file sao lưu của ngày này");
				textMess.append("\nVui lòng chọn ngày khác.");
				textMess.append("\n=====================");
				progressBar.setIndeterminate(false);
				progressBar.setValue(100);
				progressBar.setForeground(Color.RED);
				JOptionPane.showMessageDialog(this, "Không tìm thấy file sao lưu của ngày này. Vui lòng chọn ngày khác",
						"Phục hồi dữ liệu", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			ResultSet rs;
			try {
				rs = getResultSet(restoreHead, droped);
				int maxfull = 1;
				int maxdiff = 0;
				int maxlog = 0;
				boolean flag = true;

				while (rs.next()) {
					if (rs.getInt(2) == 1 && rs.getInt(3) > maxfull) {
						maxfull = rs.getInt(3);
					}
					if (rs.getInt(2) == 5) {
						flag = true;
						if (rs.getInt(3) > maxdiff)
							maxdiff = rs.getInt(3);
					}
					if (rs.getInt(2) == 2) {
						flag = false;
						if (rs.getInt(3) > maxlog)
							maxlog = rs.getInt(3);
					}
				}
				if (flag) {
					query += restoreDatbase + " WITH FILE=" + maxfull;
					if (maxdiff != 0)
						query += " , NORECOVERY \n" + restoreDatbase + " WITH FILE=" + maxdiff;
				} else {
					query += restoreDatbase + " WITH FILE=" + maxfull;
					if (maxdiff != 0)
						query += " , NORECOVERY \n" + restoreDatbase + " WITH FILE=" + maxdiff;
					if (maxlog != 0)
						query += " , NORECOVERY \n" + "RESTORE LOG " + DATABASE_NAME + " FROM DISK=" + path
								+ " WITH FILE=" + maxlog;
				}
				query += ", RECOVERY";
				PreparedStatement ps = con.prepareStatement(query);
				ps.execute();
				return true;
			} catch (SQLException e) {
				return false;
			}
		} else {
			textMess.append("\nPhục hồi không thành công.");
			textMess.append("\nVui lòng chọn ngày nhỏ hơn ngày hiện tại");
			textMess.append("\n=====================");
			progressBar.setIndeterminate(false);
			progressBar.setValue(100);
			progressBar.setForeground(Color.RED);
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày nhỏ hơn ngày hiện tại");
			return false;
		}
	}

	/**
	 * lấy dữ liệu bằng câu lệnh restore head only
	 * 
	 * @param restoreHead
	 * @return
	 * @throws SQLException
	 */
	private ResultSet getResultSet(String restoreHead, boolean droped) throws SQLException {
		String dbURL = "jdbc:sqlserver://localhost;databaseName=master;user=sa;password=sapassword";
		con = DriverManager.getConnection(dbURL);
		String sql = "DECLARE @a TABLE ( BackupName nvarchar(128), BackupDescription nvarchar(255), "
				+ "BackupType smallint, ExpirationDate datetime, Compressed bit, Position smallint, "
				+ "DeviceType tinyint, UserName nvarchar(128), ServerName nvarchar(128), "
				+ "DatabaseName nvarchar(128), DatabaseVersion int, DatabaseCreationDate datetime, "
				+ "BackupSize numeric(20, 0), FirstLSN numeric(25, 0), LastLSN numeric(25, 0), "
				+ "CheckpointLSN numeric(25, 0), DatabaseBackupLSN numeric(25, 0), "
				+ "BackupStartDate datetime, BackupFinishDate datetime, SortOrder smallint, "
				+ "[CodePage] smallint, UnicodeLocaleId int, UnicodeComparisonStyle int, "
				+ "CompatibilityLevel tinyint, SoftwareVendorId int, SoftwareVersionMajor int, "
				+ "SoftwareVersionMinor int, SoftwareVersionBuild int, MachineName nvarchar(128), "
				+ "Flags int, BindingId uniqueidentifier, RecoveryForkId uniqueidentifier, "
				+ "Collation nvarchar(128), FamilyGUID uniqueidentifier, HasBulkLoggedData bit, "
				+ "IsSnapshot bit, IsReadOnly bit, IsSingleUser bit, HasBackupChecksums bit, "
				+ "IsDamaged bit, BeginsLogChain bit, HasIncompleteMetaData bit, IsForceOffline bit, "
				+ "IsCopyOnly bit, FirstRecoveryForkID uniqueidentifier, ForkPointLSN numeric(25, 0), "
				+ "RecoveryModel nvarchar(60), DifferentialBaseLSN numeric(25, 0), "
				+ "DifferentialBaseGUID uniqueidentifier, BackupTypeDescription nvarchar(60), "
				+ "BackupSetGUID uniqueidentifier, CompressedBackupSize bigint, Containment TINYINT ) "
				+ "SET DATEFORMAT DMY INSERT INTO @a exec(" + restoreHead + ") "
				+ "SELECT BackupDescription, BackupType, Position, BackupStartDate FROM @a WHERE CAST(BackupDescription AS DATE) <= '"
				+ sdf.format(dateRestore.getDate()) + "' ORDER BY BackupDescription";
		if (!droped) {
			PreparedStatement ps1 = con.prepareStatement(
					"exec('DECLARE @kill varchar(8000) = '''';  SELECT @kill = @kill + ''kill '' + CONVERT(varchar(5), session_id) + '';''  FROM sys.dm_exec_sessions WHERE database_id  = db_id(''"
							+ DATABASE_NAME + "'') EXEC(@kill); DROP DATABASE " + DATABASE_NAME + ";')");
			ps1.executeUpdate();
		}
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return rs;
	}

}

class PhuchoiTask extends SwingWorker<Void, Integer> {

	private PanelPhuchoi panel;
	private JProgressBar progressBar;
	private JTextArea textMess;
	private boolean result = false;
	private boolean droped = false;

	public PhuchoiTask(PanelPhuchoi panel, JProgressBar progressBar, JTextArea textMess, boolean droped) {
		super();
		this.panel = panel;
		this.progressBar = progressBar;
		this.textMess = textMess;
		this.droped = droped;
	}

	@Override
	protected Void doInBackground() throws Exception {
		progressBar.setForeground(new Color(6, 176, 37));
		progressBar.setIndeterminate(true);
		result = panel.phucHoi(droped);
		return null;
	}

	@Override
	protected void done() {
		progressBar.setIndeterminate(false);
		progressBar.setValue(100);
		if (result) {
			textMess.append("\nPhục hồi thành công.");
			textMess.append("\n=====================");
			progressBar.setForeground(new Color(6, 176, 37));
			JOptionPane.showMessageDialog(panel,
					"Phục hồi thành công. Vui lòng đóng tất cả chương trình và khởi động lại.", "Phục hồi dữ liệu",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			textMess.append("\nPhục hồi không thành công.");
			textMess.append("\n=====================");
			progressBar.setForeground(Color.RED);
			JOptionPane.showMessageDialog(panel, "Phục hồi không thành công.", "Phục hồi dữ liệu",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}