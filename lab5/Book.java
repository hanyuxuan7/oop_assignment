public class Book implements Searchable, Comparable<Book> {
    private String title;
    private String author;
    private String genre;
    private int publicationYear;

    //constructor
    public Book(String t, String a, String g, int p) {
        this.title = t;
        this.author = a;
        this.genre = g;
        this.publicationYear = p;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }
    public int getPublicationYear(){
        return publicationYear;
    }
    public String toString(){
        return "'%s' by %s (%s, %d)".formatted(title, author, genre, publicationYear);
    }
    public int compareTo(Book otherbook){
        return this.title.compareToIgnoreCase(otherbook.title);
    }
}