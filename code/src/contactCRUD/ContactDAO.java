package contactCRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
	// helper method to convert a ResultSet object returned from JDBC call to a Contact object.
	private Contact createContactPerson(ResultSet rs) {
		Contact p = new Contact();
		try {
			p.setId(rs.getInt("id"));
			p.setName(rs.getString("name"));
			p.setAddress(rs.getString("address"));
			p.setCellPhone(rs.getString("cellphone"));
			p.setEmail(rs.getString("email"));
		} catch (SQLException ex) {
		}
		return p;
	}

	public List<Contact> findAll() {
		String sql = "Select * from contact order by id";
		List<Contact> list = new ArrayList<>();
		try {
			Connection connection = DbConnection.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Contact p = createContactPerson(rs);
				list.add(p);
			}
			rs.close();
			connection.close();
		} catch (SQLException ex) {
		}
		return list;
	}

	public Contact find(int id) {
		try {
			Contact contact = null;
			PreparedStatement ps = DbConnection.getConnection().prepareStatement("SELECT * from Contact WHERE id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				contact = createContactPerson(rs);
			}
			return contact;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean delete(Contact contact) {
		// To be implemented by students
		// implemented by S. Andy Short
		try {
			Connection connection = DbConnection.getConnection();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("DELETE FROM Contact where id=" + Integer.toString(contact.getId()));
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean create(Contact contact) {
		// To be implemented by students
		// implemented by S. Andy Short
		try {
			Connection connection = DbConnection.getConnection();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(
					String.format("INSERT INTO Contact (name, address, cellphone, email) VALUES ('%1s', '%2s', '%3s', '%4s')",
					contact.getName(), contact.getAddress(), contact.getCellPhone(), contact.getEmail())
					);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean edit(Contact contact) {
		// To be implemented by students
		// implemented by S. Andy Short
		try {
			Connection connection = DbConnection.getConnection();
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(
					String.format("UPDATE Contact SET name='%1s', address='%2s', cellphone='%3s', email='%4s' WHERE id=%5d",
					contact.getName(), contact.getAddress(), contact.getCellPhone(), contact.getEmail(), contact.getId())
					);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}