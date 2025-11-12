package com.huyhieu.postservice.service.impl;

import com.huyhieu.common.exception.AppException;
import com.huyhieu.common.exception.ErrorCode;
import com.huyhieu.postservice.service.FileStorageService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileStorageServiceImpl implements FileStorageService {
  MinioClient minioClient;

  @Value("${minio.bucket-name}")
  @NonFinal
  String bucketName;

  @NonFinal
  @Value("${minio.expiry}")
  int expiry;

  @Override
  public String uploadFile(MultipartFile file) {
    try {
      String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(bucketName)
              .object(fileName) // Đây là ID file (objectName)
              .stream(file.getInputStream(), file.getSize(), -1)
              .contentType(file.getContentType())
              .build());

      log.info("Upload file thành công. ObjectName: {}", fileName);
      return fileName;

    } catch (Exception e) {
      log.error("Lỗi khi upload file lên MinIO", e);
      throw new AppException(ErrorCode.UPLOAD_FILE_FAIL);
    }
  }

  public String getPresignedUrl(String objectName) {
    if (objectName == null || objectName.isEmpty()) {
      return null;
    }
    try {
      // Tạo URL có hiệu lực trong 1 giờ
      return minioClient.getPresignedObjectUrl(
          GetPresignedObjectUrlArgs.builder()
              .method(Method.GET)
              .bucket(bucketName)
              .object(objectName)
              .expiry(expiry) // <-- Thời gian hết hạn
              .build());
    } catch (Exception e) {
      log.error("Lỗi khi lấy presigned URL cho object: {}", objectName, e);
      throw new AppException(ErrorCode.FILE_NOT_EXIT);
    }
  }
}
