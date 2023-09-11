package uppgift5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ReceiptPopup extends Stage
{
	public static final int TYPE_WITHDRAWAL  = 0;
	public static final int TYPE_INFORMATION = 1;

	public ReceiptPopup(int type, long timestamp, long cardNumber, double sum, double accountBalance, ArrayList<Transaction> accountTransactions)
	{
		VBox mainColumn = new VBox();
		mainColumn.setPadding(new Insets(32, 16, 16, 16));
		mainColumn.setAlignment(Pos.CENTER);

		Label headerLabel = new Label("..EA Finans");
		headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-font-style: italic;");
		mainColumn.getChildren().add(headerLabel);

		Label separator = new Label("--------------------------------");
		separator.setStyle("-fx-font-size: 16px;");
		mainColumn.getChildren().add(separator);

		Label subHeader = new Label(type == TYPE_WITHDRAWAL ? "UTTAG" : "KONTOUTDRAG");
		subHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
		mainColumn.getChildren().add(subHeader);

		VBox bottomColumn = new VBox(8);
		mainColumn.getChildren().add(bottomColumn);

		ReceiptRow cardNumberRow = new ReceiptRow("KORTNUMMER", "*".repeat(String.valueOf(cardNumber).length() -4)+String.valueOf(cardNumber).substring(String.valueOf(cardNumber).length() -4));
		bottomColumn.getChildren().add(cardNumberRow);
		
		GridPane basicGrid = new GridPane();
		basicGrid.setHgap(4);
		basicGrid.add(new Label("DATUM"), 0, 0);
		basicGrid.add(new Label("TID"), 1, 0);
		basicGrid.add(new Label("AUTOMAT"), 2, 0);
		basicGrid.add(new Label(timestampToDateAndTime(timestamp)[0]), 0, 1);
		basicGrid.add(new Label(timestampToDateAndTime(timestamp)[1]), 1, 1);
		basicGrid.add(new Label(ATM.id), 2, 1);
		bottomColumn.getChildren().add(basicGrid);
		
		if (type == TYPE_WITHDRAWAL)
		{
			ReceiptRow sumRow = new ReceiptRow("BELOPP", formatSum(sum));
			bottomColumn.getChildren().add(sumRow);
		}
		
		ReceiptRow balanceRow = new ReceiptRow("SALDO", formatSum(accountBalance));
		bottomColumn.getChildren().add(balanceRow);
		
		VBox transactionsRow = new VBox();
		transactionsRow.setMinWidth(mainColumn.getWidth());
		
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		for (Transaction t : accountTransactions)
			transactions.add(t);
		Collections.reverse(transactions);
		for (int i=0; i<Math.min(transactions.size(),(type == TYPE_WITHDRAWAL ? 5 : 10)); i++)
		{
			HBox transactionRow = new HBox(4);
			transactionRow.getChildren().add(new Label(new SimpleDateFormat("MM-dd").format(transactions.get(i).getCreatedTimestamp())));
			transactionRow.getChildren().add(new Label(transactions.get(i).getMessage()));
			Pane spacer = new Pane();
		    HBox.setHgrow(spacer, Priority.ALWAYS);
		    spacer.setMinSize(4, 1);
		    transactionRow.getChildren().add(spacer);
			transactionRow.getChildren().add(new Label(formatSum(transactions.get(i).getSum())));
			transactionsRow.getChildren().add(transactionRow);
		}
		bottomColumn.getChildren().add(transactionsRow);

		Scene scene = new Scene(mainColumn);
		scene.getStylesheets().add(this.getClass().getResource("ReceiptTheme.css").toExternalForm());
		setScene(scene);
		setTitle("Kvitto");
		show();
	}

	private String[] timestampToDateAndTime(long timestamp)
	{
		String[] returns = new String[2];

		returns[0] = new SimpleDateFormat("yy-MM-dd").format(timestamp);
		returns[1] = new SimpleDateFormat("HH:mm").format(timestamp);

		return returns;
	}
	
	private String formatSum(double sum)
	{
		String formatted = String.valueOf(sum);
		if (formatted.substring(formatted.indexOf(".")).length() < 3)
			formatted += "0";
		if (formatted.substring(formatted.indexOf(".")).length() > 2)
			formatted = formatted.substring(0, formatted.indexOf(".") +3);
		
		return formatted+" kr";
	}
}
