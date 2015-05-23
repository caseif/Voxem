/*
 * Voxem
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.caseif.voxem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DummyMain extends JFrame implements ActionListener {

	private String[] str = new String[]{
			"Voxem cannot be opened as a standalone program. Instead, a special launcher",
			"must be used to ensure its libraries are properly loaded and it is kept updated.",
			"You can grab a copy of Voxem's launcher from my CI server at ci.caseif.net,",
			"or by clicking the button below."};

	private final Font font = new Font("Arial", Font.BOLD, 16);

	public DummyMain(){
		JFrame f = this;
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		f.setTitle("Voxem");
		f.setSize(800, 600);
		f.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 400,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 300);
		try {
			f.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/textures/block/grass.png")));
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
				Desktop.getDesktop().browse(new URI("http://ci.caseif.net/job/Voxem%20Launcher"));
			}
			catch (IOException | URISyntaxException ex) {
				ex.printStackTrace();
				str = new String[]{
						"Failed to open browser! You can still get the launcher manually by going to ci.amigocraft.net."
				};
				repaint();
			}
		}
		else if (e.getActionCommand().equals("quit")){
			System.exit(0);
		}
	}
}