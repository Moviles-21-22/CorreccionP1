package es.ucm.arblemar.desktopengine;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import es.ucm.arblemar.engine.Image;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class DesktopImage implements Image {
    public DesktopImage(String filename){
        _fileName = filename;
    }

    public boolean init(){
        try (InputStream is = new FileInputStream(_fileName)) {
            _javaImage = ImageIO.read(new File(_fileName));
        }
        catch (Exception e) {
            // Ouch. No está.
            System.err.println("Error cargando la imagen: " + e);
            return false;
        }

        return true;
    }

    @Override
    public int getWidth() {
        return _javaImage.getWidth(null);
    }

    @Override
    public int getHeight() {
        return _javaImage.getHeight(null);
    }

    public java.awt.Image getImage() {
        return _javaImage;
    }

    private java.awt.Image _javaImage;
    private String _fileName;
}
