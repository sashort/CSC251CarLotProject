package csc251.team.project;

import java.util.Scanner;

public class Car {
	private int purchaseIndex;
	private String vin;
	private int year;
	private String make;
	private String model;
	private int mileage;
	private int mpg;
	private double cost;
	private double salesPrice;
	private boolean sold;
	private double soldFor;
	private double profit;
	
	public Car() {
		this.setPurchaseIndex(-1);
		this.setVin(null);
		this.setYear(-1);
		this.setMake(null);
		this.setModel(null);
		this.setMileage(-1);
		this.setMpg(-1);
		this.setCost(-1);
		this.setSalesPrice(-1);
		this.setSold(false);
		this.setSoldFor(-1);
		this.setProfit(-1);
	}
	
	public Car(int purchaseIndex, String vin, int year, String make, String model, int mileage, int mpg, double cost, double salesPrice) {
		this();
		this.setPurchaseIndex(purchaseIndex);
		this.setVin(vin);
		this.setYear(year);
		this.setMake(make);
		this.setModel(model);
		this.setMileage(mileage);
		this.setMpg(mpg);
		this.setCost(cost);
		this.setSalesPrice(salesPrice);
	}
	
	public String toString() {
		
		String printable = String.format("Car: PurchaseIndex: %d,  %s, Mileage: %6d, MPG: %3d, Sold: %4s, Cost: $%7.2f, Selling price: $%7.2f",
				this.purchaseIndex, this.getId(), this.getMileage(), this.getMpg(), (this.isSold() ? "Yes" : " No"), this.getCost(), this.getSalesPrice());
		
		printable = printable + (this.isSold() ? 
				String.format(", Sold For $%7.2f, Profit: $%7.2f", this.getSoldFor(), this.getProfit()) : "");
		
		return printable;
	}
		
	public void sellCar(double priceSold) {
		this.setSold(true);
		this.setSoldFor(priceSold);
		this.setProfit(this.getSoldFor()-this.getCost());
	}
		
	public String getId() {
		if (vin == null)
			return null;
		return vin + ' ' + year + ' ' + make + ' ' + model; 
	}
	
	public int getPurchaseIndex() { return purchaseIndex; }
	
	public void setPurchaseIndex( int purchaseIndex ) {
		if (this.purchaseIndex > 0)
			throw new UnsupportedOperationException("Vehicle purchase index cannot be changed once it has been initialized.");
		this.purchaseIndex = purchaseIndex;
	}
	
	public String getVin() { return vin; }
	
	public void setVin(String vin) {
		if (this.vin != null)
			throw new UnsupportedOperationException("Vehicle VIN cannot be changed once it has been initialized.");
		this.vin = vin;
	}
	
	public int getYear() { return year; }
	
	public void setYear(int year) { this.year = year; }
	
	public String getMake() { return make; }
	
	public void setMake(String make) { this.make = make; }
	
	public String getModel() { return model; }
	
	public void setModel(String model) { this.model = model; }
	
	public int getMileage() { return mileage; }
	
	public void setMileage(int mileage) { this.mileage = mileage; }
	
	public int getMpg() { return mpg; }
	
	public void setMpg(int mpg) { this.mpg = mpg; }
	
	public double getCost() { return cost; }
	
	public void setCost(double cost) { this.cost = cost; } 
	
	public double getSalesPrice() { return salesPrice; }
	
	public void setSalesPrice(double salesPrice) { this.salesPrice = salesPrice; }
	
	public boolean isSold() { return sold; }
	
	public void setSold(boolean sold) { this.sold = sold; }
	
	public double getSoldFor() { return soldFor; }
	
	public void setSoldFor(double soldFor) { this.soldFor = soldFor; }
	
	public double getProfit() { return profit; }
	
	public void setProfit(double profit) { this.profit = profit; } 
	
	public int getState() {
		if (isSold())
			return State.SOLD;
		if (getPurchaseIndex() < 1)
			return State.UNPURCHASED;
		return State.IN_STOCK;
	}
	
	public static final class State {
		public static final int UNPURCHASED = 1;
		public static final int IN_STOCK = 2;
		public static final int SOLD = 4;
	}
}