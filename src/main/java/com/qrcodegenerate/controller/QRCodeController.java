package com.qrcodegenerate.controller;

import com.qrcodegenerate.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class QRCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/showQRCode")
    public String showQRCode(String latitude, String longitude, Model model) {
        model.addAttribute("qrCodeContent", "/generateQRCode?latitude=" + latitude +','+ longitude);
        return "show-qr-code";
    }

    @GetMapping("/generateQRCode")
    public void generateQRCode(String latitude, String longitude, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        byte[] qrCode = qrCodeService.generateQRCode(latitude,longitude, 500, 500);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(qrCode);
    }
}
