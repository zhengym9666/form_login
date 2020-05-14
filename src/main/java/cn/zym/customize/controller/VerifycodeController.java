package cn.zym.customize.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @ClassName VerifycodeController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/8 14:33
 * @Version 1.0
 */
@RestController
public class VerifycodeController {

    @Autowired
    private Producer producer;

    @GetMapping("/vc.jpg")
    public void getVerifycode(HttpServletResponse resp, HttpSession session) throws IOException {
        resp.setContentType("image/jpeg");
        String text = producer.createText();
        session.setAttribute("verify_code",text);
        BufferedImage image = producer.createImage(text);
        ServletOutputStream out = null;
        try {
            out = resp.getOutputStream();
            ImageIO.write(image,"jpg",out);
        } catch (Exception e) {

        }finally {
            if (out!=null) {
                out.close();
            }
        }
    }

}
