package contactCRUD;

public class Contact {
	private int id;
	private String name;
	private String address;
	private String cellPhone;
	private String email;

	public Contact() {
	}
	public Contact(int id, String name, String address, String cellPhone, String email) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.cellPhone = cellPhone;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}