/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.empresa.hito_1_3t_programacion_web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ShowReportServlet extends HttpServlet {
    
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Hacer la petición HTTP al microservicio
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://localhost:8081/api/inform/allreports");
            HttpResponse httpResponse = httpClient.execute(httpGet);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new RuntimeException("Error: HTTP status code " + statusCode);
            }

            // Obtener el JSON de la respuesta
            HttpEntity entity = httpResponse.getEntity();
            String jsonString = EntityUtils.toString(entity);

            JSONArray informes = new JSONArray(jsonString);

            // Generar el HTML
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<!DOCTYPE html>");
            htmlBuilder.append("<html lang=\"es\">");
            htmlBuilder.append("<head>");
            htmlBuilder.append("<title>Lista de Informes</title>");
            htmlBuilder.append("<script src=\"https://cdn.tailwindcss.com\"></script>");
            htmlBuilder.append("<style>"); // Sección para estilos personalizados
            htmlBuilder.append(".glow-on-hover {");
            htmlBuilder.append("  transition: all 0.5s;");
            htmlBuilder.append("}");
            htmlBuilder.append(".glow-on-hover:hover {");
            htmlBuilder.append("  box-shadow: 0 0 10px rgba(128, 0, 255, 0.6), 0 0 20px rgba(0, 128, 255, 0.6);");
            htmlBuilder.append("}");
            htmlBuilder.append("</style>");
            htmlBuilder.append("</head>");
            htmlBuilder.append("<body class=\"bg-gray-900 text-gray-200 p-8\">"); // Fondo oscuro y texto claro

            htmlBuilder.append("<div class=\"container mx-auto my-8 p-4 bg-gray-800 rounded-lg shadow-lg\">"); // Contenedor principal

            htmlBuilder.append("<h1 class=\"text-3xl font-bold text-purple-400 mb-6\">Lista de Informes</h1>"); // Título principal

            // Procesar el array de informes
            for (int i = 0; i < informes.length(); i++) {
                JSONObject informe = informes.getJSONObject(i);
                String reportName = informe.getString("reportName");
                String description = informe.getString("description");
                String pdfContentBase64 = informe.getString("pdfContent");

                // Decodificar el PDF desde Base64
                byte[] pdfBytes = Base64.getDecoder().decode(pdfContentBase64);

                htmlBuilder.append("<div class=\"bg-gray-700 p-4 rounded-lg glow-on-hover mb-4\">"); // Caja con hover y glow
                htmlBuilder.append("<h2 class=\"text-xl font-semibold text-blue-300\">").append(reportName).append("</h2>"); // Título del informe
                htmlBuilder.append("<p class=\"text-gray-400\">").append(description).append("</p>"); // Descripción con texto más claro

                String encodedPdfLink = Base64.getEncoder().encodeToString(pdfBytes);
                htmlBuilder.append("<a class=\"text-blue-500 hover:underline\" href=\"data:application/pdf;base64,").append(encodedPdfLink).append("\" download=\"").append(reportName).append(".pdf\">Descargar PDF</a>"); // Enlace para descargar PDF con hover
                htmlBuilder.append("</div>"); // Cerrar la caja del informe
            }

            htmlBuilder.append("</div>"); // Cerrar el contenedor principal
            htmlBuilder.append("</body>");
            htmlBuilder.append("</html>");

            // Escribir la respuesta
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            try (PrintWriter out = response.getWriter()) {
                out.println(htmlBuilder.toString());
            }
        } catch (Exception e) {
            throw new ServletException("Error al obtener informes: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}