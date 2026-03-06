package pl.libraryManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String author;
    private Integer publishYear;
    private String isbn;
    private BookStatus status;
    private Long categoryId;
}