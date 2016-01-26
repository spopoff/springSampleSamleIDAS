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
package org.springframework.security.saml.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.io.MarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.util.SAMLUtil;
import org.springframework.web.context.ContextLoader;

/**
 *
 * @author mrwc1264
 */
public class MetadataServlet extends HttpServlet {
    protected final static Logger log = LoggerFactory.getLogger(MetadataServlet.class);
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
        SAMLContextProvider contextPro = (SAMLContextProvider) ctx.getBean("contextProvider");
        SAMLMessageContext context = null;
        try {
            context = contextPro.getLocalEntity(request, response);
        } catch (MetadataProviderException e) {
            log.error("Error getLocalEntity ", e);
            throw new ServletException("Error getLocalEntity ", e);
        }
        MetadataManager manager = (MetadataManager) ctx.getBean("metadata");
        KeyManager keyman = (KeyManager) ctx.getBean("keyManager");
        try {
            String entityId = context.getLocalEntityId();
            log.debug("Affiche metadata de "+entityId);
            displayMetadata(entityId, response.getWriter(), manager, keyman);
        } catch (Exception e) {
            log.error("Error initializing metadata ", e);
            throw new ServletException("Error initializing metadata ", e);
        }
    }
    /**
     * Method writes metadata document into given writer object.
     *
     * @param spEntityName id of entity to display metadata for
     * @param writer       output for metadata
     * @throws ServletException error retrieving or writing the metadata
     */
    protected void displayMetadata(String spEntityName, PrintWriter writer, MetadataManager manager, KeyManager keyman) throws ServletException {
        try {
            EntityDescriptor descriptor = manager.getEntityDescriptor(spEntityName);
            if (descriptor == null) {
                throw new ServletException("Metadata entity with ID " + manager.getHostedSPName() + " wasn't found");
            } else {
                writer.print(getMetadataAsString(descriptor, manager, keyman));
            }
        } catch (MarshallingException e) {
            log.error("Error marshalling entity descriptor", e);
            throw new ServletException(e);
        } catch (MetadataProviderException e) {
            log.error("Error retrieving metadata", e);
            throw new ServletException("Error retrieving metadata", e);
        }
    }
    protected String getMetadataAsString(EntityDescriptor descriptor, MetadataManager manager, KeyManager keyman) throws MarshallingException {
        return SAMLUtil.getMetadataAsString(manager, keyman , descriptor, null);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
