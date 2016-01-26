package com.spopoff;

/*
 * Copyright 2016 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import eu.eidas.auth.engine.core.eidas.EidasExtensionConfiguration;
import eu.eidas.auth.engine.core.eidas.impl.RequestedAttributesUnmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opensaml.common.xml.SAMLSchemaBuilder;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.impl.ExtensionsBuilder;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.schema.XSAny;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mrwc1264
 */
public class EidasExtensionTest {
    
    private final String resAttrs = "<eidas:RequestedAttributes xmlns:eidas=\"http://eidas.europa.eu/saml-extensions\"><eidas:RequestedAttribute Name=\"http://eidas.europa.eu/attributes/naturalperson/CurrentAddress\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" isRequired=\"false\"/><eidas:RequestedAttribute Name=\"http://eidas.europa.eu/attributes/naturalperson/Gender\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" isRequired=\"false\"/><eidas:RequestedAttribute Name=\"http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" isRequired=\"false\"/><eidas:RequestedAttribute Name=\"http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" isRequired=\"false\"/><eidas:RequestedAttribute Name=\"http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" isRequired=\"false\"/><eidas:RequestedAttribute Name=\"http://eidas.europa.eu/attributes/naturalperson/DateOfBirth\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\" isRequired=\"false\"/></eidas:RequestedAttributes>";
    
    public EidasExtensionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

     //@Test
     public void morceauXML() {
            SAMLSchemaBuilder.addExtensionSchema("/saml_eidas_extension.xsd");
            BasicParserPool ppMgr = new BasicParserPool();
            ppMgr.setNamespaceAware(true);
            try {
                ppMgr.setSchema(SAMLSchemaBuilder.getSAML11Schema());
            } catch (SAXException ex) {
                Logger.getLogger(EidasExtensionTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            InputStream is = new ByteArrayInputStream(resAttrs.getBytes());
//            Document domAttrsRaq = null;
//            try {
//                domAttrsRaq = ppMgr.parse(is);
//            } catch (XMLParserException e) {
//                System.err.println("Erreur dom="+e);
//            }
//            if(domAttrsRaq==null){
//                System.err.println("Erreur dom vide");
//                return;
//            }
            XSAny attrs = null;
            try {
                //attrs = (XSAny) unmarshaller.unmarshall(rootReq);
                attrs = (XSAny) org.opensaml.xml.util.XMLObjectHelper.unmarshallFromInputStream(ppMgr, is);
            } catch (UnmarshallingException e) {
                System.err.println("Erreur unmarshaller="+e);
                return;
            } catch (XMLParserException e) {
                System.err.println("Erreur parser="+e);
                return;
            }
            if(attrs==null){
                System.err.println("Erreur attrs vide");
                return;
            }
     }
     //@Test
     public void reTest(){
        //Extensions eidasExt = new ExtensionsBuilder().buildObject("urn:oasis:names:tc:SAML:2.0:protocol","Extensions","saml2p");
        EidasExtensionConfiguration eidasExt = new EidasExtensionConfiguration();
        eidasExt.configureExtension();
        SAMLSchemaBuilder.addExtensionSchema("/saml_eidas_extension.xsd");
        BasicParserPool ppMgr = new BasicParserPool();
        ppMgr.setNamespaceAware(true);
        try {
            ppMgr.setSchema(SAMLSchemaBuilder.getSAML11Schema());
        } catch (SAXException ex) {
            System.err.println("Erreur schema="+ex);
        }
        InputStream is = new ByteArrayInputStream(resAttrs.getBytes());
        Document domAttrsRaq = null;
        try {
            domAttrsRaq = ppMgr.parse(is);
        } catch (XMLParserException e) {
            System.err.println("Erreur dom="+e);
        }
        if(domAttrsRaq==null){
            System.err.println("Erreur dom vide");
            return;
        }
         RequestedAttributesUnmarshaller unMars = new RequestedAttributesUnmarshaller();
         XMLObject attrs = null;
         try {
             attrs = unMars.unmarshall(domAttrsRaq.getDocumentElement());
         } catch (UnmarshallingException e) {
                System.err.println("Erreur unMarsh error="+e);
         }
         //eidasExt.getUnknownXMLObjects().add(attrs);
     }
     @Test
     public void substringue(){
         String b64 = "SAMLResponse=PD94bW%3BSR65GE564G&RelayState=";
         System.out.println(b64.substring(13));
         int pos = b64.indexOf("&RelayState=");
         Assert.assertEquals(b64.substring(13, pos), "PD94bW%3BSR65GE564G");
     }
}
