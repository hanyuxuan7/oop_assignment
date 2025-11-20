import java.util.ArrayList;
import java.util.List;

public class Borrower {
    private final String name;
    private final List<Book> borrowedBooks = new ArrayList<>();

    public Borrower(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrow(Book book) {
        borrowedBooks.add(book);
    }

    public boolean returnBook(String title) {
        return borrowedBooks.removeIf(b -> b.getTitle().equalsIgnoreCase(title));
    }

    @Override
    public String toString() {
        return "Borrower{name: '%s', borrowedBooks: %s}".formatted(
                name,
                borrowedBooks.stream().map(Book::getTitle).toList()
        );
    }
}
