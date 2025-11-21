package com.daar.SeachEngineAPI.repository;

import com.daar.SeachEngineAPI.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM books WHERE tsv @@ plainto_tsquery('english', :query)", nativeQuery = true)
    List<Book> searchByFullText(@Param("query") String query);

}
