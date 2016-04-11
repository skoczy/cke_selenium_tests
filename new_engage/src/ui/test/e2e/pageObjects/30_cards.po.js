var CardsPage = function () {
	
    var orderNewCardLink	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[1]"));
	var blockOrReplaceLink 	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[2]"));
											
	var unblockLink 		= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[3]"));
    var orderNewPINLink 	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[4]"));
	var changeProfilesLink 	= element(By.xpath("/html/body/div/div[2]/main/section/div[2]/span[5]"));
	
	var dialogCancelLink 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/a"));
	var dialogPrevButton	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button[1]"));
	var dialogNextButton1 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button"));
	var dialogNextButton2	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button[2]"));
	var dialogCloseButton	= element(By.xpath("/html/body/div[2]/div[2]/div[1]/i"));
	
	// ORDERING CARD
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
	// ORDERING CARD
	
	// BLOCKING CARD // BLOCKING CARD // BLOCKING CARD // BLOCKING CARD // BLOCKING CARD // BLOCKING CARD 
	var blockDialog1stPartner 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div/a")); 
	var blockDialog1stAccount 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div/a"));
	var blockDialog1stCardgrp 	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div[2]/div/a"));
	var blockDialog1stCard		= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div[2]/div[2]/label/p[1]/span"));
	var blockDialogBlockType1	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[2]/form/table/tbody/tr[1]/td[2]/label[1]/span"));
	var blockDialogBlo// BLOCKING CARD // BLOCKING CARD // BLOCKING CARD // BLOCKING CARD // BLOCKING CARD 
	
	//UNBLOCKING CARD //UNBLOCKING CARD //UNBLOCKING CARD //UNBLOCKING CARD //UNBLOCKING CARD 
	var unblockDialog1stPartner	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div/a"));
	var unblockDialog1stAccount	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div/a"));
	var unblockDialog1stCardgrp	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div[2]/div/a"));
	var unblockDialog1stCard	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div[2]/div[2]/label/p[1]/span"));
	var unblDialogNextButton1	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button"));
	var unblDialogNextButton2	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button[2]"));
	
	this.unblockDialog1stPartnerClick 	= function() {unblockDialog1stPartner.click();}
	this.unblockDialog1stAccountClick	= function() {unblockDialog1stAccount.click();}
	this.unblockDialog1stCardgrpClick	= function() {unblockDialog1stCardgrp.click();}
	this.unblockDialog1stCardClick		= function() {unblockDialog1stCard.click();}
	this.unblDialogNextButton1Click		= function() {unblDialogNextButton1.click();}
	this.unblDialogNextButton2Click		= function() {unblDialogNextButton2.click();}
	//UNBLOCKING CARD //UNBLOCKING CARD //UNBLOCKING CARD //UNBLOCKING CARD //UNBLOCKING CARD 
	
	//ORDERING PIN //ORDERING PIN //ORDERING PIN //ORDERING PIN //ORDERING PIN //ORDERING PIN 
	var orderPinDialog1stPartner	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div/a"));
	var orderPinDialog1stAccount	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div/a"));
	var orderPinDialog1stCardgrp	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div[2]/div/a"));
	var orderPinDialog1stCard		= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/div[3]/div[1]/form/label/div/div/div[3]/div[1]/div[2]/div[2]/div[2]/label/p[1]/span"));
	var orderPinDialogNextButton1	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button"));
	var orderPinDialogNextButton2	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button[2]"));
	var orderPinDialogsendButton	= element(By.xpath("/html/body/div[2]/div[2]/div[2]/section/footer/div/span[1]/button[2]"));
	
	this.orderPinDialog1stPartnerClick	= function() {orderPinDialog1stPartner.click();}
	this.orderPinDialog1stAccountClick	= function() {orderPinDialog1stAccount.click();}
	this.orderPinDialog1stCardgrpClick	= function() {orderPinDialog1stCardgrp.click();}
	this.orderPinDialog1stCardClick	= function() {orderPinDialog1stCard.click();}
	this.orderPinDialogNextButton1Click	= function() {orderPinDialogNextButton1.click();}
	this.orderPinDialogNextButton2Click	= function() {orderPinDialogNextButton2.click();}
	this.orderPinDialogsendButtonClick	= function() {orderPinDialogsendButton.click();}
	//ORDERING PIN //ORDERING PIN //ORDERING PIN //ORDERING PIN //ORDERING PIN //ORDERING PIN 
	
	this.orderNewCardLinkClick 		= function() {orderNewCardLink.click();}
	this.blockOrReplaceLinkClick	= function() {blockOrReplaceLink.click();}
	this.unblockLinkClick 			= function() {unblockLink.click();}
	this.orderNewPINLinkClick 		= function() {orderNewPINLink.click();}	
	this.changeProfilesLinkClick 	= function() {changeProfilesLink.click();}
	
	this.dialogCancelLinkClick 		= function() {dialogCancelLink.click();}
	this.dialogPrevButtonClick 		= function() {dialogPrevButton.click();}
	this.dialogNextButton1Click 	= function() {dialogNextButton1.click();}
	this.dialogNextButton2Click 	= function() {dialogNextButton2.click();}
	this.dialogCloseButtonClick 	= function() {dialogCloseButton.click();}
	
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