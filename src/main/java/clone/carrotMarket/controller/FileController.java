//package clone.carrotMarket.controller;
//
//import clone.carrotMarket.file.FileStore;
//import lombok.AllArgsConstructor;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.net.MalformedURLException;
//
//@Controller
//@AllArgsConstructor
//@RequestMapping("/files")
//public class FileController {
//
//    private  final FileStore fileStore;
//
//
//
//    @ResponseBody
//    @GetMapping("/images/{filename}")
//    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
//        return new UrlResource("file:" + fileStore.getFullPath(filename));
//    }
//}
