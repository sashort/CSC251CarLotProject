package csc251.team.project;

import java.util.List;

import csc251.team.project.CarLotDAO.FilterKeys;

public class CarLotDAOtest {
	
	
	public static void main(String[] args) {
		//reset the database
		UninstallDatabase.main(args);
		InstallDatabase.main(args);
		
		CarLotDAO dao = new CarLotDAO();
		
		dao.purchaseCar("AAAAAAAAAAAAAAAAA", 2001, "Mercury", "Grande Marquis", 70000, 22, 32500, 33000);
		dao.purchaseCar("DDDDDDDDDDDDDDDDD", 1886, "Goldie",  "Oldie", 887000, 4, 1986400, 2374500);
		dao.cacheCarInfo("BBBBBBBBBBBBBBBBB", 1999, "Honda", "Civic");
		dao.purchaseCar("BBBBBBBBBBBBBBBBB", 90000, 19, 7600, 8000);
		dao.sellCar("BBBBBBBBBBBBBBBBB",8250);
		dao.purchaseCar("BBBBBBBBBBBBBBBBB", 555555, 12, 950, 1000);
		dao.sellCar("BBBBBBBBBBBBBBBBB",1250);
		dao.cacheCarInfo("CCCCCCCCCCCCCCCCC", 1982, "AMC", "Gremlin");
		
		System.out.println("Average MPG: " + dao.getAverageMpg());
		System.out.println("Average Mileage: " + dao.getAverageMileage());
		for (int i = 1; i < 16; i++) {
			List<Car> cars = dao.getCars(i, null);
			System.out.println("========== " + i + " ========== ");
			for (Car c : cars) {
				System.out.println(c.toString());
			}
		}
		
	}
}
