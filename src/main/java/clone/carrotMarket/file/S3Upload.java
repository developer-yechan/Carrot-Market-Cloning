package clone.carrotMarket.file;

import clone.carrotMarket.domain.ImageRank;
import clone.carrotMarket.domain.ProductImage;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class S3Upload {

//    private final AmazonS3Client amazonS3Client;

//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

//    public List<ProductImage> getProductImages(List<MultipartFile> multipartFiles) throws IOException {
//        List<ProductImage> productImages = new ArrayList<>();
//        for(int i = 0; i< multipartFiles.size(); i++){
//            if(!multipartFiles.get(i).isEmpty()){
//                MultipartFile imageFile = multipartFiles.get(i);
//                String s3UploadUrl = upload(imageFile, "product");
//                if(i == 0){
//                    ProductImage productImage = new ProductImage(s3UploadUrl, ImageRank.대표);
//                    productImages.add(productImage);
//                }else{
//                    ProductImage productImage = new ProductImage(s3UploadUrl,ImageRank.일반);
//                    productImages.add(productImage);
//                }
//            }
//        }
//        return productImages;
//    }
//    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile을 File로 전환하지 못했습니다."));
//        return uploadS3URL(uploadFile,dirName);
//    }

//    private String uploadS3URL(File uploadFile, String dirName) {
//        String fileName = dirName + "/" + uploadFile.getName();
//        String uploadImageUrl = putS3(uploadFile, fileName);
//        removeLocalFile(uploadFile);
//        return uploadImageUrl;
//    }

//    private void removeLocalFile(File targetFile) {
//        if(targetFile.delete()){
//            log.info("파일이 삭제되었습니다.");
//        }else{
//            log.info("파일이 삭제되지 못했습니다.");
//        }
//    }

//    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName,uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
//        return amazonS3Client.getUrl(bucket,fileName).toString();
//    }

//    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
//        File convertFile = new File(multipartFile.getOriginalFilename());
//        if(convertFile.createNewFile()){
//            try(FileOutputStream fos = new FileOutputStream(convertFile)){
//                fos.write(multipartFile.getBytes());
//            }
//            return Optional.of(convertFile);
//
//        }
//        return Optional.empty();
//    }
}
