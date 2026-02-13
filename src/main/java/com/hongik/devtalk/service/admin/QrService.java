package com.hongik.devtalk.service.admin;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.global.apiPayload.code.BaseErrorCode;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.ShowSeminarRepository;
import com.hongik.devtalk.repository.seminar.StudentRepository;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {
    private final SeminarRepository seminarRepository;
    private final ShowSeminarRepository showSeminarRepository;
    private final StudentRepository studentRepository;

    private final S3Template s3Template;

    @Value("${aws.s3.bucket}") // application.yml에 설정한 버킷명
    private String bucketName;

    public String generateAndUploadQrCode(Long seminarId) throws Exception {
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(()-> new GeneralException(GeneralErrorCode.SESSION_NOT_FOUND));

        // 1. QR 데이터 구성 (출석 체크용 API 경로)
        String attendanceUrl = "https://api.hongikdevtalk.com/user/live/auth-applicants";

        // 2. QR 코드 생성 (메모리 내)
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(attendanceUrl, BarcodeFormat.QR_CODE, 400, 400);

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
            byte[] imageBytes = bos.toByteArray();

            // 3. S3 파일명 결정 (회차별 구분 및 중복 방지)
            String fileName = "qr-codes/seminar-" + seminarId + "-" + UUID.randomUUID() + ".png";

            // 4. S3 업로드
            try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
                var s3Resource = s3Template.upload(bucketName, fileName, bis);

                // 업로드된 파일의 Public URL 반환
                return s3Resource.getURL().toString();
            }
        }
    }

}
