var timeoutFast = 300;
var timeoutSlow = 500;
var timeoutSlower = 1000;
var timeoutSlowest = 1500;
var timeoutSlowest1 = 3000;
var env = '-prep';

var LoginPage = require('./pageObjects/00_login_page.po.js');
var MainPage = require('./pageObjects/10_main_page.po.js');
var Transactions = require('./pageObjects/20_transactions.po.js');

var login = new LoginPage();
var mainpage = new MainPage();
var trans = new Transactions();
  
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
	
	login.acceptTerms();
	browser.sleep(timeoutSlow);	
	
	login.loginFormSubmit();
    browser.waitForAngular();
	
    expect(browser.getCurrentUrl()).toEqual('https://engage'+env+'.sfrlabs.com/app/#!/dashboard');
	
	browser.sleep(timeoutFast);
	
  });

});


describe('click on Transactions link', function() {
		
  it('should transfer us to Transactions page', function() {
	
	mainpage.partnersListClick();
	browser.sleep(timeoutFast);
	browser.waitForAngular();
	
	mainpage.chooseSjofart();
	browser.sleep(timeoutFast);
	browser.waitForAngular();
	
	mainpage.transactionsLinkClick();
	browser.sleep(timeoutFast);
	console.log('\n clicked on Transactions menu option');	
    
    expect(browser.getCurrentUrl()).toEqual('https://engage'+env+'.sfrlabs.com/app/#!/transactions');
	
	browser.sleep(timeoutFast);
	
  });

});

describe('enter from Date', function() {
		
  it('enter from Date', function() {
	
	trans.clearTransFrom();
	trans.setTransFrom('2014-01-01 12:25:00');
	browser.sleep(timeoutSlowest);
	browser.waitForAngular();
	console.log('\n set the From date');
	
  });
});	


describe('enter to Date', function() {
		
  it('enter to Date', function() {
	
	trans.clearTransTo();
	trans.setTransTo('2016-04-01 09:50:15');
	browser.sleep(timeoutSlowest);
	browser.waitForAngular();
	console.log('\n set the To date');
	
  });
});	


describe('click on SEARCH button', function() {
		
  it('should get the results', function() {
	
	trans.clickSearch();
	browser.sleep(timeoutSlowest1);
	browser.waitForAngular();
	console.log('\n clicked Search button');		
 	
	//check if search summary is visible
	expect(element(By.xpath('/html/body/div/div[2]/main/section/div[2]')).isDisplayed()).toBeTruthy();
	console.log('\n search summary is visible');						 
	 
  });
});

describe('click on Load all results button', function() {
		
  it('should load all results into the table', function() {
	
	browser.executeScript('window.scrollTo(0,700);');
	trans.clickLoadAll();
	browser.sleep(timeoutSlowest1);
	browser.waitForAngular();
	console.log('\n clicked Load all results');		
 	
	//check if Search in all results input is visible
	expect(element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[1]/div[1]/div/input')).isDisplayed()).toBeTruthy();
	console.log('\n Search in results input is visible');						 
	 
  });
}); 

describe('check for buttons presence', function() {
		
  it('should chech if buttons are present', function() {
 	
	//Show/hide columns button
	expect(element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[2]/div/div[1]/div[1]')).isDisplayed()).toBeTruthy();
	console.log('\n Show/hide columns button is visible');						 
	 
	//Export to CSV button
	expect(element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[2]/div/div[2]/div')).isDisplayed()).toBeTruthy();
	console.log('\n Export to CSV button is visible');		 
	 
	//Export to XLSX button
	expect(element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[2]/div/div[3]/div')).isDisplayed()).toBeTruthy();
	console.log('\n Export to XLSX button is visible');		
	 
  });
}); 

  
 
describe('search for Uppsala', function() {
		
  it('should filter results with Uppsala station', function() {
	
	trans.setSearchInAll('Uppsala');
	browser.sleep(timeoutSlowest1);
	browser.waitForAngular();
	console.log('\n filtered search results for Uppsala');		
 	
	//check if results are correctly filtered
	expect(element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[3]/table/tfoot/tr/td/div/span[3]/span[3]')).getText()).toEqual('6');
	console.log('\n results are correctly filtered for Uppsala');	
	browser.pause();	
	 
  });  
});