package com.movision.utils.captcha;

import com.movision.utils.captcha.backgrounds.BackgroundProducer;
import com.movision.utils.captcha.backgrounds.TransparentBackgroundProducer;
import com.movision.utils.captcha.noise.CurvedLineNoiseProducer;
import com.movision.utils.captcha.noise.NoiseProducer;
import com.movision.utils.captcha.text.producer.DefaultTextProducer;
import com.movision.utils.captcha.text.producer.TextProducer;
import com.movision.utils.captcha.text.renderer.DefaultWordRenderer;
import com.movision.utils.captcha.text.renderer.WordRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * A builder for generating a CAPTCHA image/answer pair.
 * <p/>
 * <p>
 * Example for generating a new CAPTCHA:
 * </p>
 * <pre>Captcha captcha = new Captcha.Builder(200, 50)
 * 	.addText()
 * 	.addBackground()
 * 	.build();</pre>
 * <p>Note that the <code>build()</code> must always be called last. Other methods are optional,
 * and can sometimes be repeated. For example:</p>
 * <pre>Captcha captcha = new Captcha.Builder(200, 50)
 * 	.addText()
 * 	.addNoise()
 * 	.addNoise()
 * 	.addNoise()
 * 	.addBackground()
 * 	.build();</pre>
 * <p>Adding multiple backgrounds has no affect; the last background added will simply be the
 * one that is eventually rendered.</p>
 * <p>To validate that <code>answerStr</code> is a correct answer to the CAPTCHA:</p>
 * <p/>
 * <code>captcha.isCorrect(answerStr);</code>
 *
 * @author <a href="mailto:james.childers@gmail.com">James Childers</a>
 *         v
 */
public final class Captcha implements Serializable {

    private static final long serialVersionUID = 617511236L;
    public static final String NAME = "simpleCaptcha";
    private Builder _builder;

    private Captcha(Builder builder) {
        _builder = builder;
    }

    public static class Builder implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * @serial
         */
        private String _answer = "";
        /**
         * @serial
         */
        private BufferedImage _img;
        /**
         * @serial
         */
        private BufferedImage _bg;
        private boolean _addBorder = false;

        public Builder(int width, int height) {
            _img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        /**
         * Add a background using the default BackgroundProducer.
         *
         * @return
         */
        public Builder addBackground() {
            return addBackground(new TransparentBackgroundProducer());
        }

        /**
         * Add a background using the given BackgroundProducer.
         *
         * @param bgProd
         * @return
         */
        public Builder addBackground(BackgroundProducer bgProd) {
            _bg = bgProd.getBackground(_img.getWidth(), _img.getHeight());

            return this;
        }

        /**
         * Add text to the image using the default TextProducer
         *
         * @return
         */
        public Builder addText() {
            return addText(new DefaultTextProducer());
        }

        /**
         * Add text to the image using the given TextProducer
         *
         * @param txtProd
         * @return
         */
        public Builder addText(TextProducer txtProd) {
            return addText(txtProd, new DefaultWordRenderer());
        }

        /**
         * Display the answer on the image using the given WordRenderer.
         *
         * @param wRenderer
         * @return
         */
        public Builder addText(WordRenderer wRenderer) {
            return addText(new DefaultTextProducer(), wRenderer);
        }

        public Builder addText(TextProducer txtProd, WordRenderer wRenderer) {
            _answer += txtProd.getText();
            wRenderer.render(_answer, _img);

            return this;
        }

        /**
         * Add noise using the default NoiseProducer.
         *
         * @return
         */
        public Builder addNoise() {
            return this.addNoise(new CurvedLineNoiseProducer());
        }

        /**
         * Add noise using the given NoiseProducer.
         *
         * @param nProd
         * @return
         */
        public Builder addNoise(NoiseProducer nProd) {
            nProd.makeNoise(_img);
            return this;
        }


        /**
         * Draw a single-pixel wide black border around the image.
         *
         * @return
         */
        public Builder addBorder() {
            _addBorder = true;

            return this;
        }

        /**
         * Build the CAPTCHA. This method should always be called, and should always
         * be called last.
         *
         * @return
         */
        public Captcha build() {
            if (_bg == null) {
                _bg = new TransparentBackgroundProducer().getBackground(_img.getWidth(), _img.getHeight());
            }

            // Paint the main image over the background
            Graphics2D g = _bg.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g.drawImage(_img, null, null);

            // Add the border, if necessary
            if (_addBorder) {
                int width = _img.getWidth();
                int height = _img.getHeight();

                g.setColor(Color.BLACK);
                g.drawLine(0, 0, 0, width);
                g.drawLine(0, 0, width, 0);
                g.drawLine(0, height - 1, width, height - 1);
                g.drawLine(width - 1, height - 1, width - 1, 0);
            }

            _img = _bg;

            return new Captcha(this);
        }

        @Override
        public String toString() {
            return "[Answer: " + _answer + "][Image: " + _img + "]";
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(_answer);
            ImageIO.write(_img, "png", ImageIO.createImageOutputStream(out));
        }

        private void readObject(ObjectInputStream in) throws IOException,
                ClassNotFoundException {
            _answer = (String) in.readObject();
            _img = ImageIO.read(ImageIO.createImageInputStream(in));
        }
    }

    public boolean isCorrect(String answer) {
        return _builder._answer.equals(answer);
    }

    public String getAnswer() {
        return _builder._answer;
    }

    public BufferedImage getImage() {
        return _builder._img;
    }

    @Override
    public String toString() {
        return _builder.toString();
    }
}