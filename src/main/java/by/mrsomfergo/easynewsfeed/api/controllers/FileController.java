package by.mrsomfergo.easynewsfeed.api.controllers;

import by.mrsomfergo.easynewsfeed.api.dto.FileDto;
import by.mrsomfergo.easynewsfeed.api.exceptions.BadRequestException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class FileController {

    public static final String UPLOAD_FILE = "/api/files";
    public static final String DOWNLOAD_FILE = "/api/files/{file_name}";
    public static final Path SAVE_PATH = Path.of("/home/mrsomfergo/uploads/");

    @PostMapping(UPLOAD_FILE)
    public FileDto uploadFile(
            @RequestParam(name = "file") MultipartFile file){

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if(fileName.contains("..")) throw new BadRequestException("Filename contains invalid path sequence.");

            Path targetLocation = SAVE_PATH.resolve(fileName);
            String newUUIDName = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Files.move(targetLocation, SAVE_PATH.resolve(newUUIDName));

            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(DOWNLOAD_FILE.substring(0,11))
                    .path(newUUIDName)
                    .toUriString();

            return FileDto.makeDefault(newUUIDName, fileDownloadUri, file.getSize());

        }catch (IOException ex){
            throw new BadRequestException("Could not store or rename file " + fileName + ". Please try again!");
        }
    }

    @GetMapping(DOWNLOAD_FILE)
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("file_name") String fileName,
            HttpServletRequest request){

        try{
            Path filePath = SAVE_PATH.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(!resource.exists()) throw new BadRequestException("File dosen't exists.");

            String contentType = request
                    .getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", resource.getFilename()))
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new BadRequestException("Resourse dosen't exists.");
        } catch (IOException e) {
            throw new BadRequestException("File dosen't exists.");
        }
    }
}
