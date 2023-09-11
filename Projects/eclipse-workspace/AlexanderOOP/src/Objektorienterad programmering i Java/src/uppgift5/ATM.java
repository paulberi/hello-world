package uppgift5;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ATM extends Application
{
	public final static String id = "206302";

	private Account               currentAccount;
	private CardData              currentCard;
	private WalletPopup           walletPopup;
	private DisplayArea           displayArea           = new DisplayArea();
	private CardArea              cardArea              = new CardArea();
	private ControlBoard          controlBoard          = new ControlBoard();
	private MoneyOutput           moneyOutput           = new MoneyOutput();
	ReceiptOutput                 receiptOutput         = new ReceiptOutput();
	private String                currentPinCode        = "";
	private int                   currentAmount         = 0;
	private int                   maximumAmount         = 5000;
	private String                currentAmountString   = "";
	private boolean               currentAmountAccepted = false;
	private boolean               receiptRequested      = false;
	private boolean               receiptAfterWithdraw  = false;
	private boolean               changePinCode         = false;
	private boolean               changePinCodeAgain    = false;
	private String                changeCurrentPinCode  = "";
	private IntegerProperty       currentStep           = new SimpleIntegerProperty(-1);
	private ListProperty<Account> accounts              = new SimpleListProperty<Account>(
			FXCollections.observableArrayList(new ArrayList<Account>()));
	private final String[][]      displayStages         = {
			{"V�lkommen!", "S�tt in ditt kort f�r att k�ra ig�ng."}, {"Var god v�nta..", ""},
			{"Tryck in din PIN-kod och \"klar\".", "T�nk p� att skydda din PIN-kod."},
			{"Skriv eller v�lj belopp.", ""}, {"Vill du ha kvitto?", ""}, {"Ta ditt kort.", ""},
			{"Ta dina pengar.", ""}, {"Ta ditt kvitto.", ""}};
	private final int[]           availableNotes        = {1000, 500, 200, 100, 50, 20};

	public static void main(String[] args)
	{
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		HBox mainRow = new HBox(32);
		mainRow.setAlignment(Pos.CENTER);

		VBox leftColumn = new VBox(16);
		leftColumn.setAlignment(Pos.CENTER);
		mainRow.getChildren().add(leftColumn);

		leftColumn.getChildren().add(displayArea);
		displayArea.addEventHandler(displayArea.sideButtonClicked, e -> onSideButtonClicked());

		VBox moneyOutputColumn = new VBox(4);
		moneyOutputColumn.setAlignment(Pos.CENTER);
		moneyOutput.currentAmount.addListener((observable, oldValue, newValue) ->
		{
			if (newValue.intValue() == 0 && oldValue.intValue() > 0)
			{
				if (receiptRequested)
				{
					nextStep();
				}
				else
					setStep(0);

			}

		});
		moneyOutputColumn.getChildren().addAll(new Header("Sedlar"), moneyOutput);
		leftColumn.getChildren().add(moneyOutputColumn);

		leftColumn.getChildren().add(controlBoard);
		controlBoard.addEventHandler(controlBoard.numpedClicked, event -> onNumpadClicked());
		controlBoard.addEventHandler(controlBoard.cancelClicked, event -> onCancelClicked());
		controlBoard.addEventHandler(controlBoard.clearClicked, event -> onClearClicked());
		controlBoard.addEventHandler(controlBoard.enterClicked, e -> onEnterClicked());

		VBox rightColumn = new VBox(64);
		rightColumn.setAlignment(Pos.CENTER);

		VBox receiptOutputColumn = new VBox(4);
		receiptOutputColumn.setAlignment(Pos.CENTER);
		receiptOutputColumn.getChildren().addAll(new Header("Minneslapp"), receiptOutput);
		rightColumn.getChildren().add(receiptOutputColumn);

		receiptOutput.showReceiptProperty.addListener((observable, oldValue, newValue) ->
		{
			if (oldValue.booleanValue() == true && newValue.booleanValue() == false)
			{
				@SuppressWarnings("unused") ReceiptPopup receiptPopup = new ReceiptPopup(
						receiptAfterWithdraw
								? ReceiptPopup.TYPE_WITHDRAWAL
								: ReceiptPopup.TYPE_INFORMATION,
						getTimestamp(), currentCard.getNumber(), currentAmount,
						currentAccount.getBalance(), currentAccount.getTransactions());

				setStep(0);
			}

		});

		CardArea.currentCardProperty.addListener(
				(observable, oldCard, newCard) -> onCardAreaCurrentCardChanged(newCard));
		accounts.addListener((observable, oldValue, newValue) ->
		{
			cardArea.cardsProperty.clear();

			for (Account acc : newValue)
			{
				for (CardData cardData : acc.getCards())
				{
					cardArea.addCard(new Card(cardData));
				}

			}

		});
		rightColumn.getChildren().add(cardArea);

		mainRow.getChildren().add(rightColumn);

		currentStep.addListener(
				(observable, oldValue, newValue) -> onCurrentStepChanged(newValue.intValue()));
		setStep(0);

		StackPane mainStack = new StackPane();
		mainStack.setPadding(new Insets(16));
		
		ImageView surfaceNoise = new ImageView(new Image(getClass().getResourceAsStream("surfacenoise.png")));
		surfaceNoise.setOpacity(.20);
		surfaceNoise.fitWidthProperty().bind(primaryStage.widthProperty());
		surfaceNoise.fitHeightProperty().bind(primaryStage.heightProperty());
		mainStack.getChildren().add(surfaceNoise);
		
		mainStack.getChildren().add(mainRow);
		
		VBox walletAndAccountCreationColumn = new VBox(8);
		walletAndAccountCreationColumn.setAlignment(Pos.BASELINE_RIGHT);
		StackPane.setAlignment(walletAndAccountCreationColumn, Pos.BOTTOM_RIGHT);
		
		Button walletButton = new Button("�ppna pl�nboken");
		walletButton.visibleProperty().bind(Bindings.equal(0, currentStep));
		walletButton.disableProperty().bind(Bindings.size(cardArea.cardsProperty).lessThan(1));
		walletButton.setOnMouseClicked(e ->
		{
			walletPopup = new WalletPopup(cardArea.cardsProperty.toArray());
			walletPopup.isOpen.addListener((observable, oldValue, newValue) ->
			{
				if (newValue == false)
				{
					CardArea.currentCardProperty.setValue(walletPopup.pickedCard.getValue());
					walletPopup = null;
				}

			});
		});
		walletAndAccountCreationColumn.getChildren().add(walletButton);
		Button newAccountButton = new Button("Skapa konto");
		newAccountButton.visibleProperty().bind(Bindings.greaterThan(1, currentStep));
		newAccountButton.setOnMouseClicked(e ->
		{
			@SuppressWarnings("unused") CreateAccountPopup createAccountPopup = new CreateAccountPopup();
			createAccountPopup.isOpen.addListener((observable, oldValue, newValue) ->
			{
				if (oldValue == true && newValue == false)
				{
					accounts.add(createAccountPopup.accountProperty.getValue());
				}

			});
		});

		mainStack.getChildren().add(walletAndAccountCreationColumn);

		Scene scene = new Scene(mainStack);
		scene.getStylesheets().add(this.getClass().getResource("MainTheme.css").toExternalForm());

		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Uppgift 5: Bankomaten");
		primaryStage.show();
		
		walletAndAccountCreationColumn.getChildren().add(newAccountButton);
		walletAndAccountCreationColumn.minWidthProperty().bind(Bindings.max(walletButton.widthProperty(), newAccountButton.widthProperty()));
		walletAndAccountCreationColumn.minHeightProperty().bind(walletAndAccountCreationColumn.spacingProperty().add(walletButton.heightProperty()).add(newAccountButton.heightProperty()));
		walletAndAccountCreationColumn.setMaxSize(walletAndAccountCreationColumn.getMinWidth(), walletAndAccountCreationColumn.getMinHeight());
		walletAndAccountCreationColumn.setPrefSize(walletAndAccountCreationColumn.getMinWidth(), walletAndAccountCreationColumn.getMinHeight());

		accounts.setValue(XMLHandler.load());
	}


	@Override
	public void stop()
	{
		XMLHandler.save(accounts.toArray());
	}

	private void nextStep()
	{
		currentStep.setValue(currentStep.intValue() + 1);
	}

	private void setStep(int step)
	{
		currentStep.setValue(step);
	}

	public static long getTimestamp()
	{
		return System.currentTimeMillis();
	}

	private void onCancelClicked()
	{
		if (currentStep.intValue() > 0)
		{
			if (currentCard != null)
			{
				cardArea.cardLockedProperty.setValue(false);
				setStep(5);
			}
			else
				setStep(0);

		}

	}

	private void onCurrentStepChanged(int step)
	{
		if (step == 0)
		{
			currentAmount         = 0;
			currentCard           = null;
			currentAccount        = null;
			currentAmountAccepted = false;
			currentAmountString   = "";
			currentPinCode        = "";
			changeCurrentPinCode  = "";
			receiptAfterWithdraw  = false;
			receiptRequested      = false;
		}
		else if (step == 3)
		{
			displayArea.setSideButtonTexts("100", "200", "500", "750", "1000", "S�tt in pengar",
					"�ndra PIN-kod", "Kontoutdrag");
		}
		else if (step == 4)
		{
			currentAccount.withdraw(currentAmount);
			displayArea.setSideButtonTexts("", "", "", "Ja", "", "", "", "Nej");
		}
		else
		{
			displayArea.clearSideButtonTexts();

			if (step == 5)
			{
				cardArea.cardLockedProperty.setValue(false);
			}
			else if (step == 6)
			{
				moneyOutput.currentAmount.setValue(currentAmount);
			}
			else if (step == 7)
			{
				receiptOutput.showReceipt();
			}

		}

		displayArea.setLabelTextProperty(displayStages[step][0]);
		displayArea.setSubTextProperty(displayStages[step][1]);

		displayArea.setInputTextProperty("");
		displayArea.setInputFeedbackProperty("");
	}

	private void onSideButtonClicked()
	{
		if (currentStep.intValue() == 3)
		{
			// Value
			if (displayArea.getSideButtonClickedIndex() < 5)
			{
				int[] values = {100, 200, 500, 750, 1000};
				currentAmount = values[displayArea.getSideButtonClickedIndex()];

				if (currentAmount <= currentAccount.getBalance())
				{
					nextStep();
				}
				else
				{
					displayArea.showBalanceTooLow();
				}

			}
			// Cancel
			else if (displayArea.getSideButtonClickedIndex() == 5)
			{
				onDepositClicked();
			}
			// Change pin
			else if (displayArea.getSideButtonClickedIndex() == 6)
			{
				changePinCode      = true;
				changePinCodeAgain = true;

				setStep(2);
				displayArea.setLabelTextProperty("Skriv in din nya PIN-kod.");
			}
			// Receipt
			else
			{
				currentAmount        = 0;
				receiptRequested     = true;
				receiptAfterWithdraw = false;
				setStep(5);
			}

		}
		else if (currentStep.intValue() == 4 && (displayArea.getSideButtonClickedIndex() == 3
				|| displayArea.getSideButtonClickedIndex() == 7))
		{
			if (displayArea.getSideButtonClickedIndex() == 3)
			{
				receiptRequested     = true;
				receiptAfterWithdraw = true;
				nextStep();
			}
			else
			{
				receiptRequested = false;
				nextStep();
			}

		}

	}

	private void onNumpadClicked()
	{
		if (currentStep.intValue() == 2)
		{
			if (currentPinCode.length() < 4)
			{
				currentPinCode += String.valueOf(controlBoard.getNumpadNumer());
				displayArea.setInputTextProperty("*".repeat(currentPinCode.length()));

				if (currentPinCode.length() > 0 && currentPinCode.length() < 4)
					displayArea.setInputFeedbackProperty("F�r kort");
				else
					displayArea.setInputFeedbackProperty("");
			}

		}

		else if (currentStep.intValue() == 3 && currentAmountString.length() < 4)
		{
			currentAmountString += String.valueOf(controlBoard.getNumpadNumer());
			displayArea.setInputTextProperty(currentAmountString);

			ArrayList<Integer> notes = new ArrayList<Integer>();
			int                sum   = 0;
			for (int note : availableNotes)
			{
				while (note <= Integer.parseInt(currentAmountString) - sum)
				{
					notes.add(note);
					sum += note;
				}

			}

			if (sum != Integer.parseInt(currentAmountString))
			{
				displayArea.setInputFeedbackProperty("Ogiltig summa");
				currentAmountAccepted = false;
			}
			else if (sum > maximumAmount)
			{
				displayArea.setInputFeedbackProperty("Maxbeloppet �r 5000");
				currentAmountAccepted = false;
			}
			else
			{
				displayArea.setInputFeedbackProperty("");
				currentAmountAccepted = true;
			}

		}

	}

	private void onDepositClicked()
	{
		DepositPopup depositPopup = new DepositPopup();
		depositPopup.isOpen.addListener((observable, oldValue, newValue) ->
		{
			if (depositPopup.sum.intValue() > 0)
			{
				currentAccount.deposit(depositPopup.sum.intValue());
				displayArea.showText("Ins�ttning klar");
			}

		});
	}

	private void onClearClicked()
	{
		if (currentStep.intValue() == 2)
		{
			currentPinCode = "";
			displayArea.setInputTextProperty("");
			displayArea.setInputFeedbackProperty("");
		}
		else if (currentStep.intValue() == 3)
		{
			currentAmountString = "";
			displayArea.setInputTextProperty("");
			displayArea.setInputFeedbackProperty("");
		}

	}

	private void onEnterClicked()
	{
		if (currentStep.intValue() == 2 && currentPinCode.length() == 4)
		{
			if (!changePinCode)
			{
				if (Long.valueOf(currentPinCode) == currentCard.getPinCode())
				{
					accountLoop :
					for (Account account : accounts)
					{
						for (CardData cardData : account.getCards())
						{
							if (cardData.getNumber() == currentCard.getNumber())
							{
								currentAccount = account;

								nextStep();
								break accountLoop;
							}

						}

					}
				}
				else
				{
					currentPinCode = "";
					displayArea.setInputTextProperty("");
					displayArea.showWrongPinCode();
				}

			}
			else
			{
				if (changePinCodeAgain)
				{
					displayArea.setLabelTextProperty("Skriv in din nya PIN-kod igen.");
					changePinCodeAgain   = false;
					changeCurrentPinCode = new String(currentPinCode);
				}
				else
				{
					if (changeCurrentPinCode.equals(currentPinCode))
					{
						changePinCode = false;
						for (CardData cardData : currentAccount.getCards())
						{
							if (cardData.equals(currentCard))
							{
								cardData.setPinCode(Integer.parseInt(changeCurrentPinCode));

							}

						}

						nextStep();
					}
					else
					{
						displayArea.showWrongPinCode();
					}

				}

			}

			currentPinCode = "";
			displayArea.setInputTextProperty("");
		}
		else if (currentStep.intValue() == 3 && currentAmountAccepted)
		{
			if (Integer.parseInt(currentAmountString) <= currentAccount.getBalance())
			{
				currentAmount = Integer.parseInt(currentAmountString);
				nextStep();
			}
			else
			{
				displayArea.showBalanceTooLow();
			}

		}

	}

	private void onCardAreaCurrentCardChanged(Card newCard)
	{
		if (currentStep.intValue() == 5 && newCard == null)
		{
			if (currentAmount > 0)
				nextStep();
			else if (receiptRequested)
			{
				setStep(7);
			}
			else
				setStep(0);
		}

		else if (newCard != null)
		{
			cardArea.cardLockedProperty.setValue(true);

			nextStep();

			accountLoop :
			for (Account account : accounts)
			{
				for (CardData cardData : account.getCards())
				{
					if (cardData.getNumber() == newCard.getNumber())
					{
						currentCard = cardData;
						break accountLoop;
					}

				}

			}

			// Simulate loading time
			Timeline readCardDelay = new Timeline(
					new KeyFrame(Duration.millis(Math.round(600 + (Math.random() * 600))),
							new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event)
								{
									nextStep();
									System.out.println("Hey thief, for this card use the pin "
											+ currentCard.getPinCode());

								}
							}));
			readCardDelay.setCycleCount(1);
			readCardDelay.play();
		}
	}
}