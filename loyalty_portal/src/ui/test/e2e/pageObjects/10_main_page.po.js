var MainPage = function () {
browser.driver.ignoreSynchronization = true;
	//var webdriver = require('selenium-webdriver');
	//var browser = new webdriver.Builder().usingServer().withCapabilities({'browserName': 'chrome' }).build();

	//var editProfileLink = element(By.xpath("/html/body/div/div[1]/div/div/div[1]/div/div/a[1]"));
	var editProfileLink =  browser.driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div[1]/div/div/a[1]"));
	//var logoutLink = element(By.xpath("/html/body/div/div[1]/div/div/div[1]/div/div/a[2]"));
	var logoutLink =  browser.driver.findElement(By.xpath("/html/body/div/div[1]/div/div/div[1]/div/div/a[2]"));
	//var extraCalatogueLink = element(By.xpath("/html/body/div/div[1]/div/div/section/a/section"));
	var extraCalatogueLink =  browser.driver.findElement(By.xpath("/html/body/div/div[1]/div/div/section/a/section"));
	
	this.editProfileLinkClick = function () {editProfileLink.click();};
	this.logoutLinkClick = function () {logoutLink.click();};
	this.extraCalatogueLinkClick = function() {extraCalatogueLink.click();};
		
};
module.exports = MainPage;

