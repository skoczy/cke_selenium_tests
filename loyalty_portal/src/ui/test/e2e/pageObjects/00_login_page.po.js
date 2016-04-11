var LoginPage = function () {
	browser.driver.ignoreSynchronization = true;
	//var webdriver = require('selenium-webdriver');
	//var browser = new webdriver.Builder().usingServer().withCapabilities({'browserName': 'chrome' }).build();
	
	var user = String.fromCharCode(115, 101, 108, 101, 110, 105, 117, 109, 46, 116, 101, 115, 116, 64, 115, 102, 114, 46, 99, 111, 109);
	var pwd = String.fromCharCode(49, 50, 113, 119, 65, 83, 33, 64);
    //var loginForm = element(by.id("login"));	
	//var username = element(by.id("username"));
    //var password = element(by.name("password"));
	var loginForm = browser.driver.findElement(by.id("login"));
	var username = browser.driver.findElement(by.name("j_username"));
	var password = browser.driver.findElement(by.name("j_password"));
	
    //var signInBtn = element(by.name("signin"));
    	
	this.getLoginPage = function () {browser.get('https://loyalty.statoil.pl:8443/login.v');};
    this.setUsername = function (name) {username.sendKeys(name);};
    this.setPassword = function (_password) {password.sendKeys(_password);};
	this.enterUsername = function () {this.setUsername(user);};
	this.enterPassword = function () {this.setPassword(pwd);};
	this.loginFormSubmit = function () {loginForm.submit();};
    
};

module.exports = LoginPage;