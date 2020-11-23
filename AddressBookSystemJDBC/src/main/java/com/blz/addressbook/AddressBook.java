package com.blz.addressbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddressBook {
	// UC1
	static Scanner sc = new Scanner(System.in);
	static List<Person> list = new ArrayList<Person>();
	static Person person;

	// UC2
	public void addContact(String addressBookName) {
		System.out.println("Enter First Name: ");
		String firstName = sc.next();
		System.out.println("Enter Last Name: ");
		String lastName = sc.next();
		System.out.println("Enter Address: ");
		String address = sc.next();
		System.out.println("Enter City Name: ");
		String city = sc.next();
		System.out.println("Enter State Name: ");
		String state = sc.next();
		System.out.println("Enter Zip code: ");
		String zip = sc.next();
		System.out.println("Enter Phone Number: ");
		String phno = sc.next();
		System.out.println("Enter email address: ");
		String emailId = sc.next();
		if (list.size() > 0) {
			for (Person personList : list) {
				person = personList;
				if (firstName.equals(person.firstName)) {
					System.out.println("Person with name : " + person.firstName + " already exists!!!");
					break;
				}
			}
		} else {
			person = new Person(firstName, lastName, address, city, state, zip, phno, emailId);
			list.add(person);
			addDataToFile(addressBookName);
			try {
				addDataToCSVFile(addressBookName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				addDataToJSONFile(addressBookName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// UC3
	public void editDetails() {
			System.out.println("Enter first name: ");
			String fname = sc.next();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFirstName().equals(fname)) {
					System.out.println(list.get(i));
					@SuppressWarnings("resource")
					System.out.println("Enter your choice to edit....\n1. FirstName\n2. LastName\n3. Address\n4. City\n5. State\n6. Zipcode\n7. PhoneNumber\n8. Email\n");
					int choice = sc.nextInt();
					switch (choice) {
					case 1:
						System.out.println("Enter new FirstName: ");
						String new_first_name = sc.next();
						list.get(i).setFirstName(new_first_name);
						System.out.println(list.get(i).getFirstName());
						break;
					case 2:
						System.out.println("Enter new LastName: ");
						String new_last_name = sc.next();
						list.get(i).setLastName(new_last_name);
						System.out.println(list.get(i).getLastName());
						break;
					case 3:
						System.out.println("Enter new Address: ");
						String new_address = sc.next();
						list.get(i).setAddress(new_address);
						System.out.println(list.get(i).getAddress());
						break;
					case 4:
						System.out.println("Enter new City Name: ");
						String new_city = sc.next();
						list.get(i).setCity(new_city);
						System.out.println(list.get(i).getCity());
						break;
					case 5:
						System.out.println("Enter new State Name: ");
						String new_state = sc.next();
						list.get(i).setState(new_state);
						System.out.println(list.get(i).getState());
						break;
					case 6:
						System.out.println("Enter new Zip code: ");
						String new_zip = sc.next();
						list.get(i).setZip(new_zip);
						System.out.println(list.get(i).getZip());
						break;
					case 7:
						System.out.println("Enter new Phone Number: ");
						String new_phno = sc.next();
						list.get(i).setPhNo(new_phno);
						System.out.println(list.get(i).getPhNo());
						break;
					case 8:
						System.out.println("Enter new email Id: ");
						String new_emailId = sc.next();
						list.get(i).setEmailId(new_emailId);
						System.out.println(list.get(i).getEmailId());
						break;
					default:
						System.out.println("Enter a valid choice");
						break;
					}
				}
			}
			System.out.println(list);
		}

	// UC4
	public void deleteDetails() {
		System.out.println("Enter first name: ");
		String FirstName = sc.next();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getFirstName().equalsIgnoreCase(FirstName)) {
				list.remove(i);
			} else {
				System.out.println("No matches found...");
			}
		}
	}

	// UC5
	public void addPerson() {
			System.out.println("Enter number of new persons to be added: ");
			int noOfPersons = sc.nextInt();
			int count = 1;
			while(count <= noOfPersons) {
				addContact(addressBookName);
				count++;
			}
		}

	// UC8
	public void searchByCity() {
			System.out.println("Enter city name: ");
			String City = sc.next();
			list.stream().filter(n -> n.getCity().equals(City)).forEach(i -> System.out.println("Details found: "+i.getFirstName()));
		}
		
		//UC9
		public void viewByCity() {
			System.out.println("Enter city name: ");
			String City = sc.next();
			list.stream().filter(n -> n.getCity().equals(City)).forEach(i -> System.out.println(i));
		}
		
		//UC10
		public void countBasedOnCity() {
			int count = 0;
			System.out.println("Enter city name: ");
			String City = sc.next();
			count = (int) list.stream().filter(n -> n.getCity().equals(City)).count();
			System.out.println(count);
		}
		
		//UC11
		public void sortingByName() {
			list = list.stream().sorted(Comparator.comparing(Person::getFirstName)).collect(Collectors.toList());
			list.forEach(i -> System.out.println(i));
		}
		
		//UC12
		public void sortingByCity() {
			list = list.stream().sorted(Comparator.comparing(Person::getCity)).collect(Collectors.toList());
			list.forEach(i -> System.out.println(i));
		}
		
		public void addDataToFile(String addressBookName) {
			System.out.println("Enter name of text file to write data: ");
			String fileName = sc.next();
			File file = new File("C:\\Users\\Muthyala Aishwarya\\git\\AddressBookData"+fileName+".txt");
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				List<String[]> stringData = new ArrayList<>();
				for(Person detail : list) {
					stringData.add(new String[] { "Person: "+" \n1. FirstName: "+firstName+" \n2. LastName: "+lastName+"\n3. Address: "+address
						+"\n4. City: "+city+"\n5. State: "+state+"\n6. Zip: "+zip+"\n7. PhoneNumber: "+phno
						+"\n 8. Email: "+emailId+"\n" });
					bw.writeAll(stringData);
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void readDataFromFile() {
			System.out.println("Enter Address Book Name: ");
			String fileName = sc.next();
			Path filePath = Paths.get("C:\\Users\\Muthyala Aishwarya\\git\\AddressBookData"+fileName+".txt");
			try {
				Files.lines(filePath).map(line -> line.trim()).forEach(line -> System.out.println(line));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void addDataToCSVFile(String addressBookName) throws IOException {
			System.out.println("Enter name of CSV file to write data: ");
			String fileName = sc.next();
			Path filePath = Paths.get("C:\\Users\\Muthyala Aishwarya\\git\\AddressBookData"+fileName+".csv");
			if(Files.notExists(filePath))
				Files.createFile(filePath);
			File file = new File(String.valueOf(filePath));
			try {
				FileWriter fw = new FileWriter(file, true);
				CSVWriter writer = new CSVWriter(fw);
				List<String[]> data = new ArrayList<>();
				for(Person details : list) {
					data.add(new String[] { "Person: "+"\n1. FirstName: "+details.firstName+"\n2. LastName: "
							+details.lastName+"\n3. Address: "+details.address+"\n4. City: "+details.city+"\n5. State: "
							+details.state+"\n6. Zip: "+details.zip+"\n7. PhoneNumber: "+details.phno+"\n8. Email: "+details.emailId+"\n"
					});
				}
				writer.writeAll(data);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void readDataFromCSVFile() {
			System.out.println("Enter Address Book Name: ");
			String fileName = sc.next();
			CSVreader reader = null;
			try {
				reader = new CSVReader(new FileReader("C:\\Users\\Muthyala Aishwarya\\git\\AddressBookData"+fileName+".csv"));
				String[] nextLine;
				while((nextLine = reader.readNext() != null)) {
					for(String next : nextLine) {
						System.out.println(next);
					}
					System.out.println();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void addDataToJSONFile(String addressBookName) throws IOException {
			System.out.println("Enter name for json written file : ");
			String fileName = sc.nextLine();
			Path filePath = Paths.get("C:\\Users\\Muthyala Aishwarya\\git\\AddressBookData"+fileName+".json");
			Gson gson = new Gson();
			String json = gson.toJson(lst);
			FileWriter writer = new FileWriter(String.valueOf(filePath));
			writer.write(json);
			writer.close();
		}

		public void readDataFromJSONFile() throws FileNotFoundException {
			System.out.println("Enter address book name : ");
			String fileName = sc.nextLine();
			Path filePath = Paths.get("C:\\Users\\Muthyala Aishwarya\\git\\AddressBookData"+fileName+".json");
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new FileReader(String.valueOf(filePath)));
			Person[] data = gson.fromJson(br, Person[].class);
			List<Person> lst = Arrays.asList(data);
			for (Person details : lst) {
				System.out.println("Firstname : " + details.firstName);
				System.out.println("Lastname : " + details.lastName);
				System.out.println("Address : " + details.address);
				System.out.println("City : " + details.city);
				System.out.println("State : " + details.state);
				System.out.println("Zip : " + details.zip);
				System.out.println("PhoneNumber : " + details.phno);
				System.out.println("Email : " + details.emailId);
			}
		}
}
