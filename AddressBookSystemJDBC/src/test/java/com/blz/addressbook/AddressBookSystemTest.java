package com.blz.addressbook;

import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.blz.addressbook.AddressBookService.IOService;

public class AddressBookSystemTest {
	
	private static AddressBookService addressBookService;
	
	@BeforeClass
	public static void createAddressBookObj() {
		addressBookService = new AddressBookService();
		System.out.println("Welcome to the Address Book System");
	}
	
	@Test
	public void givenAddressBookDetails_WhenRetrieved_ShouldMachPersonsCount() throws AddressBookException {
		List<Person> list = addressBookService.readAddressBookData(IOService.DB_IO);
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void givenAddressBookDetails_WhenUpdated_ShouldSyncWithDB() throws AddressBookException {
		List<Person> data = addressBookService.readAddressBookData(IOService.DB_IO);
		addressBookService.updateDBRecord("Sana", "Ether");
		boolean result = addressBookService.checkUpdatedRecordSyncWithDatabase("Sana");
		Assert.assertEquals(true, result);
	}
	
	@Test
	public void givenAddressBookDetails_WhenRetrieved_ForGivenRange_ShouldMatchPersonsCount() throws AddressBookException {
		List<Person> list = addressBookService.readAddressBookData(IOService.DB_IO, "2020-11-01", "2020-11-22");
		Assert.assertEquals(2, list.size());
	}
}
