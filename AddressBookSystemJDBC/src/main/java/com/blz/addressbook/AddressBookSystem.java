package com.blz.addressbook;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AddressBookSystem {
	static Scanner sc = new Scanner(System.in);
	static AddressBookSystem obj = new AddressBookSystem();
	static Map<String, AddressBookSystem> adBookObj = new HashMap<>();
	static String addressBookName;

	public void addAddressBook() throws IOException {
		int option = 0;
		System.out.println("Enter choice...\n1. Create new AddressBook\n2. Add new Persons to existing AddressBook");
		int choice = sc.nextInt();
		if (choice > option) {
			switch (choice) {
			case 1:
				System.out.println("Enter AddressBook Name: ");
				String newAddressBook = sc.next();
				if (adBookObj.containsKey(newAddressBook)) {
					System.out.println("This AddressBook already exists!!");
					break;
				} else {
					adBookObj.put(newAddressBook, obj);
					System.out.println("AddressBook " + newAddressBook + " successfully added");
					AddressBookSystem.personData();
					break;
				}
			case 2:
				System.out.println("Enter AddressBook Name: ");
				String existingAddressBook = sc.next();
				if (adBookObj.containsKey(existingAddressBook)) {
					adBookObj.get(existingAddressBook);
					AddressBookSystem.personData();
					break;
				} else {
					System.out.println("AddressBook not found!!");
					break;
				}
			default:
				System.out.println("Enter valid choice");
				break;
			}
		}
	}

	public static void personData() throws IOException {
		AddressBook addressBook = new AddressBook();
		int count = 1;
		while (count == 1) {
			System.out.println("Welcome to Address Book Program in AddressBook Main Class");
			System.out.println(
					"Enter choice...\n1. Add new contact\n2. Edit existing details\n3. Delete existing contact\n4. Search"
							+ "\n5. View Person\n6. Count of People\n7. Sort by first name\n8. Sort by city name\n9. Add contact to File"
							+ "\n10. Read contact data from File\n11. Add contact to CSV File\n12. Read contact data from CSV File\n13. Exit");
			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				addressBook.addContact(addressBookName);
				break;
			case 2:
				if (AddressBook.list.isEmpty()) {
					System.out.println("Address Book is empty..!!");
					break;
				} else {
					addressBook.editDetails();
					break;
				}
			case 3:
				if (AddressBook.list.isEmpty()) {
					System.out.println("Address Book is empty..!!");
					break;
				} else {
					addressBook.deleteDetails();
					break;
				}
			case 4:
				if (AddressBook.list.isEmpty()) {
					System.out.println("Address Book is empty..!!");
					break;
				} else {
					addressBook.searchByCity();
					break;
				}
			case 5:
				if (AddressBook.list.isEmpty()) {
					System.out.println("Address Book is empty..!!");
					break;
				} else {
					addressBook.viewByCity();
					break;
				}
			case 6:
				if (AddressBook.list.isEmpty()) {
					System.out.println("Address Book is empty..!!");
					break;
				} else {
					addressBook.countBasedOnCity();
					break;
				}
			case 7:
				if (AddressBook.list.isEmpty()) {
					System.out.println("Address Book is empty..!!");
					break;
				} else {
					addressBook.sortingByName();
					break;
				}
			case 8:
				if (AddressBook.list.isEmpty()) {
					System.out.println("Address Book is empty..!!");
					break;
				} else {
					addressBook.sortingByCity();
					break;
				}
			case 9:
				addressBook.addDataToFile(addressBookName);
				System.out.println("Details added to text file");
				break;
			case 10:
				addressBook.readDataFromFile();
				break;
			case 11:
				addressBook.addDataToCSVFile(addressBookName);
				System.out.println("Data added to CSV File");
				break;
			case 12:
				addressBook.readDataFromCSVFile();
				break;
			case 13:
				obj.addAddressBook();
				count = 0;
				break;
			default:
				System.out.println("Enter a valid choice");
				break;
			}
		}
		System.out.println(addressBook.list);
	}

	public static void main(String[] args) throws IOException {
		personData();
	}
}
