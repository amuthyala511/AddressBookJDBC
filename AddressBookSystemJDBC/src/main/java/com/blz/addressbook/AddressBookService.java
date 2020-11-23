package com.blz.addressbook;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public void addNewContact(String firstName, String lastName, String address, String city, String state, String zip, String phno, String emailId) throws AddressBookException {
		addressBookList. add(addressBookDBService.addNewContact(firstName, lastName, address, city, state, zip, phno, emailId));
	}
	
	public void addMultipleContacts(List<Person> contactsList) {
		Map<Integer, Boolean> addressAdditionStatus = new HashMap<Integer, Boolean>();
		contactsList.forEach(addressbookdata -> {
			Runnable task = () -> {
				addressAdditionStatus.put(addressbookdata.hashCode(), false);
				System.out.println("Contact Being Added: "+Thread.currentThread().getName());
				try {
					this.addNewContact(addressbookdata.getFirstName(), addressbookdata.getLastName(),
									addressbookdata.getAddress(), addressbookdata.getCity(), addressbookdata.getState(),
									addressbookdata.getZip(), addressbookdata.getPhNo(), addressbookdata.getEmailId());
				} catch (AddressBookException e) {
					e.printStackTrace();
				}
				addressAdditionStatus.put(addressbookdata.hashCode(), true);
				System.out.println("Contact Added: "+Thread.currentThread().getName());
			};
			 Thread thread = new Thread(task, addressbookdata.getFirstName());
			 thread.start();
		});
		while(addressAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				
			}
		}
		System.out.println(this.addressBookList);
	}
	
	public int countEntries(IOService dbIO) {
		return addressBookList.size();
	}
}
