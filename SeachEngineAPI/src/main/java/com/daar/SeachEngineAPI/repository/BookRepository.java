package com.daar.SeachEngineAPI.repository;

import com.daar.SeachEngineAPI.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM books WHERE tsv @@ plainto_tsquery('english', :query)", nativeQuery = true)
    List<Book> searchByFullText(@Param("query") String query);

    boolean existsByTitleAndAuthor(@Param("title") String title, @Param("author") String author);

    @Query(
            value = """
            SELECT keyword, array_agg(book_id) AS books
            FROM (
                SELECT id AS book_id, unnest(tsvector_to_array(tsv)) AS keyword
                FROM books
            ) AS sub
            GROUP BY keyword
            ORDER BY keyword
            """,
            nativeQuery = true
    )
    List<KeywordBooks> findKeywordBooks();

}
