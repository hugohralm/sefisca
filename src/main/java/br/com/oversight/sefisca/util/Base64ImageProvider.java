package br.com.oversight.sefisca.util;

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;


public class Base64ImageProvider extends AbstractImageProvider {
    
    private String imagePath;
    
    public Base64ImageProvider(String imgPath) {
        this.imagePath = imgPath;
    }

    @Override
    public Image retrieve(String src) {
        int pos = src.indexOf("base64,");
        try {
            if (src.startsWith("data") && pos > 0) {
                byte[] img = Base64.decode(src.substring(pos + 7));
                return Image.getInstance(img);
            } else {
                return Image.getInstance(src);
            }
        } catch (BadElementException | IOException e) {
            e.getMessage();
            System.err.println("Erro na classe " + Base64ImageProvider.class.getName() + " no método retrive(String src)");
            return null;
        }
    }

    @Override
    public String getImageRootPath() {
        return imagePath;
    }
    
}