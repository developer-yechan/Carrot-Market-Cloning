package clone.carrotMarket.file;

import clone.carrotMarket.domain.ImageRank;
import clone.carrotMarket.domain.ProductImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class LocalUpload {


    @Value("${file.dir}")
    private String fileDir;

    public List<ProductImage> getProductImages(List<MultipartFile> multipartFiles) throws IOException {
        List<ProductImage> productImages = new ArrayList<>();
        for(int i = 0; i< multipartFiles.size(); i++){
            if(!multipartFiles.get(i).isEmpty()){
                MultipartFile imageFile = multipartFiles.get(i);
                String uuidName = upload(imageFile, "product");
                String downloadPath = "/image/product/"+ uuidName;
                if(i == 0){
                    ProductImage productImage = new ProductImage(downloadPath, ImageRank.대표);
                    productImages.add(productImage);
                }else{
                    ProductImage productImage = new ProductImage(downloadPath,ImageRank.일반);
                    productImages.add(productImage);
                }
            }
        }
        return productImages;
    }
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }
        String fileName = multipartFile.getOriginalFilename();
        String uuidName = createUUIDName(fileName);
        String fullPath = fileDir + "\\"+ uuidName;
        multipartFile.transferTo(new File(fullPath));
        return uuidName;
    }

    private String createUUIDName(String originalFileName){
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }


}
