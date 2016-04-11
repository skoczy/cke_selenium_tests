var GeneralData = function () {

	//var saveBtn1 = element(by.id('generaldata-misc')).element(by.css('input[type="submit"]'));
	//this.notificationsArea = element(by.id("main-notifications"));
	
	var openingHoursForm = element(by.id("generaldata-misc"));
	//var customFieldsForm = 
	
	
	this.openingHoursSubmit = function () {
        openingHoursForm.submit();
	}
	

	
	this.changeLogClick = function () {
        changeLogLink.click();
	}
		
	var open247 = element(by.model("site.openingInfo.alwaysOpen"));
	
	this.open247Click = function () {
        open247.click();
	}
	 
	this.weekdayOpen = element(by.model("site.openingInfo.openingTimes.weekdays.open"));
	this.weekdayClose = element(by.model("site.openingInfo.openingTimes.weekdays.close"));
	this.saturdayOpen = element(by.model("site.openingInfo.openingTimes.saturday.open"));
	this.saturdayClose = element(by.model("site.openingInfo.openingTimes.saturday.close"));
	this.sundayOpen = element(by.model("site.openingInfo.openingTimes.sunday.open"));
	this.sundayClose = element(by.model("site.openingInfo.openingTimes.sunday.close"));
	this.tempCloseFrom = element(by.id("tempClosedFrom"));
	this.tempCloseTo = element(by.id("tempClosedTo"));
	this.clearTempDatesBtn = element(by.linkText("Clear"));
	this.changeLog = element(by.linkText("Changelog"));
	this.xCoord = element(by.name("xCoord"));
	this.yCoord = element(by.name("yCoord"));
	
	this.saveBtn2 = element(by.id('custom-fields')).element(by.css('input[type="submit"]'));
};

module.exports = GeneralData;