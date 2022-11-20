package contactCRUD;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ContactView extends BorderPane {
	private TableView<Contact> tblContact;
	
	private TextField txtId;
	private TextField txtName;
	private TextField txtAddress;
	private TextField txtCellPhone;
	private TextField txtEmail;
	private Button btnAddNew;
	private Button btnDelete;
	private Button btnUpdate;

	public ContactView() {

		tblContact = new TableView<Contact>();
		ScrollPane pane = new ScrollPane(tblContact);
		pane.setMaxHeight(280);

		btnAddNew = new Button("Add New");
		btnDelete = new Button("Delete");
		btnUpdate = new Button("Update");

		HBox hBoxMiddle = new HBox(btnAddNew, btnDelete, btnUpdate);
		hBoxMiddle.setAlignment(Pos.TOP_CENTER);
		hBoxMiddle.getStyleClass().add("hbox");

		GridPane gridPaneBottom = new GridPane();
		gridPaneBottom.getStyleClass().add("pane");
		// gridPaneBottom.add(new Label("Id:"), 0, 0);
		gridPaneBottom.add(new Label("Name:"), 2, 0);
		gridPaneBottom.add(new Label("Address:"), 2, 1);
		gridPaneBottom.add(new Label("CellPhone:"), 4, 0);
		gridPaneBottom.add(new Label("Email:"), 4, 1);

		txtId = new TextField();
		txtId.setEditable(false);
		txtId.setVisible(false);
		txtName = new TextField();
		txtName.setPrefWidth(120);
		txtName.setMaxWidth(120);
		txtAddress = new TextField();
		txtAddress.setPrefWidth(120);
		txtAddress.setMaxWidth(120);
		//txtAddress.setAlignment(Pos.CENTER_RIGHT);
		txtCellPhone = new TextField();
		//txtCellPhone.setAlignment(Pos.CENTER_RIGHT);
		txtCellPhone.setPrefWidth(120);
		txtCellPhone.setMaxWidth(120);
		txtEmail = new TextField();
		txtEmail.setPrefWidth(180);
		txtEmail.setMaxWidth(180);

		gridPaneBottom.add(txtId, 1, 0);
		gridPaneBottom.add(txtName, 3, 0);
		gridPaneBottom.add(txtAddress, 3, 1);
		gridPaneBottom.add(txtCellPhone, 5, 0);
		gridPaneBottom.add(txtEmail, 5, 1);
		gridPaneBottom.setMinHeight(200);

		setTop(pane);
		setCenter(hBoxMiddle);
		setBottom(gridPaneBottom);
	}

	public Button getBtnDelete() {
		return btnDelete;
	}

	public Button getBtnUpdate() {
		return btnUpdate;
	}

	public Button getBtnAddNew() {
		return btnAddNew;
	}

	public TableView<Contact> getTblContact() {
		return tblContact;
	}

	public TextField getTxtId() {
		return txtId;
	}

	public TextField getTxtName() {
		return txtName;
	}

	public TextField getTxtAddress() {
		return txtAddress;
	}

	public TextField getTxtCellPhone() {
		return txtCellPhone;
	}

	public TextField getTxtEmail() {
		return txtEmail;
	}

	public void FormatTable() {

		tblContact.setEditable(true);

		TableColumn<Contact, Integer> idCol = new TableColumn<>("ID");
		idCol.setMinWidth(50);
		idCol.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("id"));
		// idCol.setCellFactory(TextFieldTableCell.forTableColumn(new
		// IntegerStringConverter()));

		TableColumn<Contact, String> nameCol = new TableColumn<>("Name");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("Name"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

		nameCol.setOnEditCommit(new EventHandler<CellEditEvent<Contact, String>>() {
			@Override
			public void handle(CellEditEvent<Contact, String> t) {
				((Contact) t.getTableView().getItems().get(t.getTablePosition()
						.getRow())).setName(t.getNewValue());
				Contact p = getTblContact().getSelectionModel().getSelectedItem();
				ContactDAO model = new ContactDAO();
				model.edit(p);
			}
		});

		TableColumn<Contact, String> addressCol = new TableColumn<>("Address");
		addressCol.setMinWidth(100);
		addressCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("Address"));
		addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
		addressCol.setOnEditCommit(new EventHandler<CellEditEvent<Contact, String>>() {
			@Override
			public void handle(CellEditEvent<Contact, String> t) {
				((Contact) t.getTableView().getItems().get(t.getTablePosition()
						.getRow())).setAddress(t.getNewValue());
				Contact p = getTblContact().getSelectionModel().getSelectedItem();
				ContactDAO model = new ContactDAO();
				model.edit(p);
			}
		});

		TableColumn<Contact, String> cellPhoneCol = new TableColumn<>("CellPhone");
		cellPhoneCol.setMinWidth(100);
		cellPhoneCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("CellPhone"));
		cellPhoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		cellPhoneCol.setOnEditCommit(new EventHandler<CellEditEvent<Contact, String>>() {
			@Override
			public void handle(CellEditEvent<Contact, String> t) {
				((Contact) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCellPhone(t.getNewValue());
				Contact p = getTblContact().getSelectionModel().getSelectedItem();
				ContactDAO model = new ContactDAO();
				model.edit(p);
			}
		});

		TableColumn<Contact, String> EmailCol = new TableColumn<>("Email");
		EmailCol.setMinWidth(260);
		EmailCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("Email"));
		EmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
		EmailCol.setOnEditCommit(new EventHandler<CellEditEvent<Contact, String>>() {
			@Override
			public void handle(CellEditEvent<Contact, String> t) {
				((Contact) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setEmail(t.getNewValue());
				Contact p = getTblContact().getSelectionModel().getSelectedItem();
				ContactDAO model = new ContactDAO();
				model.edit(p);
			}
		});
		tblContact.getColumns().add(0, idCol);
		tblContact.getColumns().add(1, nameCol);
		tblContact.getColumns().add(2, addressCol);
		tblContact.getColumns().add(3, cellPhoneCol);
		tblContact.getColumns().add(4, EmailCol);
	}
}