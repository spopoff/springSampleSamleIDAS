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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author mrwc1264
 */
public class CloneInputStream extends InputStream{

    private InputStream input;
    private ByteArrayOutputStream output;
    private ByteBuffer buffer;

    public CloneInputStream(InputStream input) throws IOException {
        this.input = input;
        this.output = new ByteArrayOutputStream(input.available()); // Note: it's resizable anyway.
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        read(b, 0, 1);
        return b[0];
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (buffer == null) {
            int read = input.read(bytes, offset, length);

            if (read <= 0) {
                input.close();
                input = null;
                buffer = ByteBuffer.wrap(output.toByteArray());
                output = null;
                return -1;
            } else {
                output.write(bytes, offset, read);
                return read;
            }
        } else {
            int read = Math.min(length, buffer.remaining());

            if (read <= 0) {
                buffer.flip();
                return -1;
            } else {
                buffer.get(bytes, offset, read);
                return read;
            }
        }

    }    
}
