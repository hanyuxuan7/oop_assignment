import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaneApp {

    private static void printMenu() {
        System.out.println();
        System.out.println("===== Plane Seating Reservation =====");
        System.out.println("(1) Show number of empty seats");
        System.out.println("(2) Show list of empty seats");
        System.out.println("(3) Show assigned seats (by seat ID)");
        System.out.println("(4) Show assigned seats (by customer ID)");
        System.out.println("(5) Assign a customer to a seat");
        System.out.println("(6) Remove a seat assignment");
        System.out.println("(7) Quit");
        System.out.print("Enter the number of your choice: ");
    }

    public static void main(String[] args) {
        Plane plane = new Plane();
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number 1–7.");
                sc.nextLine(); // clear invalid token
                continue;
            }

            switch (choice) {
                case 1:
                    plane.showNumEmptySeats();
                    break;

                case 2:
                    plane.showEmptySeats();
                    break;

                case 3:
                    plane.showAssignedSeats(true);   // by seat ID
                    break;

                case 4:
                    plane.showAssignedSeats(false);  // by customer ID
                    break;

                case 5: {
                    System.out.print("Please enter SeatID (1-12): ");
                    int seatId = sc.nextInt();
                    System.out.print("Please enter CustomerID: ");
                    int custId = sc.nextInt();
                    plane.assignSeat(seatId, custId);
                    break;
                }

                case 6: {
                    System.out.print("Please enter SeatID to unassign (1-12): ");
                    int seatId = sc.nextInt();
                    plane.unAssignSeat(seatId);
                    break;
                }

                case 7:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Please enter a number 1–7.");
            }
        }
    }
}
