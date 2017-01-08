package pl.team.sm_project.budgetmanager;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class RequestHandler {

    public String sendPostRequest(String requestURL, HashMap<String, String> postData, String parameters) {
        URL url;
        StringBuilder string_builder = new StringBuilder();

        try {
            url = new URL(requestURL + parameters);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream output_stream = connection.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(output_stream, "UTF-8"));
            writer.write(getPostDataString(postData));

            writer.flush();
            writer.close();
            output_stream.close();

            int response_code = connection.getResponseCode();
            System.out.println(response_code);

            if(response_code == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                string_builder = new StringBuilder();

                String response;
                while ((response = br.readLine()) != null){
                    string_builder.append(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string_builder.toString();
    }

    public String sendGetRequest(String requestURL, String parameters) {
        StringBuilder string_builder =new StringBuilder();

        try {
            URL url = new URL(requestURL + parameters);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String buffer;
            while((buffer = buffered_reader.readLine()) != null) {
                string_builder.append(buffer).append("\n");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return string_builder.toString();
    }

    private String getPostDataString(HashMap<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
