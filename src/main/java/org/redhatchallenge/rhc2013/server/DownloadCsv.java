package org.redhatchallenge.rhc2013.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class DownloadCsv extends HttpServlet {

    private static final int BYTES_DOWNLOAD = 1024;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition",
                "attachment;filename=downloadname.ico");
        ServletContext ctx = getServletContext();
        InputStream is = ctx.getResourceAsStream("/favicon.ico");

        int read=0;
        byte[] bytes = new byte[BYTES_DOWNLOAD];
        OutputStream os = response.getOutputStream();

        while((read = is.read(bytes))!= -1){
            os.write(bytes, 0, read);
        }
        os.flush();
        os.close();
    }
}
