package nu.epsilon.rss.ui.slotmachine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import nu.epsilon.rss.common.PerformanceTuning;
import nu.epsilon.rss.ui.components.NanoSource;
import nu.epsilon.rss.ui.components.glaspane.GlassPaneHandler;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

/**
 *  Exception handler in the form of a slot machine
 * 
 * @author Pär Sikö
 */
public class SlotMachineExceptionHandler extends JPanel implements TimingTarget {

    private final int rollerWidth = 150;
    private final int rollerHeight = 350;
    private final int padding = 10;
    private List<BufferedImage> images;
    private List<BufferedImage> blurredImages;
    private Animator animator;
    private int yPos = 0;
    private BufferedImage rollerImage1,  rollerImage2,  rollerImage3;
    private int currentImage1 = 0;
    private int currentImage2 = 0;
    private int currentImage3 = 0;
    private Exception exception;
    private BufferedImage exceptionImage;
    private int repaintCalls = 0;
    private int actualRepaintCalls = 0;
    private boolean init = false;
    private final static SlotMachineExceptionHandler INSTANCE =
            new SlotMachineExceptionHandler();
    private Logger logger = Logger.getLogger("nu.epsilon.rss.ui.slotmachine");

    public static SlotMachineExceptionHandler getInstance() {
        return INSTANCE;
    }

    public void init() {
        init = true;
        GraphicsEnvironment env = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = env.getDefaultScreenDevice().
                getDefaultConfiguration();
        try {
            for (int i = 1; i < 7; i++) {
                BufferedImage notCompatibleImage = ImageIO.read(getClass().
                        getResourceAsStream("/images/image" + i +
                        ".png"));
                BufferedImage bImage = gc.createCompatibleImage(
                        notCompatibleImage.getWidth(), notCompatibleImage.
                        getHeight());
                bImage.getGraphics().drawImage(notCompatibleImage, 0, 0, null);
                images.add(bImage);

                notCompatibleImage = ImageIO.read(
                        getClass().getResourceAsStream("/images/image" + i +
                        "_blurred.png"));
                bImage = gc.createCompatibleImage(notCompatibleImage.getWidth(),
                        notCompatibleImage.getHeight());
                bImage.getGraphics().drawImage(notCompatibleImage, 0, 0, null);
                blurredImages.add(bImage);

            }

            blurredImages.add(blurredImages.get(0));
            blurredImages.add(blurredImages.get(1));
            blurredImages.add(blurredImages.get(2));

        } catch (IOException ex) {
            logger.logp(Level.WARNING, this.getClass().toString(), "init",
                    "Error occured when initializing images", ex);
        }

        rollerImage1 = gc.createCompatibleImage(rollerWidth, rollerHeight + 100);
        rollerImage2 = gc.createCompatibleImage(rollerWidth, rollerHeight + 100);
        rollerImage3 = gc.createCompatibleImage(rollerWidth, rollerHeight + 100);

        rollerImage1.getGraphics().setColor(Color.WHITE);
        rollerImage2.getGraphics().setColor(Color.WHITE);
        rollerImage3.getGraphics().setColor(Color.WHITE);

        rollerImage1.getGraphics().fillRoundRect(0, 0, rollerWidth,
                rollerHeight + 100, 10, 10);
        rollerImage2.getGraphics().fillRoundRect(0, 0, rollerWidth,
                rollerHeight + 100, 10, 10);
        rollerImage3.getGraphics().fillRoundRect(0, 0, rollerWidth,
                rollerHeight + 100, 10, 10);

        for (int i = 0; i < 4; i++) {
            rollerImage1.getGraphics().drawImage(blurredImages.get(5 + i), 30,
                    20 + (100 * i), null);
            rollerImage2.getGraphics().drawImage(blurredImages.get(3 + i), 30,
                    20 + (100 * i), null);
            rollerImage3.getGraphics().drawImage(blurredImages.get(1 + i), 30,
                    20 + (100 * i), null);
        }

        currentImage1 = 5;
        currentImage2 = 3;
        currentImage3 = 1;
    }

    private SlotMachineExceptionHandler() {
        images = new ArrayList<BufferedImage>();
        blurredImages = new ArrayList<BufferedImage>();
    }

