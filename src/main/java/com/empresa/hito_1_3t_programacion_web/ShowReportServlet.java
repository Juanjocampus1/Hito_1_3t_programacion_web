/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.empresa.hito_1_3t_programacion_web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpClient;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author lluanllo
 */
public class ShowReportServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShowReportServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShowReportServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // Hacer la petición HTTP al microservicio
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet("http://localhost:9090/api/inform/allreports"); // Reemplazar con la URL real del microservicio
    HttpResponse httpResponse = httpClient.execute(httpGet);

    // Obtener el JSON de la respuesta
    HttpEntity entity = httpResponse.getEntity();
    String jsonString = EntityUtils.toString(entity);

    // Convertir el JSON a un array de objetos JSON
    JSONArray informes = new JSONArray(jsonString);

    // Generar el HTML para mostrar los informes
    StringBuilder htmlBuilder = new StringBuilder();
    htmlBuilder.append("<!DOCTYPE html>");
    htmlBuilder.append("<html>");
    htmlBuilder.append("<head>");
    htmlBuilder.append("<title>Informes</title>");
    htmlBuilder.append("</head>");
    htmlBuilder.append("<body>");

    // Recorrer el array de informes y agregarlos al HTML
    for (int i = 0; i < informes.length(); i++) {
        JSONObject informe = informes.getJSONObject(i);
        String reportName = informe.getString("reportName");
        String description = informe.getString("description");
        String pdfContentBase64 = informe.getString("pdfContent");

        // Decodificar el contenido binario del PDF
        byte[] pdfContent = Base64.getDecoder().decode(pdfContentBase64);

        // Agregar al HTML el título y la descripción del informe
        htmlBuilder.append("<div>");
        htmlBuilder.append("<h2>").append(reportName).append("</h2>");
        htmlBuilder.append("<p>").append(description).append("</p>");

        // Mostrar el PDF como un enlace para descargarlo
        String pdfContentBase64Encoded = Base64.getEncoder().encodeToString(pdfContent);
        htmlBuilder.append("<a href=\"data:application/pdf;base64,").append(pdfContentBase64Encoded).append("\" download=\"").append(reportName).append(".pdf\">Descargar PDF</a>");

        htmlBuilder.append("</div>");
    }

    htmlBuilder.append("</body>");
    htmlBuilder.append("</html>");

    // Configurar la respuesta
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");

    // Escribir el HTML en la respuesta
    try (PrintWriter out = response.getWriter()) {
        out.println(htmlBuilder.toString());
    }
}

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
