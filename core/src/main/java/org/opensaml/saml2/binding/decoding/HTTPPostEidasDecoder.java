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
package org.opensaml.saml2.binding.decoding;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HTTPInTransport;
import org.opensaml.xml.XMLObject;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mrwc1264
 */
public class HTTPPostEidasDecoder extends BaseSAML2MessageDecoder{
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HTTPPostEidasDecoder.class);
    private String body;
    public HTTPPostEidasDecoder(){
        super();
    }
    @Override
    protected boolean isIntendedDestinationEndpointURIRequired(SAMLMessageContext samlMsgCtx) {
        return false;
    }

    @Override
    protected void doDecode(MessageContext messageContext) throws MessageDecodingException {
        if (!(messageContext instanceof SAMLMessageContext)) {
            logger.error("Invalid message context type, this decoder only support SAMLMessageContext");
            throw new MessageDecodingException(
                    "Invalid message context type, this decoder only support SAMLMessageContext");
        }

        if (!(messageContext.getInboundMessageTransport() instanceof HTTPInTransport)) {
            logger.error("Invalid inbound message transport type, this decoder only support HTTPInTransport");
            throw new MessageDecodingException(
                    "Invalid inbound message transport type, this decoder only support HTTPInTransport");
        }
        int pos = body.indexOf("&RelayState=");
        logger.debug("le debut Body="+body.substring(0, 13)+" et la fin="+pos);
        String urlDec = null;
        try {
            urlDec = URLDecoder.decode(body.substring(13, pos), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new MessageDecodingException("Erreur sur URLdecode du body ", e);
        }
        byte[] b64 = Base64.getDecoder().decode(urlDec);
        ByteArrayInputStream bis = new ByteArrayInputStream(b64);
        XMLObject  sr = unmarshallMessage(bis);
        SAMLMessageContext samlMsgCtx = (SAMLMessageContext) messageContext;
        if(sr instanceof SAMLObject){
            samlMsgCtx.setInboundSAMLMessage((SAMLObject) sr);
            populateMessageContext(samlMsgCtx);
        }else{
            throw new MessageDecodingException("Erreur sur decode du body");
        }
    }

    public String getBindingURI() {
        return SAMLConstants.SAML2_POST_BINDING_URI;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
}
