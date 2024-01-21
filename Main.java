import java.sql.*;
import java.util.Scanner;
public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "1234";
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            while (true) {
                System.out.println();
                System.out.println("Hotel Reservation System");
                System.out.println("1. For reserve a room");
                System.out.println("2. To show reservations");
                System.out.println("3. TO get room number");
                System.out.println("4. For update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("0. For exit");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(con, sc);
                        break;
                    case 2:
                        showReservation(con, sc);
                        break;
                    case 3:
                        getRoomNumber(con, sc);
                        break;
                    case 4:
                        updateReservation(con, sc);
                        break;
                    case 5:
                        deleteReservation(con, sc);
                        break;
                    case 0:
                        exit();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void reserveRoom(Connection connection, Scanner sc) {
        System.out.println("Enter guest name:");
        String guestname = sc.next();
        sc.nextLine(); // Consume newline
        System.out.println("Enter room number:");
        int roomnumber = sc.nextInt();
        System.out.println("Enter mobile number:");
        String  mobilenumber = sc.next();
        String query = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                "VALUES ('" + guestname + "', " + roomnumber + ", '" + mobilenumber + "');";
        try (Statement stmt = connection.createStatement()) {
            int row_affected = stmt.executeUpdate(query);
            if (row_affected > 0) {
                System.out.println("Reservation successful");
            } else {
                System.out.println("Reservation failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void showReservation(Connection connection, Scanner sc) {
        String query = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement(); ResultSet res = stmt.executeQuery(query)) {
            while (res.next()) {
                int reservationID = res.getInt("reservation_id");
                String guestName = res.getString("guest_name");
                int roomNumber = res.getInt("room_number");
                int contactNumber = res.getInt("contact_number");
                String date = res.getString("reservation_date");
                System.out.println(reservationID);
                System.out.println(guestName);
                System.out.println(roomNumber);
                System.out.println(contactNumber);
                System.out.println(date);
                System.out.println("========================================");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void getRoomNumber(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter reservation ID:");
            int reservationId = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.println("Enter guest name:");
            String guestName = sc.nextLine();
            String query = "SELECT room_number FROM reservations WHERE reservation_id = " +
                    reservationId + " AND guest_name = '" + guestName + "'";
            try (Statement stmt = connection.createStatement(); ResultSet res = stmt.executeQuery(query)) {
                if (res.next()) {
                    int roomNumber = res.getInt("room_number");
                    System.out.println("Your room number for ID " + reservationId +
                            " and Guest name " + guestName + " is " + roomNumber);
                } else {
                    System.out.println("Reservation not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateReservation(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter reservation id for updation:");
            int id = sc.nextInt();
            if (!reservationExist(connection, id)) {
                System.out.println("Reservation does not exist");
                return;
            }
            sc.nextLine(); // Consume newline
            System.out.println("Enter new Guest name:");
            String newGuest = sc.nextLine();
            System.out.println("Enter new mobile number:");
            int newNumber = sc.nextInt();
            System.out.println("Enter new room number:");
            int newRoomNumber = sc.nextInt();
            String query = "UPDATE reservations SET guest_name = '" + newGuest +
                    "', room_number = " + newRoomNumber +
                    ", contact_number = " + newNumber +
                    " WHERE reservation_id = " + id;
            try (Statement stmt = connection.createStatement()) {
                int rows_affected = stmt.executeUpdate(query);
                if (rows_affected > 0) {
                    System.out.println("Reservation updated successfully");
                } else {
                    System.out.println("Reservation update failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteReservation(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter reservation ID to delete:");
            int id = sc.nextInt();
            String query = "DELETE FROM reservations WHERE reservation_id = " + id;
            try (Statement stmt = connection.createStatement()) {
                int rows_affected = stmt.executeUpdate(query);
                if (rows_affected > 0) {
                    System.out.println("Reservation has been deleted successfully");
                } else {
                    System.out.println("Deletion failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean reservationExist(Connection connection, int reservationId) {
        String query = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;
        try (Statement stmt = connection.createStatement(); ResultSet res = stmt.executeQuery(query)) {
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void exit() {
        System.out.println("Exiting System");
        int i = 5;
        try {
            while (i != 0) {
                System.out.print(".");
                Thread.sleep(450);
                i--;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupted status
        }
        System.out.println();
        System.out.println("Thank you for using the hotel reservation system");
    }
}