package application.extra;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Different types of usable dialog boxes in the application for fast
 * and easy access.
 * 
 * @author Mattias Persson
 */
public final class DialogBox {
	// Denies access to create instances of this class
	private DialogBox() {
	}

	/**
	 * Show a custom alert dialog box to the user.
	 * 
	 * @param type   The type of alert dialog
	 * @param title  Dialog box title (window title)
	 * @param header Shown in bigger text beside the alert icon
	 * @param text   Shown in normal sized text beneath the header and
	 *               icon
	 */
	public static void dialog(AlertType type, String title, String header,
			String text) {

		Alert alert = new Alert(type);
		// Use the same icon as main window
//		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
//				.add(new Image(
//						Main.class.getResourceAsStream(Main.APP_ICON)));
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}

	/**
	 * Creates a confirmation dialog so that the user has an option to
	 * accept or decline an action.
	 * 
	 * @param  title Dialog box title (window title)
	 * @param  text  Shown in bigger text beside the confirmation icon
	 * @return       True if the user accepts, false if the user aborts
	 */
	public static boolean confirmation(String title, String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		// Use the same icon as main window
//		((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
//				.add(new Image(
//						Main.class.getResourceAsStream(Main.APP_ICON)));
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(text);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Show an information dialog to the user.
	 * 
	 * @param title  Dialog box title (window title)
	 * @param header Shown in bigger text beside the information icon
	 * @param text   Shown in normal sized text beneath the header and
	 *               icon
	 */
	public static void information(String title, String header,
			String text) {
		dialog(AlertType.INFORMATION, title, header, text);
	}

	/**
	 * Show an information dialog to the user without a header section.
	 * See {@link #information(String, String, String)} to use a header as
	 * well.
	 * 
	 * @param title Dialog box title (window title)
	 * @param text  Shown in bigger text beside the information icon
	 */
	public static void information(String title, String text) {
		dialog(AlertType.INFORMATION, title, null, text);
	}

	/**
	 * Show a warning dialog to the user.
	 * 
	 * @param title  Dialog box title (window title)
	 * @param header Shown in bigger text beside the warning icon
	 * @param text   Shown in normal sized text beneath the header and
	 *               icon
	 */
	public static void warning(String title, String header, String text) {
		dialog(AlertType.WARNING, title, header, text);
	}

	/**
	 * Show a warning dialog to the user without a header section. See
	 * {@link #warning(String, String, String)} to use a header as well.
	 * 
	 * @param title Dialog box title (window title)
	 * @param text  Shown in bigger text beside the warning icon
	 */
	public static void warning(String title, String text) {
		dialog(AlertType.WARNING, title, null, text);
	}

	/**
	 * Show a error dialog to the user.
	 * 
	 * @param title  Dialog box title (window title)
	 * @param header Shown in bigger text beside the error icon
	 * @param text   Shown in normal sized text beneath the header and
	 *               icon
	 */
	public static void error(String title, String header, String text) {
		dialog(AlertType.ERROR, title, header, text);
	}

	/**
	 * Show a error dialog to the user without a header section. See
	 * {@link #error(String, String, String)} to use a header as well.
	 * 
	 * @param title Dialog box title (window title)
	 * @param text  Shown in bigger text beside the error icon
	 */
	public static void error(String title, String text) {
		dialog(AlertType.ERROR, title, null, text);
	}

	/**
	 * The exception will be shown within a warning dialog with
	 * information:<br>
	 * 
	 * <pre>
	 * title  = Exception occured
	 * header = The exception that was thrown
	 * text   = Reason:
	 *          The exception message
	 * </pre>
	 * 
	 * @param exception Instance of the exception that was thrown
	 */
	public static void exception(Exception exception) {
		error("Exception occured", exception.getClass().getSimpleName(),
				"Reason:\n" + exception.getMessage());
	}
}
