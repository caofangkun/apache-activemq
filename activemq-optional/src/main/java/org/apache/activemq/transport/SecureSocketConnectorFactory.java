/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.transport;

import org.apache.activemq.spring.SpringSslContext;
import org.apache.activemq.transport.https.Krb5AndCertsSslSocketConnector;
import org.apache.activemq.util.IntrospectionSupport;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import org.apache.activemq.broker.SslContext;

import javax.net.ssl.SSLContext;
import java.util.Map;

public class SecureSocketConnectorFactory extends SocketConnectorFactory {

    private String keyPassword = System.getProperty("javax.net.ssl.keyPassword");
    private String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
    private String keyStore = System.getProperty("javax.net.ssl.keyStore");
    private String keyStoreType;
    private String secureRandomCertficateAlgorithm;
    private String trustCertificateAlgorithm;
    private String keyCertificateAlgorithm;
    private String protocol;
    private String auth;

    private SslContext context;

    public SecureSocketConnectorFactory(SslContext context) {
        this.context = context;
    }

    @Override
    public Connector createConnector() throws Exception {
        Krb5AndCertsSslSocketConnector sslConnector = new Krb5AndCertsSslSocketConnector();

        SSLContext sslContext = context == null ? null : context.getSSLContext();

        // Get a reference to the current ssl context factory...
        SslContextFactory factory = sslConnector.getSslContextFactory();

        if (context != null) {

            // Should not be using this method since it does not use all of the values
            // from the passed SslContext instance.....
            factory.setSslContext(sslContext);

        } else {
            IntrospectionSupport.setProperties(this, getTransportOptions());

            if (auth != null) {
                sslConnector.setMode(auth);
            }

            if (keyStore != null) {
                factory.setKeyStorePath(keyStore);
            }
            if (keyStorePassword != null) {
                factory.setKeyStorePassword(keyStorePassword);
            }
            // if the keyPassword hasn't been set, default it to the
            // key store password
            if (keyPassword == null && keyStorePassword != null) {
                factory.setKeyStorePassword(keyStorePassword);
            }
            if (keyStoreType != null) {
                factory.setKeyStoreType(keyStoreType);
            }
            if (secureRandomCertficateAlgorithm != null) {
                factory.setSecureRandomAlgorithm(secureRandomCertficateAlgorithm);
            }
            if (keyCertificateAlgorithm != null) {
                factory.setSslKeyManagerFactoryAlgorithm(keyCertificateAlgorithm);
            }
            if (trustCertificateAlgorithm != null) {
                factory.setTrustManagerFactoryAlgorithm(trustCertificateAlgorithm);
            }
            if (protocol != null) {
                factory.setProtocol(protocol);
            }
        }

        return sslConnector;
    }

    // Properties
    // --------------------------------------------------------------------------------

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSecureRandomCertficateAlgorithm() {
        return secureRandomCertficateAlgorithm;
    }

    public void setSecureRandomCertficateAlgorithm(String secureRandomCertficateAlgorithm) {
        this.secureRandomCertficateAlgorithm = secureRandomCertficateAlgorithm;
    }

    public String getKeyCertificateAlgorithm() {
        return keyCertificateAlgorithm;
    }

    public void setKeyCertificateAlgorithm(String keyCertificateAlgorithm) {
        this.keyCertificateAlgorithm = keyCertificateAlgorithm;
    }

    public String getTrustCertificateAlgorithm() {
        return trustCertificateAlgorithm;
    }

    public void setTrustCertificateAlgorithm(String trustCertificateAlgorithm) {
        this.trustCertificateAlgorithm = trustCertificateAlgorithm;
    }

    /**
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }

    /**
     * @param auth the auth to set
     */
    public void setAuth(String auth) {
        this.auth = auth;
    }
}