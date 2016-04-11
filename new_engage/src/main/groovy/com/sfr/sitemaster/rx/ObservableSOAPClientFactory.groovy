package com.sfr.sitemaster.rx
/**
 * Created by piotr on 09/10/15.
 */
class ObservableSOAPClientFactory {
    public ObservableSOAPClient get(final String soapAction, final String url, final String username, final String password) {
        return new ObservableSOAPClient(url).setAction(soapAction).setCredentials(username, password).trustAllCerts().noHostVerification().buildAndStart()
    }
}
