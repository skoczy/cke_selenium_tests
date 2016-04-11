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
	browser.sleep(timeoutSlowest);
    
	login.enterUsername();
	browser.sleep(timeoutSlowest);
	
	login.enterPassword();
	browser.sleep(timeoutSlowest);
	
	login.loginFormSubmit();
    browser.waitForAngular();
	
    expect(browser.getCurrentUrl()).toEqual('https://loyalty.statoil.pl:8443/benefits.v');
	
	browser.sleep(timeoutSlowest);
	
  });

});

describe('log out from loyalty portal', function() {
  var mainpage = new MainPagePO();
		
  it('should go to login screen again', function() {
	
	mainpage.logoutLinkClick();
	browser.sleep(timeoutSlowest);
    
    expect(browser.getCurrentUrl()).toEqual('https://loyalty.statoil.pl:8443/signin.v');
	
	browser.sleep(timeoutSlowest);
	
  });

});
