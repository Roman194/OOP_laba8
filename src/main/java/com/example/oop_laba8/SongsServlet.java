package com.example.oop_laba8;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Random;

@WebServlet("/songs_table")
public class SongsServlet extends HttpServlet {

    @Override
    protected  void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("songs.jsp");
        requestDispatcher.forward(req, resp);

        //req.getRequestDispatcher("/songs.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        Random rand = new Random();
        String randValue = Integer.toString(rand.nextInt(1000));

        JSONObject json = new JSONObject();
        json.put("id", randValue);
        json.put("song_name", req.getParameter("song_name"));
        json.put("author", req.getParameter("author"));
        json.put("album", req.getParameter("album"));
        json.put("length", req.getParameter("length"));
        json.put("year", req.getParameter("year"));


        //System.out.println(json);

        String path = "songs.json";

        ServletContext context = getServletContext();

        InputStream inputStream = context.getResourceAsStream(path);

        if (inputStream != null) {
            System.out.println("Hey!");

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder jsonString = new StringBuilder();

            while (true) {
                String text = bufferedReader.readLine();
                if (text == null)
                    break;
                jsonString.append(text);
            }

            System.out.println(jsonString);

            JSONArray array = new JSONArray(jsonString.toString());

            array.put(json);

            System.out.println(array);

            String fullPath;
            try {
                fullPath = new File(SongsServlet.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile().getParentFile().getParent();
                fullPath += File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "songs.json";
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            try(FileWriter writer = new FileWriter(fullPath)){
                writer.write(array.toString(4));
                writer.flush();
                writer.close();
            } catch (IOException ex){
                System.out.println("full Error!!!");
                ex.printStackTrace();
            }

            doGet(req, resp);
        }

    }
}
