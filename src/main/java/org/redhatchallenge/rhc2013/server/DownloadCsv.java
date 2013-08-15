package org.redhatchallenge.rhc2013.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class DownloadCsv extends HttpServlet {

    private static final int BYTES_DOWNLOAD = 3000;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        String fname = request.getParameter("file");

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + fname);
        ServletContext ctx = getServletContext();

        InputStream is = new FileInputStream(System.getenv("OPENSHIFT_TMP_DIR") + fname);

        int read=0;
        byte[] bytes = new byte[BYTES_DOWNLOAD];
        OutputStream os = response.getOutputStream();

        while((read = is.read(bytes))!= -1) {
            os.write(bytes, 0, read);
        }
        os.flush();
        os.close();

        File file = new File(System.getenv("OPENSHIFT_TMP_DIR") + fname + "csv");
    }
}
