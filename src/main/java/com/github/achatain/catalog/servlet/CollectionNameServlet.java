/*
 * https://github.com/achatain/catalog
 *
 * Copyright (C) 2016 Antoine Chatain (achatain [at] outlook [dot] com)
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

import com.github.achatain.catalog.service.CollectionService;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.api.client.util.Preconditions;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;

@Singleton
public class CollectionNameServlet extends AuthenticatedJsonHttpServlet {

    public static final String REGEX_PATH = "\\%s\\%s\\/collections\\/\\w+(\\/)?";

    private static final transient Logger LOG = Logger.getLogger(CollectionNameServlet.class.getName());

    private final transient CollectionService collectionService;

    @Inject
    CollectionNameServlet(final SessionService sessionService, final CollectionService collectionService, @Named("pretty") final Gson gson) {
        super(gson, sessionService);
        this.collectionService = collectionService;
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Edit a collection named " + extractCollectionNameFromRequest(req));
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final Optional<String> optionalColName = extractCollectionNameFromRequest(req);

        Preconditions.checkArgument(optionalColName.isPresent(), "No collection name was provided");

        final String colName = optionalColName.get();
        this.collectionService.deleteCollection(userId, colName);

        LOG.info(format("Deleted the collection [%s] for user [%s]", colName, userId));
    }
}
