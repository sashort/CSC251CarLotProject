/*
 * S. Andy Short
 * 11/22/22
 */
package csc251.team.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarLotDAO {
	
	                                                  // |------1------|-----2----|---4--|
	public static final class FilterKeys {            // | UNPURCHASED | IN-STOCK | SOLD |
                                                      // |-------------|----------|------|
		public static final int IN_STOCK = 2;         // |             |     X    |      |
		public static final int PURCHASED = 6;        // |             |     X    |   X  |
		public static final int SOLD = 4;             // |             |          |   X  |
		public static final int NOT_IN_STOCK = 5;     // |      X      |          |   X  |
		public static final int UNPURCHASED = 1;      // |      X      |          |      |
		public static final int UNSOLD = 3;           // |      X      |     X    |      |
		public static final int ANY = 7;              // |      X      |     X    |   X  |
                                                      // |-------------|----------|------|
		public static final int ALLOW_DUPLICATES = 8; // For when you've purchased the same car more than once.
		                                              // If false, will only return the car with with the highest
		                                              // purchaseIndex for each matching VIN.

	}
	
	
	// NOTE: The returned list is sorted by: Make, Model, Year, and purchaseIndex descending.
	public List<Car> getCars(int filterKey, String searchString) {
		List<Car> list = new ArrayList<>();
		try ( Connection connection = DbConnection.getConnection(); ) {
			
			// search for matches in the Car table
			String broadSQL = "SELECT * FROM Car" + 
			(searchString == null ? "":  " WHERE CONCAT(vin, ' ', year, ' ', make, ' ', model) LIKE ?") + 
			" ORDER BY make, model, year";
			
			PreparedStatement psBroad = connection.prepareStatement(broadSQL);
			
			if (searchString != null)
				psBroad.setString(1, '%' + searchString.replace(' ','%') + '%');
			
			ResultSet rsBroad = psBroad.executeQuery();
			
			
			
			// Extract the matching VINS from the search
			List<String> vins = new ArrayList<>();
			String likeStr = "";
			while (rsBroad.next()) {
				likeStr += ", '" + rsBroad.getString("vin") + "'";
				vins.add(rsBroad.getString("vin"));
			}
			// No cars were found with the search criteria
			if (likeStr.compareTo("") == 0)
				return list;
			
			// MySql result sets can't be scrolled. Have to close the result set
			// and reissue the SQL command to reset cursor
			rsBroad.close();
			rsBroad = psBroad.executeQuery();
			
			
			
			
			// generate query to find all cars in-stock or sold using the matched VINs and search... Found in CarLot table.
			String specificSQL = "SELECT CarLot.*, Car.year, Car.make, Car.model FROM CarLot JOIN Car ON CarLot.vin = Car.vin " +
			                     "WHERE CarLot.vin IN ( " + likeStr.substring(2) + ") " + 
			                     "ORDER BY Car.make, Car.model, Car.year, CarLot.purchaseIndex DESC";
			
			PreparedStatement psSpecific = connection.prepareStatement(specificSQL);
			ResultSet rsSpecific = psSpecific.executeQuery();
			
			
			
			// initialize the broad variables for the zigzag search.
			rsBroad.next();
			String broadVin = rsBroad.getString("vin");
			String specificVin = (rsSpecific.next() ? rsSpecific.getString("vin") : "");
			int purchasedCount = 0;
			boolean canAdd = true;
			
			
			//zigzag through the 2 result sets and add cars if allowed by filter
			do {
				// if the broad and specific VINs don't match
				if (!broadVin.equals(specificVin)) {
					// if you've never purchased the broad car and the filter allows unpurchased vehicles, add to list.
					if (purchasedCount == 0 && ((filterKey & FilterKeys.UNPURCHASED) == FilterKeys.UNPURCHASED))
						list.add(createCar(rsBroad));
					
					// move to the next broad record and reset the broad variables
					rsBroad.next();
					broadVin = rsBroad.getString("vin");
					purchasedCount = 0;
					canAdd = true;
				}
				else {
					purchasedCount++;
					// if you haven't added the specific vehicle to the list, or duplicates are allowed.
					if (canAdd) {
						rsSpecific.getDouble("profit");
						// if the specific car is in stock and the filter allows in-stock vehicles, add to list,
						// and reevaluate if you can add any more records matching the specific VIN.
						if (rsSpecific.wasNull() && (filterKey & FilterKeys.IN_STOCK) == FilterKeys.IN_STOCK) {
							list.add(createCar(rsSpecific));
							canAdd = (filterKey & FilterKeys.ALLOW_DUPLICATES) == FilterKeys.ALLOW_DUPLICATES;
						}
						// if the specific car is sold and the filter allows sold vehicles, add to list,
						// and reevaluate if you can add any more records matching the specific VIN.
						else if (!rsSpecific.wasNull() && (filterKey & FilterKeys.SOLD) == FilterKeys.SOLD) {
							list.add(createCar(rsSpecific));
							canAdd = (filterKey & FilterKeys.ALLOW_DUPLICATES) == FilterKeys.ALLOW_DUPLICATES;
						}
					}
					
					// has the search ended?
					if (!rsSpecific.next())
						break;
					
					// if not, set the specific Vin to the next specific record's vin.
					specificVin = rsSpecific.getString("vin");
				}
			} while (true);
			
			// close result set connections
			rsBroad.close();
			rsSpecific.close();
		} catch (SQLException ex) {}
		return list;
	}
	
	
	// purchase a car when the vin, year, make, and model already cached in Car table.
	public boolean purchaseCar(String vin, int mileage, int mpg, double cost, double salesPrice) {
		List<Car> cars = getCars(FilterKeys.NOT_IN_STOCK, vin);
		if (cars.size() == 0 || mileage < 0 || mpg < 0 || cost < 0 || salesPrice < 0)
			return false;
		Car car = cars.get(0);
		
		try ( Connection connection = DbConnection.getConnection(); ) {
			String sql = String.format("INSERT INTO CarLot (purchaseIndex, vin, mileage, mpg, cost, salesPrice) VALUES (NULL, '%s', %d, %d, %f, %f)",
					vin, mileage, mpg, cost,salesPrice);			
			
			Statement statement = connection.createStatement();		
			return statement.executeUpdate(sql) > 0;
		} catch (SQLException e) {
			return false;
		}
	}
	
	// purchase a car for the first time and car info not cached in Car table.
	public boolean purchaseCar(String vin, int year, String make, String model, int mileage, int mpg, double cost, double salesPrice) {
		List<Car> cars = getCars(FilterKeys.ANY, vin);
		if (cars.size() > 0 || year < 1600 || make == null || model == null || mileage < 0 || mpg < 0 || cost < 0 || salesPrice < 0)
			return false;
		
		try ( Connection connection = DbConnection.getConnection(); ) {
			String sql = String.format("INSERT INTO Car (vin, year, make, model) VALUES ('%s', %d, '%s', '%s')",
					vin, year, make, model);			
			
			Statement statement = connection.createStatement();					
			if (statement.executeUpdate(sql) > 0)
				return purchaseCar(vin, mileage, mpg, cost, salesPrice);
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean sellCar(String vin, double soldFor) {
		List<Car> cars = getCars(FilterKeys.IN_STOCK, vin);
		if (cars.size() != 1) {
			return false;
		}
		try ( Connection connection = DbConnection.getConnection(); ) {
			String sql = String.format("UPDATE CarLot SET soldFor = %f WHERE vin = '%s'", soldFor, vin);			

			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql) > 1;
		} catch (SQLException e) {
			return false;
		}
		
		
	}
	
	public boolean cacheCarInfo(String vin, int year, String make, String model) {
		try ( Connection connection = DbConnection.getConnection(); ) {
			String sql = String.format("INSERT INTO Car (vin, year, make, model) VALUES ('%s', %d, '%s', '%s')",
				vin, year, make, model);	 
					
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql) > 1;
		
		} catch (SQLException e) {
			return false;
		}
	}
	
	public double getAverageMileage() {
		try ( Connection connection = DbConnection.getConnection(); ) {
			// AVG function in MySql returns a big decimal.
			// A solution was not readily found to overcome this behavior.
			
			String sql = "SELECT mileage FROM CarLot WHERE soldFor IS NULL";
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			double sum = 0;
			int count = 0;
			while (rs.next()) {
				sum += rs.getInt("mileage");
				count += 1;
			}
			return sum / count;
		
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public double getAverageMpg() {
		try ( Connection connection = DbConnection.getConnection(); ) {
			// AVG function in MySql returns a big decimal.
			// A solution was not readily found to overcome this behavior.
			
			String sql = "SELECT mpg FROM CarLot WHERE soldFor IS NULL";
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			double sum = 0;
			int count = 0;
			while (rs.next()) {
				sum += rs.getInt("mpg");
				count += 1;
			}
			return sum / count;
		
		} catch (SQLException e) {
			return -1;
		}
	}
	
	private Car createCar(ResultSet rs) {
		Car c = new Car();
		try {
			c.setVin(rs.getString("vin"));
			c.setYear(rs.getInt("year"));
			c.setMake(rs.getString("make"));
			c.setModel(rs.getString("model"));
			
			// If result set is from the Car table, the following columns won't exist
			try {
				c.setPurchaseIndex(rs.getInt("purchaseIndex"));
				c.setMileage(rs.getInt("mileage"));
				c.setMpg(rs.getInt("mpg"));
				c.setCost(rs.getDouble("cost"));
				c.setSalesPrice(rs.getDouble("salesPrice"));
				rs.getDouble("soldFor");
				if (!rs.wasNull())
					c.sellCar(rs.getDouble("soldFor"));
			}
			
			catch (Exception ex) {}
		} catch (SQLException ex) {}
		return c;
	}
}