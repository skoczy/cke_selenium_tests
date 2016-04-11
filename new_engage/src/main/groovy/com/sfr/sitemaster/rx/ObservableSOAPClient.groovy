package com.sfr.sitemaster.rx

import org.apache.http.Consts
import org.apache.http.Header
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.message.BasicHeader
import org.apache.http.nio.client.HttpAsyncClient
import org.apache.http.nio.client.methods.HttpAsyncMethods
import org.apache.http.ssl.SSLContexts
import rx.apache.http.ObservableHttp
import rx.apache.http.ObservableHttpResponse
import wslite.soap.SOAPMessageBuilder

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import java.nio.charset.Charset
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

/**
 * Created by piotr on 09/10/15.
 */
class ObservableSOAPClient {
    static final String ENVELOPE_ELEMENT_NAME = 'Envelope'
    static final String BODY_ELEMENT_NAME = 'Body'

    HttpAsyncClient httpClient;
    final HttpAsyncClientBuilder httpClientBuilder;
    final URL serviceURL;
    final String username;
    final String password;
    final List<Header> headers = []

    public ObservableSOAPClient(final String url) {
        this.serviceURL = new URL(url)
        this.httpClientBuilder = HttpAsyncClients.custom()
    }

    public def setCredentials = {
        String username, String password ->
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials new AuthScope(serviceURL.host, serviceURL.port), new UsernamePasswordCredentials(username, password);
            httpClientBuilder.defaultCredentialsProvider = credentialsProvider
            this
    }

    public def setAction(String action) {
        this.headers << new BasicHeader('SOAPAction', action)
        this
    }

    public def trustAllCerts = {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
            @Override
            boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException { true }
        }).build();
        httpClientBuilder.setSSLContext sslContext
        this
    }

    public def noHostVerification = {
        httpClientBuilder.setSSLHostnameVerifier(new HostnameVerifier() {
            @Override
            boolean verify(String s, SSLSession sslSession) {
                true
            }
        })
        this
    }

    public def buildAndStart = {
        httpClient = httpClientBuilder.build()
        httpClient.start()
        this
    }

    def sendAndObserve(Closure content) {
        def message = new SOAPMessageBuilder().build(content).toString()
        ObservableHttp.createRequest(HttpAsyncMethods.createPost(serviceURL.toExternalForm(), message, ContentType.create('text/xml', Consts.UTF_8)), httpClient)
                .toObservable()
                .flatMap { ObservableHttpResponse response -> response.content.map { byte[] bb -> new String(bb, Charset.forName('UTF-8')) } }
                .map { parseEnvelope it }
    }


    private parseEnvelope(String soapMessageText) {
        def envelopeNode = new XmlSlurper().parseText(soapMessageText)
        if (envelopeNode.name() != ENVELOPE_ELEMENT_NAME) {
            throw new IllegalStateException('Root element is ' + envelopeNode.name() + ', expected ' + ENVELOPE_ELEMENT_NAME)
        }
        if (envelopeNode."${BODY_ELEMENT_NAME}".isEmpty()) {
            throw new IllegalStateException(BODY_ELEMENT_NAME + ' element is missing')
        }
        return envelopeNode
    }

    static class Factory {
        public static ObservableSOAPClient get(
                final String soapAction, final String url, final String username, final String password) {
            return new ObservableSOAPClient(url)
                    .setAction(soapAction)
                    .setCredentials(username, password)
                    .trustAllCerts().noHostVerification().buildAndStart()
        }
    }

}
