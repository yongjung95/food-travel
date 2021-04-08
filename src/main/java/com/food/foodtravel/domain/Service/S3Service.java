package com.food.foodtravel.domain.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.food.foodtravel.domain.image.ImageDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public static final String CLOUD_FRONT_DOMAIN_NAME = "d3l7ztabem9b9x.cloudfront.net";

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    /*public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket, fileName).toString();
    }*/
    public List<ImageDto> upload(List<MultipartFile> files) throws IOException {
        // 고유한 key 값을 갖기위해 현재 시간을 postfix로 붙여줌
        List<ImageDto> imageDtoList = new ArrayList<>();

        for (MultipartFile file : files){
            String originFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "." + originFileName.substring( originFileName.lastIndexOf(".") + 1 );

            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentType(file.getContentType());
            omd.setContentLength(file.getSize());
            omd.setHeader("filename",fileName);

            ImageDto imageDto = ImageDto.builder()
                                        .imageName(fileName)
                                        .imagePath("https://" + CLOUD_FRONT_DOMAIN_NAME + "/" +  fileName)
                                        .realImageName(file.getOriginalFilename())
                                        .build();
            imageDtoList.add(imageDto);

            // 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), omd)
                    .withCannedAcl(CannedAccessControlList.PublicRead));


        }

        return imageDtoList;
    }


    public List<String> update(List<String> filenames,List<MultipartFile> files) throws IOException {
        // 고유한 key 값을 갖기위해 현재 시간을 postfix로 붙여줌
        List<String> fileNameList = new ArrayList<>();

        for (String filename : filenames){
            // key가 존재하면 기존 파일은 삭제
            if (!"".equals(filename) && filename != null) {
                boolean isExistObject = s3Client.doesObjectExist(bucket, filename);

                if (isExistObject) {
                    s3Client.deleteObject(bucket, filename);
                }
            }
        }

        for (MultipartFile file : files){
            String originFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "." + originFileName.substring( originFileName.lastIndexOf(".") + 1 );

            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentType(file.getContentType());
            omd.setContentLength(file.getSize());
            omd.setHeader("filename",fileName);
            System.out.println(fileName);
            System.out.println(file.getInputStream());

            fileNameList.add(fileName);

            // 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), omd)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        }

        return fileNameList;

    }

    public void delete(String currentFilePath) throws IOException {
        s3Client.deleteObject(bucket, currentFilePath);
    }

}