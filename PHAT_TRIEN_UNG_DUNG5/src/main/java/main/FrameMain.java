package main;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import frame.FrameDangnhap;

public class FrameMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FrameMain() {
		try {
			setIconImage(ImageIO.read(new File("img/code1.png")));
		} catch (IOException exception) {
		}
		setSize(600, 400);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setUndecorated(true);
		getContentPane().setLayout(null);

		ImageIcon bg_img = new ImageIcon("img\\start.png");
		Image img = bg_img.getImage();
		Image temp_img = img.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
		bg_img = new ImageIcon(temp_img);

		JLabel lbl = new JLabel(bg_img);
		lbl.setBounds(0, 0, 600, 400);
		getContentPane().add(lbl);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		progressBar.setBounds(2, 388, 596, 10);
		progressBar.setForeground(new Color(6, 176, 37));
		getContentPane().add(progressBar);

		StartTask task = new StartTask(this);
		task.addPropertyChangeListener(evt -> {
			if ("progress".equals(evt.getPropertyName()))
				progressBar.setValue((int) evt.getNewValue());
		});
		task.execute();

	}

	public static void main(String[] args) {
		new FrameMain().setVisible(true);
	}
}

/**
 * @author kienc
 *
 */
class StartTask extends SwingWorker<Void, Integer> {

	private JFrame frameStart;

	public StartTask(JFrame frameStart) {
		super();
		this.frameStart = frameStart;
	}

	@Override
	protected Void doInBackground() throws Exception {
		new Thread(() -> {
			for (int i = 0; i <= 100; i++) {
				setProgress(i);
				try {
					Thread.sleep(60);
				} catch (InterruptedException e) {
				}
			}
		}).start();
//		Thread.sleep(100000);
		new FrameDangnhap().setVisible(true);
		return null;
	}

	@Override
	protected void done() {
		frameStart.dispose();
	}
}
