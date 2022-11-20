module CSC251_JavaFX_and_MySQL_Project_Template {
	
	// JavaFX
	requires javafx.controls;
	requires javafx.graphics;
	
	// MySQL
	requires java.sql;
	requires java.sql.rowset;
	
	// This package uses JavaFX and MySQL
	opens contactCRUD to javafx.graphics, javafx.fxml, javafx.base;
}