package scratch;

import java.nio.charset.Charset;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CharsetCoders extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		Application.setUserAgentStylesheet("com/sun/javafx/scene/control/skin/caspian/caspian.css");
		VBox allContent = new VBox();
		HBox encode = new HBox();
		TextField[] encodings = new TextField[2];
		Label label = new Label("Encode To: ");
		ComboBox<Charset> encodeComboBox = new ComboBox<>(FXCollections.observableArrayList(Charset.availableCharsets()
				.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.filter(Charset::canEncode)
				.filter(Charset::isRegistered)
				.toArray(Charset[]::new)));
		encodings[0] = new TextField();
		encodings[1] = new TextField();
		encodings[0].textProperty()
				.addListener((observable, oldValue, newValue) -> {
					resize(primaryStage, encodings[0], newValue);
					encode(newValue, encodeComboBox.getValue() != null ? encodeComboBox.getValue() : encodeComboBox.getItems().get(0), encodings[1]);
					System.gc();
				});
		encodings[1].textProperty()
				.addListener((observable, oldValue, newValue) -> resize(primaryStage, encodings[1], newValue));
		encode.getChildren()
				.setAll(encodings[0], label, encodeComboBox, encodings[1]);
		encode.setAlignment(Pos.CENTER);
		HBox decode = new HBox();
		TextField[] decodings = new TextField[2];
		Label decodeLabel = new Label("Decode From: ");
		ComboBox<Charset> decodeComboBox = new ComboBox<>(FXCollections.observableArrayList(Charset.availableCharsets()
				.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.filter(Charset::isRegistered)
				.toArray(Charset[]::new)));
		decodings[0] = new TextField();
		decodings[1] = new TextField();
		decodings[0].textProperty()
				.addListener((observable, oldValue, newValue) -> {
					resize(primaryStage, decodings[0], newValue);
					decode(newValue, decodeComboBox.getValue() != null ? decodeComboBox.getValue() : decodeComboBox.getItems().get(0), decodings[1]);
					System.gc();
				});
		decodings[1].textProperty()
				.addListener((observable, oldValue, newValue) -> resize(primaryStage, decodings[1], newValue));
		decode.getChildren()
				.setAll(decodings[0], decodeLabel, decodeComboBox, decodings[1]);
		decode.setAlignment(Pos.CENTER);
		allContent.getChildren().setAll(encode, decode);
		allContent.setAlignment(Pos.TOP_CENTER);
		primaryStage.setScene(new Scene(allContent));
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	private void resize(Stage primaryStage, TextField textField, String newValue) {
		textField.setPrefColumnCount(newValue.length() > TextField.DEFAULT_PREF_COLUMN_COUNT ? newValue.length() : TextField.DEFAULT_PREF_COLUMN_COUNT);
		primaryStage.sizeToScene();
	}
	
	private void encode(String source, Charset charset, TextField destination) {
		destination.setText(new String(source.getBytes(), charset));
	}
	
	private void decode(String source, Charset charset, TextField destination) {
		destination.setText(new String(source.getBytes(charset)));
	}
}