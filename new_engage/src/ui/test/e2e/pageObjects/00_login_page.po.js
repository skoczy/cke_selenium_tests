var env = '-prep';

var LoginPage = function () {
	
	var user 		= String.fromCharCode(69, 78, 71, 65, 71, 69, 95, 65, 68, 77, 73, 78, 95, 83, 69, 64, 83, 70, 82, 46, 67, 79, 77); //SE
	var pwd 		= String.fromCharCode(78, 101, 119, 117, 115, 101, 114, 64, 49, 50, 51, 52);
    var loginForm 	= element(by.name('formLogin'));	
	var username 	= element(by.name('email'));
    var password 	= element(by.name('password'));
	var terms		= element(by.name('hasAcceptedTerms'));
	var register 	= element(By.xpath('/html/body/div/div/main/section/p[1]/a'));
    	
	this.getLoginPage 		= function () {browser.get('https://engage'+env+'.sfrlabs.com/app/#!/login');};
    this.setUsername 		= function (name) {username.sendKeys(name);};
    this.setPassword 		= function (_password) {password.sendKeys(_password);};
	this.enterUsername 		= function () {this.setUsername(user);};
	this.enterPassword		= function () {this.setPassword(pwd);};
	this.acceptTerms		= function () {terms.click();};
	this.loginFormSubmit	= function () {loginForm.submit();}
    
};

module.exports = LoginPage;