package com.movision.utils.captcha.servlet;

import com.movision.utils.captcha.Captcha;
import com.movision.utils.captcha.text.renderer.DefaultWordRenderer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author Weiwy
 */
public class ImgCaptchaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static int _width = 150;
    private static int _height = 40;

//    private static final List<Color> COLORS = new ArrayList<Color>(2);
//    private static final List<Font> FONTS = new ArrayList<Font>(3);
//    
//    static {
//        COLORS.add(Color.GREEN);
//        COLORS.add(Color.RED);
//
//        FONTS.add(new Font("Times New Roman", Font.ITALIC, 32));
//        FONTS.add(new Font("Courier", Font.BOLD, 32));
//        FONTS.add(new Font("Arial", Font.BOLD, 32));
//    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (getInitParameter("captcha-height") != null) {
            _height = Integer.valueOf(getInitParameter("captcha-height"));
        }

        if (getInitParameter("captcha-width") != null) {
            _width = Integer.valueOf(getInitParameter("captcha-width"));
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // ColoredEdgesWordRenderer wordRenderer = new ColoredEdgesWordRenderer(COLORS, FONTS);
        Captcha captcha = new Captcha.Builder(_width, _height).addText(new DefaultWordRenderer())
                // .gimp(new ShearGimpyRenderer())
                //    .addNoise()
                .addBackground()
                .addBorder()
                .build();
        req.getSession().setAttribute(Captcha.NAME, captcha);

        CaptchaServletUtil.writeImage(resp, captcha.getImage());
    }

}
