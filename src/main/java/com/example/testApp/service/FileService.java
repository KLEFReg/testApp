
package com.example.testApp.service;

import com.example.testApp.model.Author;
import com.example.testApp.model.File;
import com.example.testApp.repository.AuthorRepository;
import com.example.testApp.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public File add(File newAuthor) {
        return fileRepository.save(newAuthor);
    }

    public File get(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find file " + id));
    }

    public File update(File file){
        return fileRepository.save(file);
    }

    public void delete(Long id) {
        fileRepository.deleteById(id);
    }
}

