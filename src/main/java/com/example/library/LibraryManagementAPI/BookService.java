package com.example.library.LibraryManagementAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final Map<Integer, Book> bookStore = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public Book addBook(Book book) {
        int id = (book.getId() != null && !bookStore.containsKey(book.getId()))
                ? book.getId()
                : idGenerator.incrementAndGet();
        if (id > idGenerator.get()) {
            idGenerator.set(id);
        }
        book.setId(id);
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(0);
        }
        bookStore.put(id, book);
        return book;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookStore.values());
    }

    public Optional<Book> getBookById(Integer id) {
        return Optional.ofNullable(bookStore.get(id));
    }

    public Optional<Book> updateBook(Integer id, Book updatedBook) {
        Book existing = bookStore.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setCategory(updatedBook.getCategory());
        existing.setAvailableCopies(updatedBook.getAvailableCopies());
        return Optional.of(existing);
    }

    public boolean deleteBook(Integer id) {
        return bookStore.remove(id) != null;
    }
}