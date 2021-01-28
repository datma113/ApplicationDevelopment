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

import custom.MyJButton;
import custom.MyJScrollPane;

/**
 * @author kienc
 *
 */
public class PanelSaoluu extends JPanel {

	private static final long serialVersionUID = 1L;

	private Connection con;
	private JTextArea textMess;
	private MyJButton btnBackup;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private static final String DATABASE_NAME = "Nhom14_QLMuabanLinhkienMayvitinh";
	private String dictionary = "";
	private JFileChooser fileChooser;
	private JProgressBar progressBar;

	public PanelSaoluu() {
		setSize(600, 300);
		setLayout(null);

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		textMess = new JTextArea("Vui lòng bấm nút \"Sao lưu\" để chọn thư mục lưu file sao lưu");
		textMess.append("\n=====================");
		textMess.setFont(new Font("SansSerif", Font.PLAIN, 18));
		textMess.setBounds(10, 11, 562, 192);
		textMess.setEditable(false);

		MyJScrollPane jscrollPane = new MyJScrollPane(textMess, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jscrollPane.setLocation(10, 11);
		jscrollPane.setSize(562, 180);
		jscrollPane.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		add(jscrollPane);

		btnBackup = new MyJButton("Sao Lưu");
		btnBackup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBackup.setIcon(new ImageIcon("img/backup.png"));
		btnBackup.addActionListener(e -> {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				java.io.File file = fileChooser.getSelectedFile();
				dictionary = file.getPath();
				if (new File(dictionary).exists()) {
					SaoluuTask task = new SaoluuTask(this, progressBar, textMess);
					task.execute();
				} else {
					JOptionPane.showMessageDialog(this, "Thư mục không tồn tại", "Sao lưu", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBackup.setBounds(213, 214, 161, 35);
		add(btnBackup);

		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(6, 176, 37));
		progressBar.setBorderPainted(false);
		progressBar.setBounds(10, 198, 562, 10);
		add(progressBar);

	}

	protected boolean saoLuu() {
		Date chooseDay = new Date();
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
		String restoreHead = "'RESTORE HEADERONLY  FROM DISK='" + path + "''";
		String query = "";
		textMess.append("\nĐang sao lưu dữ liệu vào file: ");
		textMess.append(dictionary + "\\week" + week + ".bak");
		try {
			getResultSet(restoreHead);
		} catch (Exception e) {
			query = "ALTER DATABASE " + DATABASE_NAME + " SET RECOVERY FULL BACKUP DATABASE " + DATABASE_NAME
					+ " TO DISK = " + path + " WITH DESCRIPTION = '" + sdf.format(chooseDay) + "'";
			try {
				PreparedStatement ps = con.prepareStatement(query);
				ps.execute();
				return true;
			} catch (SQLException e1) {
				return false;
			}
		}
		switch (thu) {
		case Calendar.FRIDAY:
			query = "BACKUP DATABASE " + DATABASE_NAME + " TO DISK = " + path + " WITH DESCRIPTION = '"
					+ sdf.format(chooseDay) + "'";
			break;
		case Calendar.MONDAY:
		case Calendar.WEDNESDAY:
			query = "BACKUP DATABASE " + DATABASE_NAME + " TO DISK = " + path + " WITH DIFFERENTIAL, DESCRIPTION = '"
					+ sdf.format(chooseDay) + "'";
			break;
		default:
			query = "BACKUP LOG " + DATABASE_NAME + " TO DISK = " + path + " WITH DESCRIPTION = '"
					+ sdf.format(chooseDay) + "'";
			break;
		}
		query = "ALTER DATABASE " + DATABASE_NAME + " SET RECOVERY FULL " + query;
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.execute();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	private ResultSet getResultSet(String restoreHead) throws SQLException {
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
				+ sdf.format(new Date()) + "' ORDER BY BackupDescription";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		return rs;
	}

}

/**
 * @author kienc
 *
 */
class SaoluuTask extends SwingWorker<Void, Integer> {

	private PanelSaoluu frame;
	private JProgressBar progressBar;
	private JTextArea textMess;
	private boolean result = false;

	public SaoluuTask(PanelSaoluu frame, JProgressBar progressBar, JTextArea textMess) {
		super();
		this.frame = frame;
		this.progressBar = progressBar;
		this.textMess = textMess;
	}

	@Override
	protected Void doInBackground() throws Exception {
		progressBar.setIndeterminate(true);
		result = frame.saoLuu();
		return null;
	}

	@Override
	protected void done() {
		progressBar.setIndeterminate(false);
		progressBar.setValue(100);
		if (result) {
			textMess.append("\nSao lưu thành công");
			textMess.append("\n=====================");
			progressBar.setForeground(new Color(6, 176, 37));
			JOptionPane.showMessageDialog(frame, "Sao lưu thành công", "Sao lưu", JOptionPane.INFORMATION_MESSAGE);
		} else {
			textMess.append("\nSao lưu không thành công");
			textMess.append("\n========================");
			progressBar.setForeground(Color.RED);
			JOptionPane.showMessageDialog(frame, "Sao lưu không thành công", "Sao lưu", JOptionPane.ERROR_MESSAGE);
		}
	}

}
