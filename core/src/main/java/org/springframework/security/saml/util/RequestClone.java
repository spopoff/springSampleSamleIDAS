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
package org.springframework.security.saml.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mrwc1264
 */
public class RequestClone extends HttpServletRequestWrapper{
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(RequestClone.class);
    private byte[] rawData;
    private HttpServletRequest request;
    private ResettableServletInputStream servletStream;
    public RequestClone(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.servletStream = new ResettableServletInputStream();
//        _body = "";
//        BufferedReader bufferedReader;           
//        try {
//            bufferedReader = new BufferedReader( 
//                            new InputStreamReader( 
//                                    request.getInputStream()));
//        } catch (IOException ex) {
//            logger.error("Erreur getReader "+ex);
//            return;
//        }
//        String line;
//        try {
//            while ((line = bufferedReader.readLine()) != null){
//                _body += line;
//            }
//        } catch (IOException ex) {
//            logger.error("Erreur readLine "+ex);
//        }
    }
    public void resetInputStream(){
        servletStream.stream = new ByteArrayInputStream(rawData);
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }
 
    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return new BufferedReader(new InputStreamReader(servletStream));
    }    
    private class ResettableServletInputStream extends ServletInputStream {

        private InputStream stream;

        @Override
        public int read() throws IOException {
                return stream.read();
        }
    }
}
