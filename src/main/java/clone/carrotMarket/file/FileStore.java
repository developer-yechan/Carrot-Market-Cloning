package clone.carrotMarket.file;

import clone.carrotMarket.domain.ImageRank;
import clone.carrotMarket.domain.ProductImage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

   @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir+filename;
    }

    public List<ProductImage> storeImages(List<MultipartFile> multipartFiles) throws IOException {
        List<ProductImage> storeImages = new ArrayList<>();
        for(int i=0;i<multipartFiles.size();i++){
            if(!multipartFiles.get(i).isEmpty()){
                String storeFileName = createStoreFileName(multipartFiles.get(i));
                multipartFiles.get(i).transferTo(new File(getFullPath(storeFileName)));
                if(i == 0){
                    ProductImage storeImage = new ProductImage(storeFileName, ImageRank.대표);
                    storeImages.add(storeImage);
                }else{
                    ProductImage storeImage = new ProductImage(storeFileName, ImageRank.일반);
                    storeImages.add(storeImage);
                }
            }
        }
        return storeImages;
    }

    private String createStoreFileName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos+1);
        String uuid = UUID.randomUUID().toString();
        String storeFileName = uuid + "." + ext;
        return storeFileName;
    }
}
