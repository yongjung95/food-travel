package com.food.foodtravel.GalleryTest;

import com.food.foodtravel.domain.Service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class GalleryController {
    private S3Service s3Service;
    private GalleryService galleryService;
    private final GalleryRepository galleryRepository;

    @GetMapping("/gallery/View")
    public String galleryView(Model model) {
        List<GalleryDto> galleryDtoList = galleryService.getList();

        model.addAttribute("galleryList", galleryDtoList);

        return "/galleryView";
    }

    @GetMapping("/gallery")
    public String dispWrite() {


        return "/gallery";
    }

    @PostMapping("/gallery")
    public String execWrite(GalleryDto galleryDto, List<MultipartFile> fileList) throws IOException {
        //String imgPath = s3Service.upload(galleryDto.getFilePath(),file);
        //galleryDto.setFilePath(imgPath);

        /*for (MultipartFile file : fileList){
            String imgPath = s3Service.upload(galleryDto.getFilePath(),file);
            galleryDto.setFilePath(imgPath);
            galleryService.savePost(galleryDto);
        }*/
        System.out.println(fileList.size() + "파일 리스트 사이즈");
        for ( MultipartFile file : fileList){
            System.out.println(file.getSize() + "파일 사이즈");
        }


        /*List<String> fileNameList = s3Service.upload(fileList);
        for (String fineName : fileNameList){
            galleryDto.setFilePath(fineName);
            galleryService.savePost(galleryDto);
        }*/

        //galleryService.savePost(galleryDto);

        return "redirect:/gallery";
    }

    @PostMapping("/gallery/update")
    public String execUpdate(GalleryDto galleryDto, List<MultipartFile> fileList) throws IOException {
        List<GalleryEntity> galleryEntities = galleryRepository.findByTitle(galleryDto.getTitle());
        List<String> filenames = new ArrayList<>();

        for(GalleryEntity galleryEntity:galleryEntities){
            filenames.add(galleryEntity.getFilePath());
            galleryRepository.delete(galleryEntity);
        }

        List<String> fileNameList = s3Service.update(filenames,fileList);
        for (String fineName : fileNameList){
            galleryDto.setFilePath(fineName);
            galleryService.savePost(galleryDto);
        }



        //galleryService.savePost(galleryDto);

        return "redirect:/gallery";
    }

    /*@PostMapping("/gallery/Delete")
    public String delete(GalleryDto galleryDto,MultipartFile file) throws IOException{
        s3Service.delete(galleryDto.getFilePath());
        return "redirect:/gallery";
    }*/
}