package stopWatch;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StopWatchByBroCodeYT implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton startButton =new JButton();
	JButton resetButton =new JButton();
	JLabel timeLabel= new JLabel();
	int elapsedTime= 0;
	int milliseconds=0;
	int seconds=0;
	int minutes=0;
	int hours=0;
	boolean started= false;
	String milliseconds_string=String.format("%03d", milliseconds);
	String seconds_string=String.format("%02d", seconds);
	String minutes_string=String.format("%02d", minutes);
	String hours_string=String.format("%02d", hours);
	String starts="Start";
	String reset="Reset";
	
	Timer timer=new Timer(1000, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			elapsedTime=elapsedTime+1;
			seconds=(elapsedTime/1000)%60;
			minutes=(elapsedTime/60000)%60;
			hours=(elapsedTime/3600000);
			
			milliseconds_string=String.format("%03d", milliseconds);
			seconds_string=String.format("%02d", seconds);
			minutes_string=String.format("%02d", minutes);
			hours_string=String.format("%02d", hours);
			timeLabel.setText(hours_string+":"+minutes_string+":"+seconds_string);
		}
		
	});
	
	StopWatchByBroCodeYT(){
		
		timeLabel.setText(hours_string+":"+minutes_string+":"+seconds_string+":"+milliseconds_string);
		timeLabel.setBounds(100, 100, 300, 100);
		timeLabel.setFont(new Font("verdana",Font.PLAIN,35));
		timeLabel.setBorder(BorderFactory.createBevelBorder(1));
		timeLabel.setOpaque(true);
		timeLabel.setHorizontalAlignment(JTextField.CENTER);
		
		startButton.setText(starts);
		startButton.setBounds(100,200,100,50);
		startButton.setFont(new Font("verdana",Font.PLAIN,20));
		startButton.setFocusable(false);
		startButton.addActionListener(this);
		
		resetButton.setText(reset);
		resetButton.setBounds(200,200,100,50);
		resetButton.setFont(new Font("verdana",Font.PLAIN,20));
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		
		frame.add(startButton);
		frame.add(resetButton);
		frame.add(timeLabel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==startButton) {
			start();
			if(started==false) {
				started=true;
				startButton.setText("STOP");
				start();
			}
			else {
				started=false;
				startButton.setText("START");
				stop();
			}
		}
		if(e.getSource()==resetButton) {
			reset();
		}
	}

	private void reset() {
		// TODO Auto-generated method stub
		timer.stop();
		elapsedTime=0;
		seconds=0;
		minutes=0;
		hours=0;
		seconds_string=String.format("%02d", seconds);
		minutes_string=String.format("%02d", minutes);
		hours_string=String.format("%06d", hours);
		timeLabel.setText(hours_string+":"+minutes_string+":"+seconds_string+":"+milliseconds_string);
	
	}

	private void stop() {
		// TODO Auto-generated method stub
		timer.stop();
	}

	void start() {
		// TODO Auto-generated method stub
		timer.start();
		
	}


}
