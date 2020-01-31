package pages;

import api.TwoCaptchaService;
import base.DriverBase;
import base.PropertyLoader;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Recaptcha2Page extends DriverBase {

    public Recaptcha2Page(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    private Boolean isCaptchaInvisible = Boolean.FALSE;
    private Point locationOfIFrame;
    private PropertyLoader prop = new PropertyLoader();
    private String pageUrl = prop.getProperty("pageUrl");

    public void setResponseForRecaptchaWithSelenium() throws InterruptedException, IOException {
        driver.get(pageUrl);
        Thread.sleep(2000);
        new WebDriverWait(driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[starts-with(@name, 'a-') and starts-with(@src, 'https://www.google.com/recaptcha')]")));
        try {
            //Check I am not a robot
            driver.findElement(By.id("recaptcha-anchor")).click();
            System.out.println("CHECKED I am not a robot box. That's not invisible captcha");
        } catch (Exception e) {
            //Invisible captcha detected
            System.out.println("Invisible ReCaptcha detected");
            isCaptchaInvisible = Boolean.TRUE;
            driver.switchTo().defaultContent();
            String myResponse = solveInvisibleRecatpcha(pageUrl);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.getElementById(\"g-recaptcha-response\").innerHTML=\"" + myResponse + "\";");
            clickSubmitButton();
        }
        Thread.sleep(2000);
        driver.switchTo().defaultContent();
        try {
            System.out.println("Trying to Submit the form");
            clickSubmitButton();
        } catch (Exception e) {
            System.out.println("You are not allowed to submit the form. Taking screenshot");
            //String textOfInstructions = driver.findElement(By.className("rc-imageselect-instructions")).getText();
            List<WebElement> newIframeList = driver.findElements(By.xpath("//iframe[starts-with(@name, 'c-') and starts-with(@src, 'https://www.google.com/recaptcha')]"));
            locationOfIFrame = newIframeList.get(0).getLocation();
            System.out.println("X: " + locationOfIFrame.x + "; Y: " + locationOfIFrame.y);
            new WebDriverWait(driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[starts-with(@name, 'c-') and starts-with(@src, 'https://www.google.com/recaptcha')]")));
            boolean resultOfTheVerification = false;
            while (!resultOfTheVerification) {
                resultOfTheVerification=takeScreenShotAndVerifyImageSquare();
            }
        }
    }

    private String solveImageRecatpcha(Boolean invisible, String url, Boolean recaptcha, Boolean canvas, String textOfInstructions, Integer recaptcharows, Integer recaptchacols) {
        int isInvisible = invisible ? 1 : 0;
        int isRecaptcha = recaptcha ? 1 : 0;
        int isCanvas = canvas ? 1 : 0;


        TwoCaptchaService service = new TwoCaptchaService(
                prop.getProperty("apiKey"),
                prop.getProperty("googleKey"),
                url,
                isInvisible,
                isRecaptcha,
                isCanvas,
                textOfInstructions,
                recaptcharows,
                recaptchacols
        );
        String responseToken = "";
        try {
            responseToken = service.solveImageCaptcha();
            System.out.println("The response token is: " + responseToken);
            return responseToken;
        } catch (InterruptedException e) {
            System.out.println("ERROR case 1");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR case 2");
            e.printStackTrace();
        }
        return responseToken;
    }

    private String solveInvisibleRecatpcha(String url) {
        TwoCaptchaService service = new TwoCaptchaService(
                prop.getProperty("apiKey"),
                prop.getProperty("googleKey"),
                url,
                1
        );
        String responseToken = "";
        try {
            responseToken = service.solveCaptcha();
            System.out.println("The response token is: " + responseToken);
            return responseToken;
        } catch (InterruptedException e) {
            System.out.println("ERROR case 1");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR case 2");
            e.printStackTrace();
        }
        return responseToken;
    }

    private void clickSubmitButton() throws InterruptedException, IOException {
        try {
            driver.findElement(By.id("recaptcha-demo-submit")).click();
        } catch (Exception e) {
            throw new IOException("Captcha Solver: No Submit button found.");
        }
        Thread.sleep(3000);
    }

    private boolean isRecaptchaSuccessful() throws InterruptedException {
        Thread.sleep(3000);
        List<WebElement> recaptchaSuccessElement = driver.findElements(By.className("recaptcha-success"));
        if (recaptchaSuccessElement.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private static <T, U> List<U>
    convertStringListToIntList(List<T> listOfString,
                               Function<T, U> function) {
        return listOfString.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    private void takeScreenShot(List<WebElement> imageElements, String imgName, Integer additionalWidth, Integer additionalHeight) throws IOException {
        if (imageElements.size() > 0) {
            System.out.println("Taking screenshot of Recaptha");
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage fullImg = ImageIO.read(screenshot);
            // Get the location of element on the page
            Point point = imageElements.get(0).getLocation();
            // Get width and height of the element
            int eleWidth = imageElements.get(0).getSize().getWidth() + additionalWidth;
            int eleHeight = imageElements.get(0).getSize().getHeight() + additionalHeight;
            // Crop the entire page screenshot to get only element screenshot
            BufferedImage eleScreenshot;
            int elementX, elementY;
            if (isCaptchaInvisible) {
                elementX = point.getX() + locationOfIFrame.x + 135;
                elementY = point.getY() + locationOfIFrame.y + 45;
                eleScreenshot = fullImg.getSubimage(elementX, elementY, eleWidth, eleHeight);
            } else {
                elementX = point.getX() + locationOfIFrame.x + 25;
                elementY = point.getY() + locationOfIFrame.y + 50;
                eleScreenshot = fullImg.getSubimage(elementX, elementY, eleWidth, eleHeight);
            }
            ImageIO.write(eleScreenshot, "gif", screenshot);
            // Copy the element screenshot to disk
            String imagePathName = "src/test/resources/screenshots/" + imgName + "." + "gif";
            File screenshotLocation = new File(imagePathName);
            FileHandler.copy(screenshot, screenshotLocation);
        }
    }

    private boolean takeScreenShotAndVerifyImageSquare() throws InterruptedException, IOException {
        List<WebElement> rowsOfTheImageElement = driver.findElements(By.xpath("//*[@id='rc-imageselect-target']/table/tbody/tr"));
        List<WebElement> colsOfTheImageElement = driver.findElements(By.xpath("//*[@id='rc-imageselect-target']/table/tbody/tr[1]/td"));
        System.out.println("Captcha size: " + rowsOfTheImageElement.size() + "x" + colsOfTheImageElement.size());
        List<WebElement> newImageFileElementList = driver.findElements(By.className("rc-imageselect-challenge"));
        List<WebElement> newImageInstructionsElementList = driver.findElements(By.className("rc-imageselect-instructions"));
        takeScreenShot(newImageFileElementList, "imageFile", 100, 100);
        takeScreenShot(newImageInstructionsElementList, "imageInstructions", 0, 0);
        System.out.println("CHECK select all squares");
        String myResponse = solveImageRecatpcha(false, pageUrl, true, false, "", rowsOfTheImageElement.size(), colsOfTheImageElement.size());
        System.out.println(myResponse);
        myResponse = myResponse.replace("click:", "");
        String[] numbers = myResponse.split("/");
        List<String> numberList = Arrays.asList(numbers);
        List<Integer> numberIntList = convertStringListToIntList(numberList, Integer::parseInt);
        List<WebElement> boxElementList = new ArrayList<WebElement>();
        for (int tr = 1; tr < rowsOfTheImageElement.size() + 1; tr++) {
            for (int td = 1; td < colsOfTheImageElement.size() + 1; td++) {
                boxElementList.add(driver.findElement(By.xpath("//*[@id='rc-imageselect-target']/table/tbody/tr[" + tr + "]/td[" + td + "]")));
            }
        }
        for (Integer integer : numberIntList) {
            boxElementList.get(integer - 1).click();
            Thread.sleep(getRandomMillis());
        }
        driver.findElement(By.id("recaptcha-verify-button")).click();
        Thread.sleep(7000);
        if (isRecaptchaSuccessful()) {
            System.out.println(".::Completed Challenge Successfully !");
            return true;
        } else {
            System.out.println(".::Captcha has been changed! RETRYING..");
            return false;
        }
    }
    private static int getRandomMillis() {
        return (int) (Math.random() * (4000)+200);
    }


}
