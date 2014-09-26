package com.headswilllol.mineflat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DummyMain extends JFrame implements ActionListener {

	private final JFrame f;

	private String[] str = new String[]{
			"MineFlat cannot be opened as a standalone program. Instead, a special launcher",
			"must be used to ensure its libraries are properly loaded and it is kept updated.",
			"You can grab a copy of MineFlat's launcher from my CI server at ci.amigocraft.net,",
			"or by clicking the button below."};

	private final Font font = new Font("Arial", Font.BOLD, 16);

	public DummyMain(){
		f = this;
		f.addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e){System.exit(0);}});
		f.setTitle("MineFlat");
		f.setSize(800, 600);
		f.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 400, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 300);
		try {
			f.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/images/icon.png")));
		}
		catch (IOException ex){
			// who cares?
		}
		f.setLayout(null);

		JButton button = new JButton("Get the Launcher");
		button.setVerticalTextPosition(AbstractButton.CENTER);
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setMnemonic(KeyEvent.VK_ENTER);
		button.setActionCommand("getLauncher");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(200, 50));
		button.setBounds(300, 275, 200, 50);
		button.setVisible(true);
		f.add(button);

		JButton quit = new JButton("Quit");
		quit.setVerticalTextPosition(AbstractButton.CENTER);
		quit.setHorizontalTextPosition(AbstractButton.CENTER);
		quit.setMnemonic(KeyEvent.VK_ENTER);
		quit.setActionCommand("quit");
		quit.addActionListener(this);
		quit.setPreferredSize(new Dimension(200, 50));
		quit.setBounds(300, 350, 200, 50);
		quit.setVisible(true);
		f.add(quit);

		f.setVisible(true);

		try {
			Thread.sleep(5);
		}
		catch (InterruptedException ex){
			ex.printStackTrace();
		}
		repaint();
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.setFont(font);
		int y = 200;
		for (String s : str) {
			g.drawString(s, centerText(g, s), y);
			y += 20;
		}
	}

	private int centerText(Graphics g, String text){
		int stringLen = (int)
				g.getFontMetrics().getStringBounds(text, g).getWidth();
		return 800 / 2 - stringLen / 2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("getLauncher")) {
			try {
				Desktop.getDesktop().browse(new URI("http://ci.amigocraft.net/job/MineFlat%20Launcher"));
			}
			catch (IOException ex) {
				ex.printStackTrace();
				str = new String[]{"Failed to open browser! You can still get the launcher manually by going to ci.amigocraft.net."};
				repaint();
			}
			catch (URISyntaxException ex) {
				ex.printStackTrace();
				str = new String[]{"Failed to open browser! You can still get the launcher manually by going to ci.amigocraft.net."};
				repaint();
			}
		}
		else if (e.getActionCommand().equals("quit")){
			System.exit(0);
		}
	}
}