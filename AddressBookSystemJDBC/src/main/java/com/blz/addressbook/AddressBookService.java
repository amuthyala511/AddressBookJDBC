package com.blz.addressbook;

import java.time.LocalDate;
import java.util.List;

public class AddressBookService {
	public enum IOService {
		DB_IO
	}

	private List<Person> addressBookList;
	private static AddressBookDBService addressBookDBService;

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}

	public List<Person> readAddressBookData(IOService ioService) throws AddressBookException {
		if (ioService.equals(IOService.DB_IO))
			this.addressBookList = addressBookDBService.readData();
		return this.addressBookList;
	}
	
	public void updateDBRecord(String firstName, String address) throws AddressBookException {
		int result = addressBookDBService.updateAddressBookData(firstName, address);
		if (result == 0)
			return;
		Person addressBookData = this.getAddressBookData(firstName);
		if (addressBookData != null)
			addressBookData.address = address;
	}

	private Person getAddressBookData(String firstName) {
		return this.addressBookList.stream().filter(n -> n.firstName.equals(firstName))
				.findFirst().orElse(null);
	}

	public boolean checkUpdatedRecordSyncWithDatabase(String firstName) throws AddressBookException {
		try {
			List<Person> addressBookData = addressBookDBService.getAddressBookData(firstName);
			return addressBookData.get(0).equals(getAddressBookData(firstName));
		} catch (AddressBookException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
		}
	}
	
	public List<Person> readAddressBookData(IOService ioService, String start, String end) throws AddressBookException {
		try {
			LocalDate startDate = LocalDate.parse(start);
			LocalDate endDate = LocalDate.parse(end);
			if(ioService.equals(IOService.DB_IO))
				return addressBookDBService.readData(startDate, endDate);
			return this.addressBookList;
		} catch (AddressBookException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
		}
	}
	
	public int readAddressBookData(String function, String city) throws AddressBookException {
		return addressBookDBService.readDataFromAddressBook(function, city);
	}
}
