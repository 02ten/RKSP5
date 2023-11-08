package com.example.app.controllers;

import com.example.app.dto.FileInfoDTO;
import com.example.app.dto.FileUploadDTO;
import com.example.app.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class FileController {
    final
    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files")
    public String getFilesPage(Model model){
        List<FileInfoDTO> fileInfoDTOList = fileService.getSentFiles();
        model.addAttribute("newFiles", new FileUploadDTO());
        model.addAttribute("files", fileInfoDTOList);
        return "files";
    }
    @PostMapping("/files")
    public String addFiles(@ModelAttribute("newFiles") FileUploadDTO fileUploadDTO){
        fileService.save(fileUploadDTO);
        return "redirect:/files";
    }
    @GetMapping("/upload-files/{url}")
    public void downloadFile(@PathVariable("url") String url, HttpServletResponse response){
        Path filePath = Paths.get("/upload-files/" + url);
        if(Files.exists(filePath)){
            response.setHeader("Content-disposition", "attachment;filename=" + url);
            response.setContentType("multipart/form-data");
            try{
                Files.copy(filePath, response.getOutputStream());
                response.getOutputStream().flush();
            }catch (IOException e){

            }

        }
    }
}
