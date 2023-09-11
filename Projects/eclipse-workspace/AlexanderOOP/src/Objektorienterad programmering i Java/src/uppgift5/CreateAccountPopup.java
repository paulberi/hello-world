package uppgift5;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CreateAccountPopup extends Stage
{
	BooleanProperty         isOpen          = new SimpleBooleanProperty(true);
	ObjectProperty<Account> accountProperty = new SimpleObjectProperty<Account>(new Account());

	private Card  card         = new Card();
	private Label pinCodeLabel = new Label();

	public CreateAccountPopup()
	{
		randomizeAccountNumber();
		randomizeAccountBalance();
		randomizeAccountCardData();

		VBox mainColumn = new VBox(4);
		mainColumn.setPadding(new Insets(16));

		Label accountHeader = new Label("Kontouppgifter");
		accountHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
		mainColumn.getChildren().add(accountHeader);

		Label accountNumberHeader = new Label("Kontonummer");
		accountNumberHeader.setStyle("-fx-font-weight: bold");
		Label accountNumberLabel = new Label();
		accountNumberLabel.textProperty().bind(Bindings.concat(accountProperty.get().accountNumber));
		Button accountNumberRandomize = new Button("Slumpa");
		accountNumberRandomize.setOnMouseClicked(e ->
		{
			randomizeAccountNumber();
		});
		mainColumn.getChildren().addAll(accountNumberHeader, accountNumberLabel, accountNumberRandomize);

		Label accountBalanceHeader = new Label("Balans");
		accountBalanceHeader.setStyle("-fx-font-weight: bold");
		Label accountBalanceLabel = new Label();
		accountBalanceLabel.textProperty().bind(Bindings.concat(accountProperty.get().balance).concat(" kr"));
		Button accountBalanceRandomize = new Button("Slumpa");
		accountBalanceRandomize.setOnMouseClicked(e ->
		{
			randomizeAccountBalance();
		});
		mainColumn.getChildren().addAll(accountBalanceHeader, accountBalanceLabel, accountBalanceRandomize);

		Label cardHeader = new Label("Kontokort");
		cardHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
		mainColumn.getChildren().add(cardHeader);

		Button accountCardRandomize = new Button("Slumpa");
		accountCardRandomize.setOnMouseClicked(e ->
		{
			randomizeAccountCardData();
		});

		Label pinCodeHeader = new Label("PIN-kod");
		pinCodeHeader.setStyle("-fx-font-weight: bold");

		mainColumn.getChildren().addAll(card, pinCodeHeader, pinCodeLabel, accountCardRandomize);

		mainColumn.getChildren().add(new VSpacer(16));
		
		VBox confirmButtonAligned = new VBox();
		confirmButtonAligned.setAlignment(Pos.CENTER);
		Button confirmButton = new Button("Lägg till");
		confirmButton.setOnMouseClicked(e ->
		{
			// TODO: Bind it to the ATM.java function
			isOpen.setValue(false);;
			close();
		});
		confirmButtonAligned.getChildren().add(confirmButton);
		mainColumn.getChildren().add(confirmButtonAligned);

		Scene scene = new Scene(mainColumn);
		setTitle("Skapa konto");
		setScene(scene);
		show();
	}

	private void randomizeAccountNumber()
	{
		String r = "";
		for (int i = 0; i < 4; i++)
			r += 1000 + Math.round(Math.random() * 8999);

		accountProperty.get().accountNumber.setValue(Long.parseLong(r));
	}

	private void randomizeAccountBalance()
	{
		double r = Math.random() * 75000;
		String s = String.valueOf(r);
		if (s.indexOf(".") > 0 && s.length() > s.indexOf(".") + 2)
			s = s.substring(0, s.indexOf(".") + 3);

		accountProperty.get().balance.setValue(Double.parseDouble(s));
	}

	private void randomizeAccountCardData()
	{
		String r = "";
		for (int i = 0; i < 4; i++)
			r += 1000 + Math.round(Math.random() * 8999);

		String   pinCode  = String.valueOf(Math.round(Math.random() * 9999));
		CardData cardData = new CardData(Long.parseLong(r), Integer.parseInt(pinCode));
		
		if (pinCode.length() == 3)
			pinCode = "0"+pinCode;

		accountProperty.get().cards.clear();
		accountProperty.get().addCard(cardData);
		card.setNumber(Long.parseLong(r));
		pinCodeLabel.setText(pinCode);
	}
}
