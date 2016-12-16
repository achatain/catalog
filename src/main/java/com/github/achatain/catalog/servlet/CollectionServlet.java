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

import com.github.achatain.catalog.entity.Collection;
import com.github.achatain.catalog.entity.Link;
import com.github.achatain.catalog.service.CollectionService;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Singleton
public class CollectionServlet extends AuthenticatedJsonHttpServlet {

    private final transient CollectionService collectionService;

    @Inject
    public CollectionServlet(final SessionService sessionService, final CollectionService collectionService, @Named("pretty") final Gson gson) {
        super(gson, sessionService);
        this.collectionService = collectionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("List all collections " + req.getRequestURI());
        final List<Collection> collections = collectionService.listCollections(getUserId(req));
        collections.forEach(col -> col.addLink(Link.create().withRel("self").withMethod(Link.Method.GET).withHref("./" + col.getId()).build()));
        sendResponse(resp, collections);
    }
}
