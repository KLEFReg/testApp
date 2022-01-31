
package com.example.testApp.service;

import com.example.testApp.exception.BadResourceException;
import com.example.testApp.exception.ResourceAlreadyExistsException;
import com.example.testApp.exception.ResourceNotFoundException;
import com.example.testApp.model.Genre;
import com.example.testApp.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    private boolean existsById(Long id) {
        return genreRepository.existsById(id);
    }

    public Genre findById(Long id) throws ResourceNotFoundException {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre==null) {
            throw new ResourceNotFoundException("Cannot find Genre with id: " + id);
        }
        else return genre;
    }

    public List<Genre> findAll(int pageNumber, int rowPerPage) {
        List<Genre> genres = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        genreRepository.findAll(sortedByIdAsc).forEach(genres::add);
        return genres;
    }

    public Genre save(Genre genre) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(genre.getName())) {
            if (genre.getId() != null && existsById(genre.getId())) {
                throw new ResourceAlreadyExistsException("Genre with id: " + genre.getId() +
                        " already exists");
            }
            return genreRepository.save(genre);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save genre");
            exc.addErrorMessage("Genre is null or empty");
            throw exc;
        }
    }

    public void update(Genre genre)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(genre.getName())) {
            if (!existsById(genre.getId())) {
                throw new ResourceNotFoundException("Cannot find Genre with id: " + genre.getId());
            }
            genreRepository.save(genre);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save genre");
            exc.addErrorMessage("Genre is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find genre with id: " + id);
        }
        else {
            genreRepository.deleteById(id);
        }
    }

    public Long count() {
        return genreRepository.count();
    }
}

