var TransactionsPage = function () {
	
	var searchForm		= element(By.name('transactionSearchForm'));
    var transFrom		= element(By.name('fromDate'));
	var transTo 		= element(By.name('toDate'));
	var transCardNumber	= element(By.model('cardNumberSearchTerm'));
	var transCardText	= element(By.model('cardTextLineSearchTerm'));

	var loadAllResults  = element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[1]/div[3]/span'));
	var searchInAll		= element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[1]/div[1]/div/input'));
	var showHideCols	= element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[2]/div/div[1]/div[1]'));
	var exportToCSV		= element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[2]/div/div[2]/div'));
	var exportToXLSX	= element(By.xpath('/html/body/div/div[2]/main/section/div[3]/div/div[2]/div/div[2]/div/div[3]/div'));
	
	this.clearTransFrom	= function () {transFrom.clear();};
	this.setTransFrom	= function (from_date) {transFrom.sendKeys(from_date);};
	this.clearTransTo	= function () {transTo.clear();};
	this.setTransTo		= function (to_date) {transTo.sendKeys(to_date);};
	this.setCardNumber	= function (card_no) {transCardNumber.sendKeys(card_no);};
	this.setCardText	= function (card_tx) {transCardText.sendKeys(card_tx);};
	this.clickSearch 	= function () {searchForm.submit();};
	
	this.clickLoadAll 	= function () {loadAllResults.click();};
	this.setSearchInAll	= function (text) {searchInAll.sendKeys(text);};
	this.clickShowHide	= function () {showHideCols.click();};
	this.clickExpCSV	= function () {exportToCSV.click();};
	this.clickExpXLSX	= function () {exportToXLSX.click();};
	
};

module.exports = TransactionsPage;