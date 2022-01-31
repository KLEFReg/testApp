package com.example.testApp.controller;

import com.example.testApp.model.File;
import com.example.testApp.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/files")
    public List<File> all() {
        return fileService.getAll();
    }

    @PostMapping("/files")
    public File add(@RequestBody File newFile) {
        return fileService.add(newFile);
    }

    @GetMapping("/files/{id}")
    public File get(@PathVariable Long id) {
        return fileService.get(id);
    }

    @GetMapping("/files/upload/{id}")
    public byte[] upload(@PathVariable Long id) {
        return fileService.get(id).getContent();
    }

    @PutMapping("/files/{id}")
    public File update(@RequestBody File newFile, @PathVariable Long id) {
        newFile.setId(id);
        return fileService.update(newFile);
    }

    @DeleteMapping("/files/{id}")
    public void deleteBook(@PathVariable Long id) {
        fileService.delete(id);
    }


}
