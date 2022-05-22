package es.ucm.arblemar.desktopengine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;

import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Image;

public class DesktopGraphics extends AbstractGraphics implements ComponentListener {
    public DesktopGraphics(String titulo, Engine engine, int w, int h) {
        super(w, h);
        _mainEngine = engine;
        _titulo = titulo;
    }

    public boolean init() {
        // Creación de la ventana
        _screen = new DesktopScreen(_titulo);
        _screen.addMouseListener((DesktopInput) _mainEngine.getInput());
        _screen.addMouseMotionListener((DesktopInput) _mainEngine.getInput());

        return _screen.init((int) _wLogWindow, (int) _hLogWindow);
    }

    public DesktopScreen getScreen() {
        return _screen;
    }

    @Override
    public Image newImage(String name) throws Exception {
        DesktopImage newImage = new DesktopImage("./assets/" + name);
        if (!newImage.init())
            throw new Exception();
        return newImage;
    }

    @Override
    public Font newFont(String name, int size, boolean isBold) throws Exception {
        DesktopFont newFont = new DesktopFont("./assets/" + name, size, isBold);
        if (!newFont.init())
            throw new Exception();
        return newFont;
    }

    @Override
    public void clear(int color) {
        _graphics = getStrategy().getDrawGraphics();
        setColor(color);
//        _graphics.clearRect(0, 0, getWidth(), getHeight());
        _graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void setColor(int newColor) {
        float r = ((newColor >> 24) & 0xff) / 255.0f;
        float g = ((newColor >> 16) & 0xff) / 255.0f;
        float b = ((newColor >> 8) & 0xff) / 255.0f;
        float a = ((newColor) & 0xff) / 255.0f;
        Color c = new Color(r, g, b, a);
        _graphics.setColor(c);
    }

    @Override
    public void setFont(Font font, int tam) {
        _graphics.setFont(((DesktopFont) font).getJavaFont().deriveFont(tam));
    }

    @Override
    public void drawImage(Image image, int x, int y, float w, float h) {
        int[] newPos = realPos(x, y);
        int[] newSize = realSize(w, h);
        _graphics.drawImage(((DesktopImage) image).getImage(), newPos[0], newPos[1],
                newSize[0], newSize[1], null);
    }

    @Override
    public void drawRect(int x, int y, float width, float height) {
        int[] newPos = realPos(x, y);
        int[] newSize = realSize(width, height);
        _graphics.drawRect(newPos[0], newPos[1], newSize[0], newSize[1]);
    }

    @Override
    public void drawCircle(int x, int y, float radio) {
        int[] newPos = realPos(x, y);
        int newSize = realSize(radio);
        _graphics.drawOval(newPos[0], newPos[1], newSize, newSize);
    }

    @Override
    public void drawText(String text, int x, int y, Font font, int tam) {
        font.setSize(realSize(tam));
        _graphics.setFont(((DesktopFont) font).getJavaFont());
        int[] newPos = realPos(x, y);
        _graphics.drawString(text, newPos[0], newPos[1]);
    }

    @Override
    public void fillCircle(int x, int y, float dm) {
        int[] newPos = realPos(x, y);
        int newSize = realSize(dm);
        _graphics.fillOval(newPos[0], newPos[1], newSize, newSize);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        int[] newPos = realPos(x, y);
        int[] newSize = realSize(width, height);
        _graphics.fillRect(newPos[0], newPos[1], newSize[0], newSize[1]);
    }

    @Override
    public int getWidth() {
        return _screen.getWidth();
    }

    @Override
    public int getHeight() {
        return _screen.getHeight();
    }

    @Override
    public void updateGraphics() {
        while (getStrategy() == null) {
            System.out.println("NULL");
        }
        _graphics = getStrategy().getDrawGraphics();
    }

    @Override
    public void translate(int x, int y) {
        ((Graphics2D) _graphics).translate(x, y);
    }

    @Override
    public void scale(float x, float y) {
        ((Graphics2D) _graphics).scale(x, y);
    }

    @Override
    public void restore() {
        _graphics.dispose();
    }

    //---------------------------------------//
    public BufferStrategy getStrategy() {
        return _screen.getStrategy();
    }

    public java.awt.Graphics getJavaGraphics() {
        return _graphics;
    }

    //---------------------------------------//
    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    // VARIABLES
    private final Engine _mainEngine;
    private final String _titulo;
    private DesktopScreen _screen;
    private java.awt.Graphics _graphics;
}