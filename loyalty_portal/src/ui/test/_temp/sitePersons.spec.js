describe('Site Persons', function () {

    var mainPageF = require('./pageObjects/main.page.js');
    var generalPageF = require('./pageObjects/general.page.js');
    var loginPageF = require('./pageObjects/loginPage.page.js');
    var sitePersonsF = require('./pageObjects/sitePersons.page.js');
    var mainPage = new mainPageF();
    var generalPage = new generalPageF();
    var loginPage = new loginPageF();
    var sitePersonsPage = new sitePersonsF();

    it('should add new site person', function () {
        loginPage.get();
        browser.waitForAngular();
        loginPage.enterUsernameAndPassword();
        loginPage.signIn();
        mainPage.setSearch('57052');
        mainPage.clickSearch();
        generalPage.getSitePersonsLink().click();
        sitePersonsPage.addNewPerson();
        sitePersonsPage.getLastPersonIndex().then(function (n) {
            sitePersonsPage.setPersonRole(n, "Caretaker2");
            sitePersonsPage.setPersonName(n, "Person Name");
            sitePersonsPage.setPersonPhoneCC(n, "48");
            sitePersonsPage.setPersonPhone(n, "12345678");
            sitePersonsPage.setPersonEmail(n, "example@mail.com");

        });
        sitePersonsPage.setPersonEmail(0, "example@mail.com");
        sitePersonsPage.setPersonEmail(1, "example@mail.com");
        sitePersonsPage.setPersonEmail(2, "example@mail.com");
        sitePersonsPage.save();
        mainPage.setSearch('57052');
        mainPage.clickSearch();
        generalPage.getSitePersonsLink().click();
        sitePersonsPage.getLastPersonIndex().then(function (n) {
            expect(sitePersonsPage.getPersonRole(n)).toEqual('Caretaker2');
            expect(sitePersonsPage.getPersonName(n)).toEqual('Person Name');
            expect(sitePersonsPage.getPersonPhoneCC(n)).toEqual('48');
            expect(sitePersonsPage.getPersonPhone(n)).toEqual('12345678');
            expect(sitePersonsPage.getPersonEmail(n)).toEqual('example@mail.com');
        });
    });

    it('should remove a site person', function () {
        loginPage.get();
        browser.waitForAngular();
        loginPage.enterUsernameAndPassword();
        loginPage.signIn();
        mainPage.setSearch('57052');
        mainPage.clickSearch();
        generalPage.getSitePersonsLink().click();
        var countBefore;
        sitePersonsPage.getLastPersonIndex().then(function (n) {
            countBefore = n;
        });
        //browser.pause();
        sitePersonsPage.removePerson(3);
        sitePersonsPage.save();
        mainPage.setSearch('57052');
        mainPage.clickSearch();
        generalPage.getSitePersonsLink().click();
        sitePersonsPage.getLastPersonIndex().then(function (n) {
            expect(n).toEqual(countBefore - 1);
        });
    })
});
