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

import com.github.achatain.catalog.dto.CollectionDto;
import com.github.achatain.catalog.entity.Link;
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
import static org.apache.commons.lang3.StringUtils.removeEnd;

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
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Look up a collection named " + extractCollectionNameFromRequest(req));

        final String userId = getUserId(req);
        final Optional<String> optionalColName = extractCollectionNameFromRequest(req);

        Preconditions.checkArgument(optionalColName.isPresent(), "No collection name was provided");

        final String colName = optionalColName.get();
        final Optional<CollectionDto> optionalCollectionDto = collectionService.readCollection(userId, colName);

        if (optionalCollectionDto.isPresent()) {
            final CollectionDto col = optionalCollectionDto.get();
            final String href = removeEnd(req.getRequestURL().toString(), "/");
            col.addLink(Link.create().withRel("self").withMethod(Link.Method.GET).withHref(href).build());
            col.addLink(Link.create().withRel("edit").withMethod(Link.Method.PUT).withHref(href).build());
            col.addLink(Link.create().withRel("delete").withMethod(Link.Method.DELETE).withHref(href).build());
            sendResponse(resp, col);
        } else {
            sendNotFoundError(resp, format("No collection was found with name [%s]", colName));
        }
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("Edit a collection named " + extractCollectionNameFromRequest(req));

        final String userId = getUserId(req);
        final Optional<String> optionalColName = extractCollectionNameFromRequest(req);

        Preconditions.checkArgument(optionalColName.isPresent(), "No collection name was provided");
        final String colName = optionalColName.get();

        final CollectionDto collectionDto = gson.fromJson(req.getReader(), CollectionDto.class);
        Preconditions.checkArgument(collectionDto != null, "Request body is missing");

        LOG.info(format("User [%s] to edit the collection named [%s] with [%s]", userId, colName, gson.toJson(collectionDto)));

        collectionService.updateCollection(userId, colName, collectionDto);
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
