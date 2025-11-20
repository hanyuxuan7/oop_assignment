import java.util.*;
import java.util.stream.Collectors;

public class LibraryApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final Library<Book> bookLibrary = new Library<>();
    private static final List<Borrower> borrowers = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("===== Welcome to the Library Management System =====");

        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books (Sorted by Title)");
            System.out.println("3. Search Book by Keyword");
            System.out.println("4. Filter Books by Genre");
            System.out.println("5. Borrow a Book");
            System.out.println("6. Return a Book");
            System.out.println("7. Show All Borrowers");
            System.out.println("8. Get Book Recommendation");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> searchBook();
                case 4 -> filterByGenre();
                case 5 -> borrowBook();
                case 6 -> returnBook();
                case 7 -> showBorrowers();
                case 8 -> recommendBook();
                case 9 -> {
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }


    private static void addBook() {
        System.out.print("Enter title: ");
        String title = sc.nextLine();
        System.out.print("Enter author: ");
        String author = sc.nextLine();
        System.out.print("Enter genre: ");
        String genre = sc.nextLine();
        System.out.print("Enter publication year: ");
        int year = readInt();

        Book b = new Book(title, author, genre, year);
        bookLibrary.add(b);
        System.out.println("Book added successfully!");
    }

    private static void viewBooks() {
        System.out.println("\n----- All Books in Library -----");
        if (bookLibrary.retrieve().isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        List<Book> sorted = new ArrayList<>(bookLibrary.retrieve());
        Collections.sort(sorted);

        Library<Book> tempLibrary = new Library<>();
        for (Book b : sorted) {
            tempLibrary.add(b);
        }

        tempLibrary.display();
    }

    private static void searchBook() {
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine();
        List<Book> results = bookLibrary.retrieve().stream()
                .filter(b -> b.matchesKeyword(keyword))
                .toList();

        System.out.println("\nSearch Results:");
        if (results.isEmpty()) System.out.println("No matching books found.");
        else results.forEach(System.out::println);
    }

    private static void filterByGenre() {
        System.out.print("Enter genre: ");
        String genre = sc.nextLine();
        List<Book> filtered = bookLibrary.retrieve().stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                .toList();

        if (filtered.isEmpty()) System.out.println("No books found in that genre.");
        else filtered.forEach(System.out::println);
    }

    private static void borrowBook() {
        System.out.print("Enter borrower name: ");
        String name = sc.nextLine();
        Borrower borrower = findOrCreateBorrower(name);

        System.out.print("Enter book title to borrow: ");
        String title = sc.nextLine();

        Optional<Book> bookOpt = bookLibrary.retrieve().stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title))
                .findFirst();

        if (bookOpt.isEmpty()) {
            System.out.println("Book not found!");
            return;
        }

        borrower.borrow(bookOpt.get());
        System.out.println( name + " borrowed \"" + title + "\"");
    }

    private static void returnBook() {
        System.out.print("Enter borrower name: ");
        String name = sc.nextLine();
        Borrower borrower = findBorrower(name);

        if (borrower == null) {
            System.out.println("Borrower not found!");
            return;
        }

        System.out.print("Enter title to return: ");
        String title = sc.nextLine();

        if (borrower.returnBook(title))
            System.out.println("Book returned successfully!");
        else
            System.out.println("This borrower didn’t borrow that book.");
    }

    private static void showBorrowers() {
        if (borrowers.isEmpty()) {
            System.out.println("No borrowers yet.");
            return;
        }

        System.out.println("\n----- Borrower List -----");
        borrowers.forEach(System.out::println);
    }

    private static void recommendBook() {
        System.out.print("Enter category (Science, Fiction, Biography): ");
        String category = sc.nextLine();
        System.out.println(recommendationSwitch(category));
    }

    private static Borrower findOrCreateBorrower(String name) {
        for (Borrower b : borrowers) {
            if (b.getName().equalsIgnoreCase(name))
                return b;
        }
        Borrower newB = new Borrower(name);
        borrowers.add(newB);
        return newB;
    }

    private static Borrower findBorrower(String name) {
        for (Borrower b : borrowers) {
            if (b.getName().equalsIgnoreCase(name))
                return b;
        }
        return null;
    }

    private static int readInt() {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private static String recommendationSwitch(String category) {
        return switch (category) {
            case "Science" -> "Try 'A Brief History of Time' by Stephen Hawking.";
            case "Fiction" -> "Try '1984' by George Orwell.";
            case "Biography" -> "Try 'A Beautiful Mind' by Sylvia Nasar.";
            default -> "No recommendation available for: " + category;
        };
    }
}
