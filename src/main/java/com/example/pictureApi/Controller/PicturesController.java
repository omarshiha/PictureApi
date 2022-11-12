package com.example.pictureApi.Controller;

import com.example.pictureApi.Model.PictureInfo;
import com.example.pictureApi.Service.PicturesStorageService;
import com.example.pictureApi.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:3000")
public class PicturesController {

    @Autowired
    PicturesStorageService picturesStorageService;

    @Autowired
    private PictureRepository pictureRepository;

    @PostMapping("/upload")
    public HashMap<String, String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("category") String category, @RequestParam("description") String description) {
        try {
            picturesStorageService.save(file);
            final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            PictureInfo pictureInfo = new PictureInfo((int) Math.random(), file.getOriginalFilename(), baseUrl + "/files/" + file.getOriginalFilename(), "pending", category, description);
            pictureRepository.save(pictureInfo);
            HashMap<String, String> res = new HashMap();
            res.put("filename", file.getOriginalFilename());
            return res;
        } catch (Exception e) {
            HashMap<String, String> res = new HashMap();
            res.put("err", e.toString());
            return res;
        }
    }

    @PostMapping("/takeaction")
    public void takeaction(HashMap<String, String> body) {
        try {
            String action = body.get("action");
            if(action == "approve"){
                Optional<PictureInfo> pics = pictureRepository.findById(Long.valueOf(body.get("imageId")));
                pics.ifPresent((pic) -> {
                    pic.setStatus("approved");
                });
            }else if(action == "reject"){
                Optional<PictureInfo> pics = pictureRepository.findById(Long.valueOf(body.get("imageId")));
                pics.ifPresent((pic) -> {
                    pic.setStatus("rejected");
                });
            }
        } catch (Exception e) {
            HashMap<String, String> res = new HashMap();
            res.put("err", e.toString());
        }
    }

    @GetMapping("/files")
    public List<PictureInfo> getListFiles() {
        return pictureRepository.findAll();
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = picturesStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
