package com.qrcodegenerate.service;

public interface QrCodeService {
    byte[] generateQRCode(String latitude, String longitude, int width, int height);
}
