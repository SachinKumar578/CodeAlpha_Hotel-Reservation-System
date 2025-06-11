
import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    boolean isBooked;

    Room(int number, String category) {
        this.roomNumber = number;
        this.category = category;
        this.isBooked = false;
    }

    public String toString() {
        return "Room " + roomNumber + " [" + category + "] - " + (isBooked ? "Booked" : "Available");
    }
}

class Hotel {
    ArrayList<Room> rooms = new ArrayList<>();

    Hotel() {
        loadRooms();
    }

    void loadRooms() {
        // Predefined rooms
        for (int i = 1; i <= 10; i++) rooms.add(new Room(i, "Standard"));
        for (int i = 11; i <= 15; i++) rooms.add(new Room(i, "Deluxe"));
        for (int i = 16; i <= 18; i++) rooms.add(new Room(i, "Suite"));

        // Load booked rooms from file
        try (Scanner sc = new Scanner(new File("bookings.txt"))) {
            while (sc.hasNextLine()) {
                int roomNo = Integer.parseInt(sc.nextLine().trim());
                for (Room room : rooms) {
                    if (room.roomNumber == roomNo) {
                        room.isBooked = true;
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // First run: file not found is okay
        }
    }

    void viewAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room room : rooms) {
            if (!room.isBooked) System.out.println(room);
        }
    }

    void bookRoom(Scanner sc) {
        viewAvailableRooms();
        System.out.print("Enter room number to book: ");
        int num = sc.nextInt();

        for (Room room : rooms) {
            if (room.roomNumber == num && !room.isBooked) {
                room.isBooked = true;
                saveBooking(num);
                System.out.println("Payment Successful. Room " + num + " booked!");
                return;
            }
        }
        System.out.println("Room not available or invalid number.");
    }

    void cancelBooking(Scanner sc) {
        System.out.print("Enter room number to cancel booking: ");
        int num = sc.nextInt();

        for (Room room : rooms) {
            if (room.roomNumber == num && room.isBooked) {
                room.isBooked = false;
                removeBooking(num);
                System.out.println("Booking for Room " + num + " cancelled.");
                return;
            }
        }
        System.out.println("No booking found for that room.");
    }

    void saveBooking(int roomNo) {
        try (FileWriter fw = new FileWriter("bookings.txt", true)) {
            fw.write(roomNo + "\n");
        } catch (IOException e) {
            System.out.println("Error saving booking.");
        }
    }

    void removeBooking(int roomNo) {
        try {
            File inputFile = new File("bookings.txt");
            File tempFile = new File("temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().equals(String.valueOf(roomNo))) {
                    writer.write(line + System.lineSeparator());
                }
            }
            writer.close();
            reader.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            System.out.println("Error canceling booking.");
        }
    }

    void viewAllBookings() {
        System.out.println("\n--- Booked Rooms ---");
        for (Room room : rooms) {
            if (room.isBooked) System.out.println(room);
        }
    }
}

public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();
        int choice;

        do {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> hotel.viewAvailableRooms();
                case 2 -> hotel.bookRoom(scanner);
                case 3 -> hotel.cancelBooking(scanner);
                case 4 -> hotel.viewAllBookings();
                case 5 -> System.out.println("Thank you for using our system.");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);

        scanner.close();
    }
}
