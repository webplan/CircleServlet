package com.circle.function;

import com.opensymphony.xwork2.ActionInvocation;
import org.apache.struts2.dispatcher.ServletRedirectResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by snow on 15-6-4.
 */
public class PrintToHtml extends ServletRedirectResult {

    public static String PrintToHtml(HttpServletResponse response,String ret){

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;chatset=utf-8");
        PrintWriter pw=null;
        try {
            pw = response.getWriter();
            pw.write(ret);
        } catch (IOException e) {

        }
        //response.setCharacterEncoding("utf-8");
        pw.flush();
        pw.close();
        //response.setCharacterEncoding("utf-8");


        pw.flush();
        pw.close();
        return null;
    }
}
