package com.project.shop.service;

import com.project.shop.models.Customers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;
import java.util.logging.Logger;

@Service
public class ImageService {

    private static final Logger logger = Logger.getLogger(ImageService.class.getName());

    public void deleteImage(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            if (Files.exists(path)) {
                Files.delete(path);
                logger.info("Image deleted successfully: " + imagePath);
            } else {
                logger.warning("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            logger.severe("Could not delete image: " + imagePath);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete image", e);
        }
    }

    public String saveBase64Image(String base64Image, String uploadDir) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            String fileName = System.currentTimeMillis() + ".png";
            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, decodedBytes);
            return path.toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save image", e);
        }
    }

    public String convertImagetoBase64(Customers req){
        String base64Image = null;
        if (req.getPic() != null && !req.getPic().isEmpty()) {
            try {
                byte[] imageBytes = Files.readAllBytes(Paths.get(req.getPic()));
                base64Image = Base64.getEncoder().encodeToString(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64Image;
    }
}
