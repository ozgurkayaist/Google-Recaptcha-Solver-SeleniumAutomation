# Google Recaptcha Solver Selenium Test Automation

![alt text](https://raw.githubusercontent.com/ozgurkayaist/Google-Recaptcha-Solver-SeleniumAutomation/master/img/bots.png)![alt text](https://raw.githubusercontent.com/ozgurkayaist/Google-Recaptcha-Solver-SeleniumAutomation/master/img/recaptcha.png)![alt text](https://raw.githubusercontent.com/ozgurkayaist/Google-Recaptcha-Solver-SeleniumAutomation/master/img/imnotarobot.png)

2Captcha.com's API used for solving Google's Invisible Recaptcha and Recaptcha v2 with Java Selenium Chrome Browser Test Automation.

  - Should solve image challenges
  - Should retry  for image challenges
  - Should click I am no robot checkbox
  - Should response to invisible captcha forms

# What do you need to start?

  - https://2captcha.com/ Account with minimum 3$ balance for 1000 Recaptcha. (Starting from 0.5 USD for 1000 solved CAPTCHAs by data entry workers) 
  - Development needs Java JDK v1.8.0, Maven, IntelliJ (preferable IDE)

You should also:
  - Change configuration and site settings from "src\test\resources\config.properties" file.
  - Use 2captcha.com Sandbox mode (for developers) for image selection challenges.
  - Use proxy settings for test automation running (not required, but recommended).
  
# Why do we need 2captcha.com API services?

Because, you can not fight with Google. You need to analyze and decide to answer like a human. If you have a software that has artificial intelligence and learns continuously, everything changes. All of these solutions are of course paid.

#### 2captcha.com solves different types of captchas:
Normal Captcha, Text Captcha, ReCaptcha V2, ReCaptcha V3, GeeTest, ReCaptcha V2 (old method), Solving ClickCaptcha, RotateCaptcha, FunCaptcha, FunCaptcha Token Method, KeyCaptcha, hCaptcha

#### Other popular alternatives
- anti-captcha.com
- deathbycaptcha.com
- azcaptcha.com
  
# How to configure?
##### Chrome Browser Driver

  - You have to download the correct driver for your operating system. There are 3 versions of the driver (Windows/Linux/Mac)
  - You have to download the correct version of the driver for your Chrome Browser. Just check your browser version before download the file to this location "src\test\resources\drivers". After that you should fix the filename and extension on this file "src\test\java\base\DriverBase" 
```sh
System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
```
##### config.properties file

  - Required fields: apiKey,googleKey,pageUrl
  - apikey is YOUR API KEY FROM 2captcha.com ->dashboard->Account Settings -> Copy API Key
  - googleKey is you should get it from target website(it will change for each application, you don't have to change it for this dummy project. But you should find it from the website code. More information:https://2captcha.com/2captcha-api#solving_recaptchav2_new)
  - pageUrl is the website which has recaptha on this test page. You should solve the invisible captcha on this demo just change the querystring which is at the and of the pageUrl invisible=false -> invisible=true
  - Optional fields: proxyIp, proxyPort, proxyUser, proxyPw
```sh
apiKey=YOUR_2captcha.com_API_KEY
googleKey=6Le-wvkSAAAAAPBMRTvw0Q4Muexq9bi0DJwx_mJ-
pageUrl=https://www.google.com/recaptcha/api2/demo?invisible=false
proxyIp=183.38.231.131
proxyPort=8888
proxyUser=username
proxyPw=password
```
##### Image Challenge Locally stored images

  You should find the Recaptcha's iframe elements screenshots at this folder. This code is always taking screenshots and overwriting the existing 2 files automatically. "src\test\resources\screenshots"
  - imageFile.gif: That's the 3x3 or 4x4 image challenge box.
  - imageInstructions.gif: That's the instructions for 2captcha.com workers. Example: "Select all images with crosswalks"

![alt text](https://raw.githubusercontent.com/ozgurkayaist/Google-Recaptcha-Solver-SeleniumAutomation/master/img/operation.png)

# How to run?
  That's a junit-java-maven-selenium Webdriver project. Default Test folder is "src\test\java\tests\Recaptcha2Test.java" just right click on IntelliJ's project explorer window or run it from command line with maven command.
  
  #### Warning: 
  I cannot guarantee that it will always work. However, I developed necessary implementations for possible scenarios. Response times from 2captcha.com should take between 30-90 seconds depending on your settings at 2captcha.com. https://2captcha.com/setting/percent_100
  
# What is the motivation behind this project?
If it is possible to do this just showing it to the community to raise awareness. 
What they can do: Bulk ticket purchases, limited number of product purchases, creating bot accounts

##### References:

https://github.com/2captcha/2captcha-api-examples

https://github.com/search?l=Java&q=2captcha&type=Repositories

https://2captcha.com/

