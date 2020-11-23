package com.blz.addressbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	
	private static List<Person> addressBookData = new ArrayList<>();
	private PreparedStatement addressBookPreparedStatement;
	private static AddressBookDBService addressBookDBService;
	
	public static AddressBookDBService getInstance() {
		if(addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}
	
	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/AddressBookSystem?useSSL=false";
		String userName = "root";
		String password = "1234";
		Connection con;
		System.out.println("Connecting to database: "+jdbcURL);
		con = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is successful: "+con);
		return con;
	}
	
	public List<Person> readData() throws AddressBookException {
		String query = null;
		query = "select * from addressbook";
		return getAddressBookDataUsingDatabase(query);
	}

	private List<Person> getAddressBookDataUsingDatabase(String query) throws AddressBookException {
		try (Connection con = this.getConnection();) {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			addressBookData = this.getAddressBookDetails(rs);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.ConnectionFailed);
		}
		return addressBookData;
	}

	private List<Person> getAddressBookDetails(ResultSet rs) throws AddressBookException {
		try {
			while(rs.next()) {
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				String address = rs.getString("Address");
				String city = rs.getString("City");
				String state = rs.getString("State");
				String zip = rs.getString("Zip");
				String phNo = rs.getString("PhoneNumber");
				String emailId = rs.getString("Email");
				addressBookData.add(new Person(firstName, lastName, address, city, state, zip, phNo, emailId));
			}
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
		}
		return addressBookData;
	}
	
	public int updateAddressBookData(String firstName, String address) throws AddressBookException {
		try (Connection connection = this.getConnection()) {
			String query = String.format("update addressbook set Address = '%s' where FirstName = '%s';", address, firstName);
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			return preparedStatement.executeUpdate(query);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.ConnectionFailed);
		}
	}
	
	public List<Person> getAddressBookData(String firstName) throws AddressBookException {
		if (this.addressBookPreparedStatement == null)
			this.prepareAddressBookStatement();
		try {
			addressBookPreparedStatement.setString(1, firstName);
			ResultSet resultSet = addressBookPreparedStatement.executeQuery();
			addressBookData = this.getAddressBookDetails(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.ConnectionFailed);
		}
		System.out.println(addressBookData);
		return addressBookData;
	}

	private void prepareAddressBookStatement() throws AddressBookException {
		try {
			Connection connection = this.getConnection();
			String query = "select * from addressbook where FirstName = ?";
			addressBookPreparedStatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
		}
	}
	
	public List<Person> readData(LocalDate start, LocalDate end) throws AddressBookException {
		String query = null;
		if (start != null)
			query = String.format("select * from addressbook where startdate between '%s' and '%s';", start, end);
		if (start == null)
			query = "select * from addressbook";
		List<Person> addressBookList = new ArrayList<>();
		try (Connection con = this.getConnection();) {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			addressBookList = this.getAddressBookDetails(rs);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
		}
		return addressBookList;
	}
	
	public int readDataFromAddressBook(String count, String city) throws AddressBookException {
		int noOfContacts = 0;
		String query = String.format("select %s(state) from addressbook where city = '%s' group by city;", count, city);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			noOfContacts = rs.getInt(1);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
		}
		return noOfContacts;
	}
	
	public Person addNewContact(String firstName, String lastName, String address, String city, String state, String zip, String phno, String emailId) throws AddressBookException {
		int personId = -1;
		Person personData = null;
		String query = String.format("insert into addressbook(FirstName, LastName, Address, City, State, Zip, PhoneNumber, Email) values"
				+ "('%s', '%s', '%s', '%s', '%s','%s', '%s', '%s')",
				firstName, lastName, address, city, state, zip, phno, emailId);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					personId = resultSet.getInt(1);
			}
			personData = new Person(firstName, lastName, address, city, state, zip, phno, emailId);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
		}
		return personData;
	}
}
