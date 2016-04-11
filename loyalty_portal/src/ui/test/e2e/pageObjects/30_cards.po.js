var CardsPage = function () {
	
    var orderNewCardLink	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[1]"));
	var blockOrReplaceLink 	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[2]"));
	var unblockLink 		= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[3]"));
    var orderNewPINLink 	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[4]"));
	var changeProfilesLink 	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[5]"));
	
	var dialogCancelLink 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/a"));
	var dialogPrevButton	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button[1]"));
	var dialogNextButton 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button[2]"));
	var dialogCloseButton	= element(By.xpath("/html/body/div[2]/div[2]/div[1]/i"));
	
	var dialog1stPartner = element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/div/div[2]/div[1]/div[1]/a"));
	var dialog1stAccount = element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/div/div[2]/div[1]/div[2]/div[1]/a"));
	var dialog1stCardgrp = element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/div/div[2]/div[1]/div[2]/div[2]/div/label/input"));
	
	var dialogAddress1		= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/div/div/div[2]/p[1]/label[1]"));
	var dialogAddress2		= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/div/div/div[2]/p[1]/label[2]"));
	var dialogAttention 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/div/div/div[2]/p[2]/label/input"));
	this.setAttention		= function(text) {dialogAttention.sendKeys(text);};
	var dialogTextline2 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/div/div/div[2]/p[3]/label/input"));
	this.dialogTextline2	= function(text) {dialogTextline2.sendKeys(text);};
	var dialogCardtype 		= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/div/div/div[2]/p[4]/label/select"));
	var confirmationSent	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[4]"));
	
	// BLOCKING CARD
	var blockDialog1stPartner 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/div/div/div[3]/div[1]/div/a")); 
	var blockDialog1stAccount 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/div/div[2]/div[1]/div[2]/div[1]/a"));
	var blockDialog1stCardgrp 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/div/div/div[3]/div[1]/div[2]/div[2]/div[1]/a"));
	var blockDialog1stCard		= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/div/div/div[3]/div[1]/div[2]/div[2]/div[2]/label/p[1]/span"));
	var blockDialogBlockType1	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/table/tbody/tr[1]/td[2]/label[1]/span"));
	var blockDialogBlockType2	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/table/tbody/tr[1]/td[2]/label[2]/span"));
	
	
	// BLOCKING CARD
		
	this.orderNewCardLinkClick 		= function() {orderNewCardLink.click();}
	this.blockOrReplaceLinkClick	= function() {blockOrReplaceLink.click();}
	this.unblockLinkClick 			= function() {unblockLink.click();}
	this.orderNewPINLinkClick 		= function() {orderNewPINLink.click();}	
	this.changeProfilesLinkClick 	= function() {changeProfilesLink.click();}
	
	this.dialogCancelLinkClick 	= function() {dialogCancelLink.click();}
	this.dialogPrevButtonClick 	= function() {dialogPrevButton.click();}
	this.dialogNextButtonClick 	= function() {dialogNextButton.click();}
	this.dialogCloseButtonClick = function() {dialogCloseButton.click();}
	
	this.dialog1stPartnerClick 	= function() {dialog1stPartner.click();}
	this.dialog1stAccountClick 	= function() {dialog1stAccount.click();}
	this.dialog1stCardgrpClick	= function() {dialog1stCardgrp.click();}
	
	this.blockDialog1stPartnerClick 	= function() {blockDialog1stPartner.click();}
	this.blockDialog1stAccountClick 	= function() {blockDialog1stAccount.click();}
	this.blockDialog1stCardgrpClick		= function() {blockDialog1stCardgrp.click();}
	this.blockDialog1stCardClick		= function() {blockDialog1stCard.click();}
	
	this.chooseAddress1			= function() {dialogAddress1.click();}
	this.chooseAddress2			= function() {dialogAddress2.click();}
	this.enterTextAttention		= function(text) {this.setAttention(text);}
	this.enterTextTextline2		= function(text) {this.dialogTextline2(text);}
	this.selectCardtype			= function() {dialogCardtype.click(); dialogCardtype.$('[value="X3K"]').click();}

};
module.exports = CardsPage;