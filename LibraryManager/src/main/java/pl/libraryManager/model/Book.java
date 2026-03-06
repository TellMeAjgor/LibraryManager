package pl.libraryManager.model;

public class Book {
    private Long id;
    private String title;
    private String author;
    private Integer publishYear;
    private String isbn;
    private BookStatus status;
    private Long categoryId;

    public Book() {}

    public Book(Long id, String title, String author, Integer publishYear, String isbn, BookStatus status, Long categoryId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.isbn = isbn;
        this.status = status;
        this.categoryId = categoryId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (Status: %s, Kat ID: %s)", id, title, author, status, categoryId);
    }
}