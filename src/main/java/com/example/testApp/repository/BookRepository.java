package com.example.testApp.repository;

import com.example.testApp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByTitleLike(String name);

    List<Book> findAllByTitleLikeOrDescriptionLikeOrAuthors_NameLikeOrGenres_NameLikeAllIgnoreCase(String name, String description, String athor, String genre, Pageable pageable);



    // work only Postgre. Change to universal
    @Query(value = "select b from books b order by random () limit :bookCount",  nativeQuery = true)
    List<Book> random(@Param("bookCount") Long bookCount);
}
