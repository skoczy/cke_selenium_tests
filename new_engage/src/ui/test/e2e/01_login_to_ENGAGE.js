var env = '-prep';

var timeoutFast = 300;
var timeoutSlow = 500;
var timeoutSlower = 1000;
var timeoutSlowest = 1500;

var LoginPagePO = require('./pageObjects/00_login_page.po.js');
var MainPagePO = require('./pageObjects/10_main_page.po.js');

describe('log in to app', function() {
  var login = new LoginPagePO();
		
  it('should login to app', function() {

	
	browser.driver.manage().window().maximize();
	browser.driver.switchTo().activeElement();	
		
	login.getLoginPage();
	browser.sleep(timeoutSlow);
    
	login.enterUsername();
	browser.sleep(timeoutSlow);
	
	login.enterPassword();
	browser.sleep(timeoutSlow);
	
	login.acceptTerms();
	browser.sleep(timeoutSlow);
	
	login.loginFormSubmit();
    browser.waitForAngular();
	
    expect(browser.getCurrentUrl()).toEqual('https://engage'+$env+'.sfrlabs.com/#!/dashboard');
	
	browser.sleep(timeoutSlow);
	
  });

});

describe('log out from app', function() {
  var mainpage = new MainPagePO();
		
  it('should go to login screen again', function() {
	
	mainpage.signOutLinkClick();
	browser.sleep(timeoutSlow);
    
    expect(browser.getCurrentUrl()).toEqual('https://engage'+$env+'.sfrlabs.com/#!/login');
	
	browser.sleep(timeoutSlow);
	
  });

});
