package com.blz.addressbook;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddressBookSystemTest {
	
	private static AddressBook addressBook;
	
	@BeforeClass
	public static void createAddressBookObj() {
		addressBook = new AddressBook();
		System.out.println("Welcome to the Address Book System");
	}
	
	@Test
	public void givenAddressBookDetails_WhenRetrieved_ShouldMachPersonsCount() throws AddressBookException, SQLException {
		List<Person> list = addressBook.readData();
		Assert.assertEquals(2, list.size());
	}
}
