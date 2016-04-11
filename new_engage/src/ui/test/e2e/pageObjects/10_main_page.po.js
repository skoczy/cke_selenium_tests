var MainPage = function () {

	var dashboardLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[1]/li[1]/a"));
	var transactionsLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[1]/li[2]/a"));
	var cardsLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[1]/li[3]/a"));
	var invoicesLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[1]/li[4]/a"));
	var reportsLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[1]/li[5]/a"));
	var creditLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[1]/li[6]/a"));
	var searchAllLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[1]/li[7]/a"));
	var traffineoLink = element(By.xpath("/html/body/div/div[1]/nav[1]/ul[2]/li/a"));
	
	var partnersList 	= element(By.xpath("/html/body/header[2]/div[2]/div[2]/div[1]/span"));
	var sjofartsverket	= element(By.xpath('/html/body/header[2]/div[2]/div[2]/div[2]/div[2]/ul/li[225]/span'));
	
	var languagesList = element(By.xpath("/html/body/header[2]/div[6]/select"));
	var userProfileLink = element(By.xpath("/html/body/header[2]/div[5]/div[1]/span/span"));
	var signOutLink = element(By.xpath("/html/body/header[2]/div[3]/span"));	

	
	this.dashboardLinkClick = function () {dashboardLink.click();}
	this.transactionsLinkClick = function () {transactionsLink.click();}
	this.signOutLinkClick = function() {signOutLink.click();}
	this.cardsLinkClick = function () {cardsLink.click();}	
	this.invoicesLinkClick = function () {cardsLink.click();}
	this.reportsLinkClick = function () {reportsLink.click();}
	this.creditLinkClick = function () {creditLink.click();}
	this.searchAllLinkClick = function () {searchAllLink.click();}
	this.traffineoLinkClick = function () {traffineoLink.click();}
	
	this.partnersListClick = function () {partnersList.click();}
	this.chooseSjofart	= function () {sjofartsverket.click();};	
	
	this.languagesListClick = function () {languagesList.click();}
	this.userProfileLinkClick = function () {userProfileLink.click();}
	this.signOutLinkClick = function() {signOutLink.click();}
		
};
module.exports = MainPage;