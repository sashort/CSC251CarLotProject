package csc251.team.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UninstallDatabase {
	public static void main(String[] args) {
		
		String sql1 = "DROP TABLE CarLot";
		String sql2 = "DROP TABLE Car";
		
		try ( Connection connection = DbConnection.getConnection(); ) {	
			
			PreparedStatement psCarLot = connection.prepareStatement(sql1);
			psCarLot.execute();
			psCarLot.close();
			
			PreparedStatement psCar = connection.prepareStatement(sql2);
			psCar.execute();
			psCar.close();
		
			
			System.out.println("No errors reported.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
