package com.hongik.devtalk.service;

import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.cloudfront-domain:}")
    private String cloudfrontDomain;

    /**
     * S3에 파일을 업로드하고 접근 가능한 URL을 반환합니다.
     * 
     * @param file 업로드할 파일
     * @param folder S3 버킷 내 폴더 경로 (예: "home/images")
     * @return 업로드된 파일의 URL
     */
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID().toString() + extension;
            String s3Key = folder + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("파일이 S3에 업로드되었습니다. Key: {}", s3Key);
            return getFileUrl(s3Key);

        } catch (IOException e) {
            log.error("파일 업로드 중 오류가 발생했습니다.", e);
            throw new GeneralException(GeneralErrorCode.S3_UPLOAD_FAILED, "S3 파일 업로드에 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("S3 연결 중 오류가 발생했습니다.", e);
            throw new GeneralException(GeneralErrorCode.S3_CONNECTION_FAILED, "S3 서비스 연결에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * S3에서 파일을 삭제합니다.
     * 
     * @param s3Key 삭제할 파일의 S3 키
     */
    public void deleteFile(String s3Key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("파일이 S3에서 삭제되었습니다. Key: {}", s3Key);

        } catch (Exception e) {
            log.error("파일 삭제 중 오류가 발생했습니다. Key: {}", s3Key, e);
            throw new GeneralException(GeneralErrorCode.S3_DELETE_FAILED, "S3 파일 삭제에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * URL에서 S3 키를 추출합니다.
     * 
     * @param url S3 또는 CloudFront URL
     * @return S3 키
     */
    public String extractS3KeyFromUrl(String url) {
        if (url == null) return null;
        
        // CloudFront URL인 경우
        if (!cloudfrontDomain.isEmpty() && url.contains(cloudfrontDomain)) {
            return url.substring(url.indexOf(cloudfrontDomain) + cloudfrontDomain.length() + 1);
        }
        
        // S3 직접 URL인 경우
        if (url.contains(bucketName + ".s3.")) {
            return url.substring(url.indexOf(bucketName + ".s3.") + bucketName.length() + 4 + url.substring(url.indexOf(bucketName + ".s3.") + bucketName.length() + 4).indexOf("/") + 1);
        }
        
        return null;
    }

    /**
     * 파일 확장자를 추출합니다.
     * 
     * @param filename 파일명
     * @return 확장자 (점 포함)
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    /**
     * S3 키를 기반으로 접근 가능한 URL을 생성합니다.
     * 
     * @param s3Key S3 키
     * @return 접근 가능한 URL
     */
    private String getFileUrl(String s3Key) {
        // CloudFront 도메인이 설정되어 있으면 CloudFront URL 사용
        if (!cloudfrontDomain.isEmpty()) {
            return "https://" + cloudfrontDomain + "/" + s3Key;
        }
        
        // 그렇지 않으면 S3 직접 URL 사용
        return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + s3Key;
    }

    /**
     * 업로드 허용 파일 형식을 검증합니다.
     * 
     * @param file 검증할 파일
     * @return 허용된 파일인지 여부
     */
    public boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("image/jpeg") ||
            contentType.equals("image/png") ||
            contentType.equals("image/webp")
        );
    }

    /**
     * 파일 크기를 검증합니다.
     * 
     * @param file 검증할 파일
     * @param maxSizeBytes 최대 크기 (바이트)
     * @return 크기가 유효한지 여부
     */
    public boolean isValidFileSize(MultipartFile file, long maxSizeBytes) {
        return file != null && file.getSize() <= maxSizeBytes;
    }
}
