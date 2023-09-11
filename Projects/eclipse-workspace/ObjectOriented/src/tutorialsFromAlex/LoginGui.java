package tutorialsFromAlex;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginGui {
	public static void main(String[] args) {
		
		JPanel panel=new JPanel();
		JFrame frame=new JFrame();
		frame.setSize(350, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(panel);
		
		panel.setLayout(null);
		
		JLabel label =new JLabel("User");
		label.setBounds(10,20,80,25);
		panel.add(label);
		
		JTextField userText=new JTextField(20);
		userText.setBounds(100,20,165,25);
		panel.add(userText);
		
		JLabel passwordLabel=new JLabel("Password");
		passwordLabel.setBounds(10,50,80,25);
		panel.add(passwordLabel);
		
		JPasswordField passwordText=new JPasswordField();
		passwordText.setBounds(100,50,165,25);
		panel.add(passwordText);
		
		JButton button =new JButton("Login");
		button.setBounds(10,80,80,25);
		panel.add(button);
		
		frame.setVisible(true);
		countLetters();
	}
	private static void countLetters() {
		String str="how the hell did all this get to be the norm";
		char[] ch=str.toCharArray();
		int[]freq=new int[str.length()];
		
		for(int i=0; i<str.length(); i++) {
			freq[i]=1;
			for (int j=i+1; j<str.length(); j++ ) {
				if(ch[i]==ch[j]) {
					freq[i]++;
					ch[j]='0';
				}
			}
		}
		for (int i=0;i<freq.length;i++) {
			if(ch[i]!=' '&&ch[i]!='0') {
				System.out.println(ch[i]+"="+freq[i]);
			}
		}
		
	}

}
