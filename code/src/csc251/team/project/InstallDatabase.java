package csc251.team.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InstallDatabase {
	public static void main(String[] args) {
		
		String createCarTableSQL = "CREATE TABLE IF NOT EXISTS Car ("
				+ "	vin CHAR(17) PRIMARY KEY,"
				+ "	year INT NOT NULL,"
				+ "	make VARCHAR(25) NOT NULL,"
				+ "	model VARCHAR(25) NOT NULL"
				+ ");";
		
		String createCarLotTableSQL = "CREATE TABLE IF NOT EXISTS CarLot ("
				+ "	purchaseIndex INT AUTO_INCREMENT PRIMARY KEY,"
				+ "	vin CHAR(17) NOT NULL,"
				+ "	mpg INT NOT NULL,"
				+ "	mileage INT NOT NULL,"
				+ "	cost DECIMAL(9, 2) NOT NULL,"
				+ "	salesPrice DECIMAL(9, 2) NOT NULL,"
				+ "	soldFor DECIMAL(9, 2),"
				+ "	profit DECIMAL(9,2) AS (soldFor - cost)"
				+ ");";
		
		try ( Connection connection = DbConnection.getConnection(); ) {			
			PreparedStatement psCar = connection.prepareStatement(createCarTableSQL);
			psCar.execute();
			psCar.close();
			
			PreparedStatement psCarLot = connection.prepareStatement(createCarLotTableSQL);
			psCarLot.execute();
			psCarLot.close();
			
			System.out.println("No errors reported.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
