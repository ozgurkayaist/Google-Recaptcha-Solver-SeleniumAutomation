package http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpWrapper {

    private boolean printHeaders = false;
    private String html;
    private int responseCode = 0;

    /**
     * Default constructor
     */
    public HttpWrapper() {
        html = "";
    }

    /**
     * A method to get the content and headers (if set) of a given page, with a given referer.
     *
     * @param url     The given URL.
     * @param referer The given referer.
     * @throws IllegalStateException Whenever an IO-related problem occurs.
     * @post new.getHtml() will return the headers and content of the given URL.
     */

    public void get(String url) {

        try {
            URL url_ = new URL(url);
            HttpURLConnection conn;

            conn = (HttpURLConnection) url_.openConnection();
            conn.setRequestMethod("GET");
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(false);
            conn.setInstanceFollowRedirects(false);

            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.setRequestProperty("Connection", "keep-alive");

            String headers = "";

            if (printHeaders) {
                for (String key : conn.getHeaderFields().keySet())
                    headers += ((key != null) ? key + ": " : "") + conn.getHeaderField(key) + "\n";
            }

            responseCode = conn.getResponseCode();

            BufferedReader d = new BufferedReader(new InputStreamReader(new DataInputStream(conn.getInputStream())));
            String result = "";
            String line = null;
            while ((line = d.readLine()) != null) {
                line = new String(line.getBytes(), "UTF-8");
                result += line + "\n";
            }

            d.close();

            if (printHeaders) {
                setHtml(headers + "\n" + result);
            } else {
                setHtml(result);
            }
        } catch (IOException e) {
            throw new IllegalStateException("An IOException occurred:" + "\n" + e.getMessage());
        }
    }

    public void postMultipartImage(String url, String textinstructions) {
        String charset = "UTF-8";
        File uploadFile1 = new File("src/test/resources/screenshots/imageFile.gif");
        File uploadFile2 = new File("src/test/resources/screenshots/imageInstructions.gif");
        String requestURL = url;

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
            multipart.addHeaderField("Content-Type", "multipart/form-data");
            // multipart.addFormField("textinstructions", textinstructions);
            multipart.addFormField("keywords", "Java,upload,Spring");

            multipart.addFilePart("file", uploadFile1);
            multipart.addFilePart("imginstructions", uploadFile2);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                setHtml(line);
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


    /**
     * Return the html content that this Wrapper has last retrieved from a request.
     */
    public String getHtml() {
        return this.html;
    }

    /**
     * Set the html content of this HttpWrapper.
     *
     * @param html The new html content.
     */
    private void setHtml(String html) {
        this.html = html;
    }

    /**
     * Set if headers should be print above the content or not
     *
     * @param trueOrFalse
     */
    public void setPrintHeaders(boolean trueOrFalse) {
        printHeaders = trueOrFalse;
    }

    /**
     * Returns the response code of the request
     *
     * @return
     */
    public int getResponseCode() {
        return responseCode;
    }

}