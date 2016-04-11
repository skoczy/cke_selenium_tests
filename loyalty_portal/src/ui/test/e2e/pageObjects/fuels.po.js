var Fuels = function () {
	
	var fuelsForm = element(by.name("fuelsForm"));
	
	this.fuelsFormSubmit = function () {
        fuelsForm.submit();
	}
		
	this.saveBtn = element(by.id('fuelsForm')).element(by.css('input[type="submit"]'));
	
	this.addNewFuel = element(by.linkText("Add new fuel"));
	
	var removeFuelLink = function (n) {
        //return element.all(by.css("form[name='servicesForm'] td.remove")).get(n - 1);
		return element.all(by.linkText("Remove")).get(n - 1);
    };
	
	this.removeFuel= function (n) {
        return removeFuelLink(n).click();
    };
	
};
module.exports = Fuels;