package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;



@Service
@AllArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private final Cloudinary cloudinary; // injected via constructor

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            logger.error("Cloudinary upload failed: {}", e.getMessage(), e);
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage(), e);
        }
    }
}