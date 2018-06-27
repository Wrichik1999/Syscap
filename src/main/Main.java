package main;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("System Capabilities");
		primaryStage.setScene(new Scene(new TabPane(charsets()), Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, true, SceneAntialiasing.BALANCED));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	
	private Tab charsets() {
		Tab tab = new Tab("Character Sets");
		VBox tabContent = new VBox();
		ScrollPane pane = new ScrollPane();
		pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		Button refresh = new Button("Refresh character sets");
		Accordion accordion = new Accordion();
		accordion.getPanes().setAll(getCharsetPanes());
		refresh.setOnAction(event -> {
			accordion.getPanes().setAll(getCharsetPanes());
			System.gc();
		});
		pane.setContent(accordion);
		pane.setFitToWidth(true);
		tabContent.getChildren()
				.setAll(refresh, pane);
		tabContent.setAlignment(Pos.TOP_CENTER);
		tab.setClosable(false);
		tab.setContent(tabContent);
		System.gc();
		return tab;
	}
	
	private TitledPane[] getCharsetPanes() {
		return Charset.availableCharsets()
				.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.flatMap(charset -> Stream.concat(Stream.of(charset), charset.aliases().stream().map(Charset::forName)))
				.distinct()
				.sorted(Comparator.comparing(Charset::toString))
				.filter(charset -> Charset.isSupported(charset.name()))
				.map(entry -> new TitledPane(entry.toString(), new Button(entry.toString())))
				.toArray(TitledPane[]::new);
	}
}