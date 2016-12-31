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
import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;

@Singleton
public class CollectionServlet extends AuthenticatedJsonHttpServlet {

    private static final transient Logger LOG = Logger.getLogger(CollectionServlet.class.getName());

    private final transient CollectionService collectionService;

    @Inject
    public CollectionServlet(final SessionService sessionService, final CollectionService collectionService, @Named("pretty") final Gson gson) {
        super(gson, sessionService);
        this.collectionService = collectionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        LOG.info(format("List all collections for user [%s]", userId) + req.getRequestURI());
        final List<CollectionDto> collections = collectionService.listCollections(userId);
        collections.forEach(col -> {
            final String href = format("%s/%s", req.getRequestURL(), col.getId());
            col.addLink(Link.create().withRel("self").withMethod(Link.Method.GET).withHref(href).build());
            col.addLink(Link.create().withRel("edit").withMethod(Link.Method.PUT).withHref(href).build());
            col.addLink(Link.create().withRel("delete").withMethod(Link.Method.DELETE).withHref(href).build());
        });
        sendResponse(resp, collections);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final CollectionDto collectionDto = gson.fromJson(req.getReader(), CollectionDto.class);
        Preconditions.checkArgument(collectionDto != null, "Request body is missing");
        LOG.info(format("User [%s] to create the collection [%s]", userId, gson.toJson(collectionDto)));
        collectionService.createCollection(userId, collectionDto);
    }
}
