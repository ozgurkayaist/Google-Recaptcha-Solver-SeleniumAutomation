package api;

import java.io.IOException;

import http.HttpWrapper;

public class TwoCaptchaService {

    /**
     * Your 2captcha.com captcha KEY
     */
    private String apiKey;


    /**
     * The google site key from the page you want to solve the recaptcha at
     */
    private String googleKey;


    /**
     * The URL where the recaptcha is placed.
     * For example: https://www.google.com/recaptcha/api2/demo
     */
    private String pageUrl;

    /**
     * The proxy ip if you want a worker to solve the recaptcha through your proxy
     */
    private String proxyIp;

    /**
     * The proxy port
     */
    private String proxyPort;

    /**
     * Your proxy username, if your proxy uses user authentication
     */
    private String proxyUser;

    /**
     * Your proxy password, if your proxy uses user authentication
     */
    private String proxyPw;

    /**
     * Your proxy type, for example ProxyType.HTTP
     */
    private ProxyType proxyType;

    /**
     * The HttpWrapper which the requests are made with
     */
    private HttpWrapper hw;

    /**
     * 1 - means that ReCaptcha is invisible. 0 - normal ReCaptcha.
     */
    private Integer inVisible;

    /**
     * 1 - defines that you're sending recatcha as image
     */
    private Integer recaptcha;

    /**
     * 1 - defines that you want to use canvas method
     */
    private Integer canvas;

    /**
     * String
     * Max 140 characters
     * Endcoding: UTF-8	Yes	Text with instruction for solving ReCaptcha.
     * For example: select images with trees.
     * Not required if you're sending instruction as an image with imginstructions.
     */
    private String textinstructions;

    /**
     * Integer	No	Number of rows in ReCaptcha grid.
     */
    private Integer recaptcharows;

    /**
     * recaptchacols	Integer	No	Number of columns in ReCaptcha grid.
     */
    private Integer recaptchacols;

    /**
     * Constructor if you don't use any proxy
     *
     * @param apiKey
     * @param googleKey
     * @param pageUrl
     */
    public TwoCaptchaService(String apiKey, String googleKey, String pageUrl) {
        this.apiKey = apiKey;
        this.googleKey = googleKey;
        this.pageUrl = pageUrl;
        hw = new HttpWrapper();
    }

    /**
     * Constructor if you don't use any proxy
     *
     * @param apiKey
     * @param googleKey
     * @param pageUrl
     */
    public TwoCaptchaService(String apiKey, String googleKey, String pageUrl, Integer inVisible) {
        this.apiKey = apiKey;
        this.googleKey = googleKey;
        this.pageUrl = pageUrl;
        this.inVisible = inVisible;
        hw = new HttpWrapper();
    }

    public TwoCaptchaService(String apiKey, String googleKey, String pageUrl, Integer inVisible, Integer recaptcha, Integer canvas, String textinstructions, Integer recaptcharows, Integer recaptchacols) {
        this.apiKey = apiKey;
        this.googleKey = googleKey;
        this.pageUrl = pageUrl;
        this.inVisible = inVisible;
        this.recaptcha = recaptcha;
        this.canvas = canvas;
        this.textinstructions = textinstructions;
        this.recaptcharows = recaptcharows;
        this.recaptchacols = recaptchacols;
        hw = new HttpWrapper();
    }

    /**
     * Constructor if you are using a proxy without user authentication
     *
     * @param apiKey
     * @param googleKey
     * @param pageUrl
     * @param proxyIp
     * @param proxyPort
     * @param proxyType
     */
    public TwoCaptchaService(String apiKey, String googleKey, String pageUrl, String proxyIp, String proxyPort, ProxyType proxyType) {
        this(apiKey, googleKey, pageUrl);
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
        this.proxyType = proxyType;
    }

    /**
     * Constructor if you are using a proxy with user authentication
     *
     * @param apiKey
     * @param googleKey
     * @param pageUrl
     * @param proxyIp
     * @param proxyPort
     * @param proxyUser
     * @param proxyPw
     * @param proxyType
     */
    public TwoCaptchaService(String apiKey, String googleKey, String pageUrl, String proxyIp, String proxyPort,
                             String proxyUser, String proxyPw, ProxyType proxyType) {
        this(apiKey, googleKey, pageUrl);
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPw = proxyPw;
        this.proxyType = proxyType;
    }

