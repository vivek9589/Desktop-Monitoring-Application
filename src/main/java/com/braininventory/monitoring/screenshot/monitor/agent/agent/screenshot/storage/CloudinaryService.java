package com.braininventory.monitoring.screenshot.monitor.agent.agent.screenshot.storage;

import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.CloudinaryUploadException;
import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;



@Slf4j
@Service
@AllArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {
        try {
            log.info("Uploading file '{}' to Cloudinary...", file.getOriginalFilename());

            Map uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            String secureUrl = uploadResult.get("secure_url").toString();
            log.info("Cloudinary upload successful -> file='{}', link={}",
                    file.getOriginalFilename(), secureUrl);

            return secureUrl;
        } catch (Exception e) {
            log.error("Cloudinary upload failed for file '{}': {}",
                    file.getOriginalFilename(), e.getMessage(), e);
            throw new CloudinaryUploadException("Cloudinary upload failed: " + e.getMessage());
        }
    }
}