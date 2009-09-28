package nu.epsilon.rss.ui.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Pär Sikö
 */
public class UserNamePanel extends JPanel {

	private Font font = new Font("Arial", Font.BOLD, 18);

	public UserNamePanel() {

		setOpaque(false);
		setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel userName = new JLabel("UserName: ");
		userName.setForeground(Color.WHITE);
		userName.setFont(font);
		add(userName);

		JTextField userNameTextField = new JTextField("Test...", 13);
		userNameTextField.setFont(font);
		userNameTextField.setForeground(Color.WHITE);
		userNameTextField.setOpaque(false);
		userNameTextField.setBorder(new XBorder());
		add(userNameTextField);

	}
}
