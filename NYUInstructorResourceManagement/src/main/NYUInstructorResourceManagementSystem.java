package main;
import java.io.*;
import java.util.*;

class Item {
	private String name;
	private boolean isAvailable;
	private String description;

	public Item(String name, boolean isAvailable, String description) {
		this.name = name;
		this.isAvailable = isAvailable;
		this.description = description;
    }

	public String getName() {
		return name;
    }

	public boolean isAvailable() {
		return isAvailable;
    }

	public void setAvailable(boolean available) {
		isAvailable = available;
    }

	public String getDescription() {
		return description;
    }

	public String toString() {
		return name + "," + (isAvailable ? "Available" : "Unavailable") + "," + description;
    }

	public static Item fromString(String data) {
		String[] parts = data.split(",", 3);
		return new Item(parts[0], parts[1].equals("Available"), parts[2]);
    }
}




public class NYUInstructorResourceManagementSystem {
	private static final String DATA_FILE = "items.txt";
	private static Scanner scanner = new Scanner(System.in);
	private static List<Item> itemList = new ArrayList<>();
	
	public static void main(String[] args) {
		loadItems();
		while (true) {
			System.out.println("Select an option:\nInstructor (1)\nStudent (2)\nQuit (3)");
				// select 1 for instructor, 2 for student, and 3 to quit program
			
			String choice = scanner.nextLine();
			switch (choice) {
				case "1" -> instructorMenu();
				case "2" -> studentMenu();
				case "3" -> {
					saveItems();
					return;
				}
			}
		}
	}

	
	
	
	// after choosing (1) for instructor, user can organize storage file
	private static void instructorMenu() {
		while (true) {
			System.out.println("\nInstructor Menu: \nAdd Item (1)\nUpdate Item (2)\nRemove Item (3)\nSearch (4)\nQuit (5)");
				// selecting 5 for quit will return the user to login screen
			switch (scanner.nextLine()) {
				case "1" -> addItem();
				case "2" -> updateItem();
				case "3" -> removeItem();
				case "4" -> searchItem();
				case "5" -> {
					return;
				}
			}
		}
	}

	
	
	
	
	// after choosing (2) for student, user can borrow and return items from storage file
	private static void studentMenu() {
		while (true) {
			System.out.println("\nStudent Menu: Borrow Item (1)\nReturn Item (2)\nSearch (3)\nView Details (4)\nQuit (5)");
				// selecting 5 for quit will return the user to login screen
			switch (scanner.nextLine()) {
				case "1" -> borrowItem();
				case "2" -> returnItem();
				case "3" -> searchItem();
				case "4" -> viewDetails();
				case "5" -> {
					return;
				}
			}
		}
	}

	
	
	
	// actions for user, depending on whether user is student or instructor
	private static void addItem() {
		System.out.print("Item Name: ");
		String name = scanner.nextLine();
		System.out.print("Description: ");
		String desc = scanner.nextLine();
		itemList.add(new Item(name, true, desc));
		saveItems();
		System.out.println("Item added.");
	}

	private static void updateItem() {
		System.out.print("Enter Item Name to Update: ");
		String name = scanner.nextLine();
		for (Item item : itemList) {
			if (item.getName().equalsIgnoreCase(name)) {
				System.out.print("New Name: ");
				itemList.remove(item);
				String newName = scanner.nextLine();
				System.out.print("Enter Status (Available/Unavailable): ");
				boolean isAvailable = scanner.nextLine().equalsIgnoreCase("Available");
				System.out.print("New Description: ");
				String desc = scanner.nextLine();
				itemList.add(new Item(newName, isAvailable, desc));
				saveItems();
				System.out.println("Item updated.");
				return;
			}
		}
		System.out.println("Item not found.");
	}

	private static void removeItem() {
		System.out.print("Enter Item Name to Remove: ");
		String name = scanner.nextLine();
		itemList.removeIf(i -> i.getName().equalsIgnoreCase(name));
		saveItems();
		System.out.println("Item removed if existed.");
	}

	private static void borrowItem() {
		System.out.print("Enter Item Name to Borrow: ");
		String name = scanner.nextLine();
		for (Item item : itemList) {
			if (item.getName().equalsIgnoreCase(name)) {
				if (item.isAvailable()) {
					item.setAvailable(false);
					saveItems();
					System.out.println("You have borrowed the item.");
				} else {
					System.out.println("Item unavailable.");
				}
				return;
			}
		}
		System.out.println("Item not found.");
	}

	private static void returnItem() {
		System.out.print("Enter Item Name to Return: ");
		String name = scanner.nextLine();
		for (Item item : itemList) {
			if (item.getName().equalsIgnoreCase(name)) {
				item.setAvailable(true);
				saveItems();
				System.out.println("Item returned.");
				return;
			}
		}
		System.out.println("Item not found.");
	}

	private static void searchItem() {
		System.out.print("Enter Item Name to Search: ");
		String name = scanner.nextLine();
		for (Item item : itemList) {
			if (item.getName().equalsIgnoreCase(name)) {
				System.out.println("Item Found: " + item.getName() + " - " + (item.isAvailable() ? "Available" : "Unavailable"));
				return;
			}
		}
		System.out.println("Item not found.");
	}

	private static void viewDetails() {
		System.out.print("Enter Item Name: ");
		String name = scanner.nextLine();
		for (Item item : itemList) {
			if (item.getName().equalsIgnoreCase(name)) {
				System.out.println("Name: " + item.getName());
				System.out.println("Status: " + (item.isAvailable() ? "Available" : "Unavailable"));
				System.out.println("Description: " + item.getDescription());
				return;
			}
		}
		System.out.println("Item not found.");
	}

	private static void loadItems() {
		File file = new File(DATA_FILE);
		if (!file.exists()) return;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				itemList.add(Item.fromString(line));
			}
		} catch (IOException e) {
			System.err.println("Failed to load items.");
		}
	}

	private static void saveItems() {
		try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
			for (Item item : itemList) {
				pw.println(item);
			}
		} catch (IOException e) {
			System.err.println("Failed to save items.");
		}
	}
}