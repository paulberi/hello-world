package application;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
public class FXMLDocumentController {
	

	    @FXML
	    private Label txt_result;
	    
	    String op="";
	    long number1;
	    long number2;
	    
	    //first method
	    public void number(ActionEvent ae) {
	    	String no=((Button)ae.getSource()).getText();
	    	txt_result.setText(txt_result.getText()+no);
	    }
	    //second method
	    public void operation(ActionEvent ae) {
	    	String operation=((Button)ae.getSource()).getText();
	    	if(!operation.equals("=")) {
	    		if(!op.equals("")) {
	    			return;
	    		}
	    		op=operation;
	    		number1=Long.parseLong(txt_result.getText());
	    		txt_result.setText("");
	    	}
	    	else {
	    		if(op.equals("")) {
	    			return;
	    		}
	    		number2 =Long.parseLong(txt_result.getText());
	    		calculate(number1,number2,op);
	    		op="";
	    	}
	    }
	    public void calculate(long n1, long n2, String op) {
	    	
	    	switch(op) {
			case "+" :txt_result.setText(n1+n2+"");break;
			case "-" :txt_result.setText(n1-n2+"");break;
			case "*" :txt_result.setText(n1*n2+"");break;
			case "/" :
				if(n2==0) {
					txt_result.setText("0");break;
				}
				txt_result.setText(n1/n2+"");break;
			
			
		}

	    }
	    public void intialize(URL url, ResourceBundle rb) {
	    	//TODO
	    }

	

}
