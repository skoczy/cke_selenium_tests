var timeoutFast = 300;
var timeoutSlow = 500;
var timeoutSlower = 1000;
var timeoutSlowest = 1500;
var timeoutSlowest1 = 3000;
var env = '-prep';

var LoginPagePO = require('./pageObjects/00_login_page.po.js');
var MainPagePO = require('./pageObjects/10_main_page.po.js');
var CardsPO = require('./pageObjects/30_cards.po.js');

var login = new LoginPagePO();
var mainpage = new MainPagePO();
var cards = new CardsPO();
  
describe('log in to app', function() {

		
  it('should login to app', function() {
	
	browser.driver.manage().window().maximize();
	browser.driver.switchTo().activeElement();	
		
	login.getLoginPage();
	browser.sleep(timeoutFast);
    
	login.enterUsername();
	browser.sleep(timeoutFast);
	
	login.enterPassword();
	browser.sleep(timeoutFast);
	
	login.loginFormSubmit();
    browser.waitForAngular();
	
    expect(browser.getCurrentUrl()).toEqual('https://engage'+env+'.sfrlabs.com/app/#!/dashboard');
	
	browser.sleep(timeoutFast);
	
  });

});

describe('click on Cards link', function() {
		
  it('should transfer us to Cards page', function() {
	
	mainpage.cardsLinkClick();
	browser.sleep(timeoutFast);
	console.log('\n clicked on Cards menu option');	
    
    expect(browser.getCurrentUrl()).toEqual('https://engage'+env+'.sfrlabs.com/app/#!/cards');
	
	browser.sleep(timeoutFast);
	
  });

});

describe('click on Unblock button', function() {
		
  it('should display blocking dialog window', function() {
	
	cards.unblockLinkClick();
	browser.sleep(timeoutSlowest);
	browser.waitForAngular();
	console.log('\n clicked on Block or replace button');
	
  });
});	

describe('click on 1st available partner', function() {
		
  it('should extend the tree and show accounts', function() {
	
	browser.sleep(timeoutSlowest); 
	cards.unblockDialog1stPartnerClick();
	browser.sleep(timeoutSlowest);
	browser.waitForAngular();
	console.log('\n clicked on 1st partner');
	
  });
});	

describe('click on 1st available account', function() {
		
  it('should extend the tree and show cardgroups', function() {
	  	
	cards.unblockDialog1stAccountClick();
	browser.sleep(timeoutSlowest1);
	browser.waitForAngular();
	console.log('\n clicked on 1st account');
	
  });
});		

describe('click on 1st available cardgroup', function() {
		
  it('should extend the tree and show cards', function() {
	  	
	cards.unblockDialog1stCardgrpClick();
	browser.sleep(timeoutSlowest1);
	browser.waitForAngular();
	console.log('\n clicked on 1st card group');	
	
  });
});		

describe('click on 1st available card', function() {
		
  it('should select the card', function() {
	  	
	cards.unblockDialog1stCardClick();
	browser.waitForAngular();
	console.log('\n clicked on 1st card');	
	
  });
});	
	
describe('click on NEXT button', function() {
		
  it('should show the summary window', function() {
	
	cards.unblDialogNextButton1Click();
	browser.sleep(timeoutSlowest1);
	browser.waitForAngular();
	console.log('\n clicked on NEXT button to see summary');		
  
  });
});	

describe('click on SEND button', function() {;
		
  it('should end the request from Engage to PALS and receive the confirmation', function() {
	
	cards.dialogNextButton2Click();
	browser.sleep(timeoutSlowest1);
	browser.waitForAngular();
	console.log('\n clicked on SEND button to send to PALS');		
 
	
	//Read the confirmation
	expect(element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[3]")).getText()).toEqual("Your request was successfully sent to the card system.");
	console.log('\n confirmation message has been read');						 
	 
  });
});	
	
	
describe('it', function() {
		
  it('should finish the process', function() {	
	browser.sleep(timeoutSlowest);
	cards.dialogCloseButtonClick();
    browser.sleep(timeoutSlowest);
	
  });

});
