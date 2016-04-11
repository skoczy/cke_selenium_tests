var SiteServices = function () {
	
	var siteServicesForm = element(by.name("servicesForm"));
	
	this.siteServicesFormSubmit = function () {
        siteServicesForm.submit();
	}
		
	this.saveBtn = element(by.id('servicesForm')).element(by.css('input[type="submit"]'));
	
	this.addNewService = element(by.linkText("Add new service"));
	
	var removeServiceLink = function (n) {
        //return element.all(by.css("form[name='servicesForm'] td.remove")).get(n - 1);
		return element.all(by.linkText("Remove")).get(n - 1);
    };
	
	this.removeService = function (n) {
        return removeServiceLink(n).click();
    };
	
};
module.exports = SiteServices;