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
				this.getPurchaseIndex(), this.getId(), this.getMileage(), this.getMpg(), (this.getState() == State.SOLD ? "Yes" : " No"), this.getCost(), this.getSalesPrice());
		
		printable = printable + (this.getState() == State.SOLD ? 
				String.format(", Sold For $%7.2f, Profit: $%7.2f", this.getSoldFor(), this.getProfit()) : "");
		
		return printable;
	}
		
	public void sellCar(double priceSold) {
		this.setSoldFor(priceSold);
		this.setProfit(this.getSoldFor()-this.getCost());
	}
		
	public String getId() {
		if (vin == null)
			return null;
		return vin + ' ' + year + ' ' + make + ' ' + model; 
	}
	
	public Integer getPurchaseIndex() { return (getState() == State.UNPURCHASED) ? null : purchaseIndex; }
	
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
	
	public Integer getMileage() { return (getState() == State.UNPURCHASED) ? null : mileage; }
	
	public void setMileage(int mileage) { this.mileage = mileage; }
	
	public Integer getMpg() { return (getState() == State.UNPURCHASED) ? null : mpg; }
	
	public void setMpg(int mpg) { this.mpg = mpg; }
	
	public Double getCost() { return (getState() == State.UNPURCHASED) ? null : cost; }
	
	public void setCost(double cost) { this.cost = cost; } 
	
	public Double getSalesPrice() { return (getState() == State.UNPURCHASED) ? null : salesPrice; }
	
	public void setSalesPrice(double salesPrice) { this.salesPrice = salesPrice; }
			
	public Double getSoldFor() { return (getState() == State.SOLD) ? soldFor : null; }
	
	public void setSoldFor(double soldFor) { this.soldFor = soldFor; }
	
	public Double getProfit() { return (getState() == State.SOLD) ? profit : null; }
	
	public void setProfit(double profit) { this.profit = profit; } 
	
	public int getState() {
		if (purchaseIndex < 1)
			return State.UNPURCHASED;
		if (soldFor < 0)
			return State.IN_STOCK;
		return State.SOLD;
	}
	
	public static final class State {
		public static final int UNPURCHASED = 1;
		public static final int IN_STOCK = 2;
		public static final int SOLD = 4;
	}
}
