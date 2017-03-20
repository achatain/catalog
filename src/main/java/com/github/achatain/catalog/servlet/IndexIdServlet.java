/*
 * https://github.com/achatain/catalog
 *
 * Copyright (C) 2017 Antoine Chatain (achatain [at] outlook [dot] com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.achatain.catalog.servlet;

import com.github.achatain.catalog.jms.IndexMessage;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.gson.Gson;
import org.apache.commons.lang3.CharEncoding;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Logger;

import static java.lang.String.format;

@Singleton
public class IndexIdServlet extends AuthenticatedJsonHttpServlet {

    public static final String REGEX_PATH = "\\%s\\%s\\/collections\\/\\w+\\/indexes\\/.+";

    private static final transient Logger LOG = Logger.getLogger(IndexIdServlet.class.getName());

    private final transient Destination indexCreateDestination;
    private final transient Destination indexDropDestination;
    private final transient JMSContext jmsContext;

    @Inject
    IndexIdServlet(final SessionService sessionService, @Named("pretty") final Gson gson, @Named("indexCreate") final Destination indexCreateDestination, @Named("indexDrop") final Destination indexDropDestination, final JMSContext jmsContext) {
        super(gson, sessionService);
        this.indexCreateDestination = indexCreateDestination;
        this.indexDropDestination = indexDropDestination;
        this.jmsContext = jmsContext;
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final String colId = extractCollectionIdFromRequest(req);
        final String fieldName = URLDecoder.decode(extractFieldNameFromRequest(req), CharEncoding.UTF_8);

        LOG.info(format("User [%s] to add the following index [%s] in the collection [%s]", userId, fieldName, colId));

        final IndexMessage indexMessage = IndexMessage.create()
                .withUserId(userId)
                .withColId(colId)
                .withFieldName(fieldName)
                .build();

        jmsContext.createProducer().send(indexCreateDestination, indexMessage);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final String colId = extractCollectionIdFromRequest(req);
        final String fieldName = URLDecoder.decode(extractFieldNameFromRequest(req), CharEncoding.UTF_8);

        LOG.info(format("User [%s] to delete the following index [%s] in the collection [%s]", userId, fieldName, colId));

        final IndexMessage indexMessage = IndexMessage.create()
                .withUserId(userId)
                .withColId(colId)
                .withFieldName(fieldName)
                .build();

        jmsContext.createProducer().send(indexDropDestination, indexMessage);
    }
}
