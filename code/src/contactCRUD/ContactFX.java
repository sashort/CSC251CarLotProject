package contactCRUD;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class ContactFX extends Application {

	private ContactDAO model;
	private ContactView view;
	
	@Override
	public void start(Stage primaryStage) {
		model = new ContactDAO();
		view = new ContactView();

		view.FormatTable();
		view.getTblContact().setItems(FXCollections.observableArrayList(model.findAll()));
		
		view.getTblContact().setOnMouseClicked((MouseEvent event) -> {
		    if (event.getClickCount() > 0) {
			    if (view.getTblContact().getSelectionModel().getSelectedItem() != null) {
			        Contact c = view.getTblContact().getSelectionModel().getSelectedItem();
			        view.getTxtId().setText(String.valueOf(c.getId()));
			        view.getTxtName().setText(c.getName());
			        view.getTxtAddress().setText(c.getAddress());
			        view.getTxtCellPhone().setText(c.getCellPhone());
			        view.getTxtEmail().setText(c.getEmail());
			    }
		    }
		});
		view.getBtnDelete().setOnAction(e -> {
			int id = Integer.parseInt(view.getTxtId().getText());
			Contact contact = model.find(id);
			
			String selection = contact.getName();
			Alert alert = new Alert(AlertType.WARNING, "Delete " + selection + " ?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				model.delete(contact);
				view.getTblContact().setItems(FXCollections.observableArrayList(model.findAll()));
				view.getTxtId().setText("");
				view.getTxtName().setText("");
				view.getTxtAddress().setText("");
				view.getTxtEmail().setText("");
				view.getTxtCellPhone().setText("");
			}
		});
		view.getBtnAddNew().setOnAction(e -> {
			String name = view.getTxtName().getText();
			String address = view.getTxtAddress().getText();
			String cellPhone = view.getTxtCellPhone().getText();
			String email = view.getTxtEmail().getText();
			Contact contact = new Contact(0, name,address, cellPhone, email);
			if (model.create(contact)) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Add contact successfull", ButtonType.OK);
				alert.showAndWait();
				view.getTblContact().setItems(FXCollections.observableArrayList(model.findAll()));
			} else {
				Alert alert = new Alert(AlertType.WARNING, "Add contact failed", ButtonType.OK);
				alert.showAndWait();
			}
			view.getTxtId().setText("");
			view.getTxtName().setText("");
			view.getTxtAddress().setText("");
			view.getTxtEmail().setText("");
			view.getTxtCellPhone().setText("");
		});
		view.getBtnUpdate().setOnAction(e -> {
			int id = Integer.parseInt(view.getTxtId().getText());
			String name = view.getTxtName().getText();
			String address = view.getTxtAddress().getText();
			String cellPhone = view.getTxtCellPhone().getText();
			String email = view.getTxtEmail().getText();
			Contact contact = new Contact(id, name,address, cellPhone, email);
			if (model.edit(contact)) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Edit contact successfull", ButtonType.OK);
				alert.showAndWait();
				view.getTblContact().setItems(FXCollections.observableArrayList(model.findAll()));
			} else {
				Alert alert = new Alert(AlertType.WARNING, "Edit contact failed", ButtonType.OK);
				alert.showAndWait();
			}
			view.getTxtId().setText("");
			view.getTxtName().setText("");
			view.getTxtAddress().setText("");
			view.getTxtEmail().setText("");
			view.getTxtCellPhone().setText("");
		});

		
		primaryStage.setTitle("Address Book");
		Scene scene = new Scene(view, 650, 400, true);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Image myIcon = new Image(getClass().getResourceAsStream("waketech_icon.png"));
		primaryStage.getIcons().add(myIcon);
	
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}