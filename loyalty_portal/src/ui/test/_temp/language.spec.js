/*global browser, by */

'use strict';

xdescribe('language selector', function() {
  var selector = element(by.model('userLanguage'));

  it('should list available languages', function() {
    browser.get('/#/login');

    var lang = selector.$('option:checked').getText(),
        options = selector.all(by.tagName('option')).getText();

    expect(lang).toBe('English');
    expect(selector).toBeDefined();
    expect(options).toContain('English');
    expect(options).toContain('Русский');
  });

  it('should change the text of the login button', function() {
    browser.get('/#/login');
    var submit = element(by.name('signin'));

    expect(submit.getAttribute('value')).toBe('Sign in');

    element(by.cssContainingText('option', 'Русский')).click();

    expect(selector.$('option:checked').getText()).toBe('Русский');
    expect(selector.$('option:checked').getText()).toBe('Русский');
    expect(submit.getAttribute('value')).toBe('Войти');
  });
});