    public boolean isRunning() {
        return animator != null && animator.isRunning();
    }

    public void startSlotMachine() {
        animator = new Animator(2500, this);
        animator.setTimer(new NanoSource());
        animator.setResolution(2);
        if (!init) {
            init();
        }
        if (!animator.isRunning()) {
            GlassPaneHandler.getInstance().addComponentToGlassPane(this);
            setVisible(true);
            setSize(new Dimension(800, 700));
            animator.start();
        }
    }

    public void setException(Exception exc) {
        exception = exc;
        createExceptionImage();
    }

    private void createExceptionImage() {
        GraphicsEnvironment env = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = env.getDefaultScreenDevice().
                getDefaultConfiguration();
        exceptionImage = gc.createCompatibleImage(rollerWidth * 3, 100,
                Transparency.TRANSLUCENT);

        Font font;
        FontMetrics metrics;

        int width = 0;
        int fontSize = 10;
        while (width < exceptionImage.getWidth()) {
            font = new Font("Verdana", Font.BOLD, fontSize++);
            metrics = exceptionImage.getGraphics().getFontMetrics(font);
            width = metrics.stringWidth(exception.getMessage());
        }

        Graphics2D g = (Graphics2D) exceptionImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(new Font("Arial", Font.BOLD, fontSize - 1));
        g.setColor(Color.BLACK);
        g.drawString(exception.getMessage(), 20, 50);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        actualRepaintCalls++;
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int startPosX = (width - rollerWidth * 3 - padding * 2) / 2;
        int startPosY = (height - rollerHeight) / 2;


        g.setClip(startPosX, startPosY, rollerWidth * 3 + 300, rollerHeight);

        if (animator.isRunning()) {

            BufferedImage subImage1 = null, subImage2 = null, subImage3 = null;
            if (yPos >= 100) {
                yPos = 0;
                currentImage1--;
                currentImage2--;
                currentImage3--;
                if (currentImage1 == -1) {
                    currentImage1 = 5;
                }
                if (currentImage2 == -1) {
                    currentImage2 = 5;
                }
                if (currentImage3 == -1) {
                    currentImage3 = 5;
                }

                if (animator.getTimingFraction() <= 0.6f) {

                    rollerImage1.getGraphics().setColor(Color.GREEN);
                    rollerImage1.getGraphics().fillRoundRect(0, 0, rollerWidth,
                            rollerHeight + 100, 10, 10);
                    rollerImage1.getGraphics().drawImage(blurredImages.get(
                            currentImage1), 30, 20, null);
                    rollerImage1.getGraphics().drawImage(blurredImages.get(currentImage1 +
                            1), 30, 120, null);
                    rollerImage1.getGraphics().drawImage(blurredImages.get(currentImage1 +
                            2), 30, 220, null);
                    rollerImage1.getGraphics().drawImage(blurredImages.get(currentImage1 +
                            3), 30, 320, null);


                } else {

                    g.setColor(Color.WHITE);
                    g.fillRoundRect(startPosX, startPosY, rollerWidth,
                            rollerHeight, 30, 30);
                    g.drawImage(images.get(0), startPosX + 30, startPosY + 30,
                            null);
                    g.drawImage(images.get(1), startPosX + 30, startPosY + 230,
                            null);

                    subImage1 = exceptionImage.getSubimage(0, 0, rollerWidth,
                            exceptionImage.getHeight());

                }

                if (animator.getTimingFraction() <= 0.8) {

                    rollerImage2.getGraphics().setColor(Color.WHITE);
                    rollerImage2.getGraphics().fillRoundRect(0, 0, rollerWidth,
                            rollerHeight + 100, 10, 10);

                    rollerImage2.getGraphics().drawImage(blurredImages.get(
                            currentImage2), 30, 20, null);
                    rollerImage2.getGraphics().drawImage(blurredImages.get(currentImage2 +
                            1), 30, 120, null);
                    rollerImage2.getGraphics().drawImage(blurredImages.get(currentImage2 +
                            2), 30, 220, null);
                    rollerImage2.getGraphics().drawImage(blurredImages.get(currentImage2 +
                            3), 30, 320, null);

                } else {

                    g.setColor(Color.WHITE);
                    g.fillRoundRect(startPosX + rollerWidth + padding, startPosY,
                            rollerWidth, rollerHeight, 30, 30);
                    g.drawImage(images.get(2),
                            startPosX + rollerWidth + padding + 30, startPosY +
                            30, null);
                    g.drawImage(images.get(3),
                            startPosX + rollerWidth + padding + 30, startPosY +
                            230, null);

                    subImage2 = exceptionImage.getSubimage(rollerWidth, 0,
                            rollerWidth, exceptionImage.getHeight());

                }

                if (animator.getTimingFraction() < 0.95f) {
                    rollerImage3.getGraphics().setColor(Color.WHITE);
                    rollerImage3.getGraphics().fillRect(0, 0, rollerWidth,
                            rollerHeight + 100);
                    rollerImage3.getGraphics().drawImage(blurredImages.get(
                            currentImage3), 30, 20, null);
                    rollerImage3.getGraphics().drawImage(blurredImages.get(currentImage3 +
                            1), 30, 120, null);
                    rollerImage3.getGraphics().drawImage(blurredImages.get(currentImage3 +
                            2), 30, 220, null);
                    rollerImage3.getGraphics().drawImage(blurredImages.get(currentImage3 +
                            3), 30, 320, null);

                } else {

                    g.setColor(Color.WHITE);
                    g.fillRoundRect(startPosX + rollerWidth * 2 + padding * 2,
                            startPosY, rollerWidth, rollerHeight, 30,
                            30);

                    g.drawImage(images.get(4), startPosX + rollerWidth * 2 +
                            padding * 2 + 30, startPosY + 30, null);
                    g.drawImage(images.get(5), startPosX + rollerWidth * 2 +
                            padding * 2 + 30, startPosY + 230, null);

                    subImage3 = exceptionImage.getSubimage(rollerWidth * 2, 0,
                            rollerWidth, exceptionImage.getHeight());

                }

            }

            if (animator.getTimingFraction() <= 0.6f) {
                g.drawImage(rollerImage1, startPosX, startPosY + yPos - 50, null);
            } else {
                g.drawImage(subImage1, startPosX, startPosY + 120, null);
            }

            if (animator.getTimingFraction() <= 0.8) {
                g.drawImage(rollerImage2, startPosX + rollerWidth + padding,
                        startPosY + yPos - 50, null);
            } else {
                g.drawImage(subImage2, startPosX + rollerWidth + padding,
                        startPosY + 120, null);
            }

            if (animator.getTimingFraction() < 0.95f) {
                g.drawImage(rollerImage3, startPosX + rollerWidth * 2 +
                        padding * 2, startPosY + yPos - 50, null);
            } else {
                g.drawImage(subImage3, startPosX + rollerWidth * 2 + padding * 2,
                        startPosY + 120, null);
            }

        }

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.drawRoundRect(startPosX, startPosY, rollerWidth, rollerHeight, 30, 30);
        g.drawRoundRect(startPosX + rollerWidth + padding, startPosY,
                rollerWidth, rollerHeight, 30,
                30);
        g.drawRoundRect(startPosX + 2 * rollerWidth + 2 * padding, startPosY,
                rollerWidth,
                rollerHeight, 30, 30);

    }

    @Override
    public void timingEvent(float arg0) {
        repaintCalls++;
        yPos += 5;
        repaint();
    }

    @Override
    public void begin() {
        GlassPaneHandler.getInstance().setOpaque(true);
    }

    @Override
    public void end() {
        System.out.println("Calls: " + repaintCalls);
        System.out.println("Actual: " + actualRepaintCalls);
        repaintCalls = 0;
        actualRepaintCalls = 0;
        yPos = 0;
        try {
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            Logger.getLogger(SlotMachineExceptionHandler.class.getName()).log(
                    Level.SEVERE, null, ex);
        } finally {
            GlassPaneHandler.getInstance().setOpaque(false);
            GlassPaneHandler.getInstance().removeComponentFromGlassPane(this);
            GlassPaneHandler.getInstance().hideGlassPane();
            setVisible(false);
        }
    }

    @Override
    public void repeat() {
    }
}