    /**
     * Sends the recaptcha challenge to 2captcha.com and
     * checks every second if a worker has solved it
     *
     * @return The response-token which is needed to solve and submit the recaptcha
     * @throws InterruptedException, when thread.sleep is interrupted
     * @throws IOException,          when there is any server issue and the request cannot be completed
     */
    public String solveImageCaptcha() throws InterruptedException, IOException {
        System.out.println("Sending recaptcha challenge to 2captcha.com");

        String parameters = "key=" + apiKey
                + "&method=post"
                + "&googlekey=" + googleKey
                + "&pageurl=" + pageUrl
                + "&invisible=" + inVisible
                + "&recaptcha=" + recaptcha
                + "&canvas=" + canvas
                + "&recaptcharows=" + recaptcharows
                + "&recaptchacols=" + recaptchacols;

        if (proxyIp != null) {
            if (proxyUser != null) {
                parameters += "&proxy="
                        + proxyUser + ":" + proxyPw
                        + "@"
                        + proxyIp + ":" + proxyPort;
            } else {
                parameters += "&proxy="
                        + proxyIp + ":" + proxyPort;
            }

            parameters += "&proxytype=" + proxyType;
        }
        hw.postMultipartImage("https://2captcha.com/in.php?" + parameters, textinstructions);
        System.out.println("Your request parameters: " + parameters);

        String captchaId = hw.getHtml().replaceAll("\\D", "");
        captchaId = hw.getHtml().replaceAll("OK\\|", "");
        int timeCounter = 0;
        System.out.println("Your CaptchaId for query the response: " + captchaId);

        do {
            hw.get("http://2captcha.com/res.php?key=" + apiKey
                    + "&action=get"
                    + "&id=" + captchaId);

            Thread.sleep(10000);

            timeCounter++;
            System.out.println("Waiting for captcha to be solved");
        } while (hw.getHtml().contains("NOT_READY"));

        System.out.println("It took " + timeCounter * 10 + " seconds to solve the captcha");
        String gRecaptchaResponse = hw.getHtml().replaceAll("OK\\|", "").replaceAll("\\n", "");
        return gRecaptchaResponse;
    }

    public String solveCaptcha() throws InterruptedException, IOException {
        System.out.println("Sending recaptcha challenge to 2captcha.com");

        String parameters = "key=" + apiKey
                + "&method=userrecaptcha"
                + "&googlekey=" + googleKey
                + "&pageurl=" + pageUrl
                + "&invisible=" + inVisible;

        if (proxyIp != null) {
            if (proxyUser != null) {
                parameters += "&proxy="
                        + proxyUser + ":" + proxyPw
                        + "@"
                        + proxyIp + ":" + proxyPort;
            } else {
                parameters += "&proxy="
                        + proxyIp + ":" + proxyPort;
            }

            parameters += "&proxytype=" + proxyType;
        }
        hw.postMultipartImage("https://2captcha.com/in.php?" + parameters, textinstructions);
        System.out.println("Your request parameters: " + parameters);

        String captchaId = hw.getHtml().replaceAll("\\D", "");
        int timeCounter = 0;
        System.out.println("Your CaptchaId for query the response: " + captchaId);

        do {
            hw.get("http://2captcha.com/res.php?key=" + apiKey
                    + "&action=get"
                    + "&id=" + captchaId);

            Thread.sleep(10000);

            timeCounter++;
            System.out.println("Waiting for captcha to be solved");
        } while (hw.getHtml().contains("NOT_READY"));

        System.out.println("It took " + timeCounter * 10 + " seconds to solve the captcha");
        String gRecaptchaResponse = hw.getHtml().replaceAll("OK\\|", "").replaceAll("\\n", "");
        System.out.println(gRecaptchaResponse);
        return gRecaptchaResponse;
    }

    /**
     * @return The 2captcha.com captcha key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the 2captcha.com captcha key
     *
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return The google site key
     */
    public String getGoogleKey() {
        return googleKey;
    }

    /**
     * Sets the google site key
     *
     * @param googleKey
     */
    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    /**
     * @return The page url
     */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * Sets the page url
     *
     * @param pageUrl
     */
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * @return The proxy ip
     */
    public String getProxyIp() {
        return proxyIp;
    }

    /**
     * Sets the proxy ip
     *
     * @param proxyIp
     */
    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    /**
     * @return The proxy port
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets the proxy port
     *
     * @param proxyPort
     */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * @return The proxy authentication user
     */
    public String getProxyUser() {
        return proxyUser;
    }

    /**
     * Sets the proxy authentication user
     *
     * @param proxyUser
     */
    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    /**
     * @return The proxy authentication password
     */
    public String getProxyPw() {
        return proxyPw;
    }

    /**
     * Sets the proxy authentication password
     *
     * @param proxyPw
     */
    public void setProxyPw(String proxyPw) {
        this.proxyPw = proxyPw;
    }

    /**
     * @return The proxy type
     */
    public ProxyType getProxyType() {
        return proxyType;
    }

    /**
     * Sets the proxy type
     *
     * @param proxyType
     */
    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }
}
