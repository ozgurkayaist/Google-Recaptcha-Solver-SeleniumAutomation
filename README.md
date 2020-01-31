# Google Recaptcha Solver Selenium Test Automation
2Captcha.com's API used for solving Google's Invisible Recaptcha and Recaptcha v2 with Java. Should click the right images to user verify and check I am not robot.


Test materials:
  1-https://2captcha.com/ Account with minimum 3$ balance for 1000 Recaptcha. (Starting from 0.5 USD for 1000
solved CAPTCHAs by data entry workers)
  2- Java JDK v1.8.0 and Maven with IntelliJ IDE
  
 Configuration requirements:
 
  Configuration file location: src\test\resources\config.properties

  Required configurations:

  apiKey=YOUR API KEY FROM 2captcha.com 
  2captcha.com ->dashboard->Account Settings -> Copy API Key

  googleKey=6Le-wvkSAAAAAPBMRTvw0Q4Muexq9bi0DJwx_mJ-  
  (this is the default key for this sample project. )

  pageUrl=https://www.google.com/recaptcha/api2/demo?invisible=true
  This is the target website which has recaptcha on this page. You should set invisible=false to see "I am not robot checkbox". If you set invisible=true  I am not robot page should not shown.


  Optional configurations:
  These configurations are not required at the begining. But it's recommended to use, because image selection challenge should be harder and continue when you have send lotf of requests to google on the same IP address.
  
proxyIp=183.38.231.131
proxyPort=8888
proxyUser=username
proxyPw=password

  
