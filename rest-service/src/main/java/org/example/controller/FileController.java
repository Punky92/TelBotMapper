package org.example.controller;

import lombok.extern.log4j.Log4j;
import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;
import org.example.entity.BinaryContent;
import org.example.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
@RequestMapping("/file")
@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public void getDoc(@RequestParam("id") String id, HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        AppDocument doc = fileService.getDocument(id);
        if (doc == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.parseMediaType(doc.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment; filename=" + doc.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);

        BinaryContent binaryContent = doc.getBinaryContent();
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

/*        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header("Content-disposition", "attachment; filename=" + doc.getDocName())
                .body(fileSystemResource);*/
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        AppPhoto photo = fileService.getPhoto(id);
        if (photo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-disposition", "attachment;");
        response.setStatus(HttpServletResponse.SC_OK);

        BinaryContent binaryContent = photo.getBinaryContent();
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

/*        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content-disposition", "attachment;")
                .body(fileSystemResource);*/
    }
}
