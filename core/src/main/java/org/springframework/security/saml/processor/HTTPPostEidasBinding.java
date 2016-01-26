/*
 * Copyright 2010 Jonathan Tellier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.saml.processor;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.liberty.binding.encoding.HTTPPAOS11Encoder;
import org.opensaml.ws.message.decoder.MessageDecoder;
import org.opensaml.ws.message.encoder.MessageEncoder;
import org.opensaml.ws.transport.InTransport;
import org.opensaml.xml.parse.ParserPool;

import org.opensaml.saml2.binding.decoding.HTTPPostEidasDecoder;

public class HTTPPostEidasBinding extends HTTPSOAP11Binding {

	public HTTPPostEidasBinding(ParserPool parserPool) {
		super(new HTTPPostEidasDecoder(), new HTTPPAOS11Encoder());
	}

    public HTTPPostEidasBinding(MessageDecoder decoder, MessageEncoder encoder) {
        super(decoder, encoder);
    }

    @Override
    public boolean supports(InTransport transport) {
	    return true;
    }

    @Override
	public String getBindingURI() {
		return SAMLConstants.SAML2_POST_BINDING_URI;
	}

}
