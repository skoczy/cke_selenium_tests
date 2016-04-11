var timeoutFast = 300;
var timeoutSlow = 500;
var timeoutSlower = 1000;
var timeoutSlowest = 1500;

var LoginPagePO = require('./pageObjects/00_login_page.po.js');
var MainPagePO = require('./pageObjects/10_main_page.po.js');
var CardsPO = require('./pageObjects/30_cards.po.js');

describe('log in to app', function() {
  var login = new LoginPagePO();
		
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
	
    expect(browser.getCurrentUrl()).toEqual('https://engage-test.sfrlabs.com/app/#!/dashboard');
	
	browser.sleep(timeoutFast);
	
  });

});

describe('click on Cards link', function() {
  var mainpage = new MainPagePO();
		
  it('should transfer us to Cards page', function() {
	
	mainpage.cardsLinkClick();
	browser.sleep(timeoutFast);
    
    expect(browser.getCurrentUrl()).toEqual('https://engage-test.sfrlabs.com/app/#!/cards');
	
	browser.sleep(timeoutFast);
	
  });

});

describe('click on Order New Card button', function() {
  var cards = new CardsPO();
		
  it('should raise new popu window', function() {
	
	cards.orderNewCardLinkClick();
	browser.sleep(timeoutSlowest);
    
	cards.dialog1stPartnerClick();
	browser.sleep(timeoutSlowest);
	
	cards.dialog1stAccountClick();
	browser.sleep(timeoutSlowest);
	
	cards.dialog1stCardgrpClick();
	browser.sleep(timeoutSlowest);
	
	cards.dialogNextButtonClick();
	browser.waitForAngular();
	
	//cards.chooseAddress2();
	//browser.sleep(timeoutSlow);
	
	//cards.chooseAddress1();
	//browser.sleep(timeoutSlow);
	
	cards.enterTextAttention("Some text...");
	browser.sleep(timeoutSlowest);
	
	cards.enterTextTextline2("Text line 2...");
	browser.sleep(timeoutSlowest);
	
	cards.selectCardtype();
	browser.sleep(timeoutSlowest);
	
	cards.dialogNextButtonClick();
	browser.waitForAngular();
	
	//Send
	cards.dialogNextButtonClick();
	browser.waitForAngular();
	
	//Read the confirmation
	expect(element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[4]")).getText()).toEqual("Your order was successfully sent to the card system.");
	
	browser.sleep(timeoutSlowest);
	
	cards.dialogCloseButtonClick();
    browser.sleep(timeoutSlowest);
	
  });

});
