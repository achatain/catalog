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

import com.github.achatain.catalog.dto.ItemDto;
import com.github.achatain.catalog.entity.Link;
import com.github.achatain.catalog.service.ItemService;
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
import static org.apache.commons.lang3.StringUtils.appendIfMissing;

@Singleton
public class ItemServlet extends AuthenticatedJsonHttpServlet {

    public static final String REGEX_PATH = "\\%s\\%s\\/collections\\/\\w+\\/items(\\/)?";

    private static final transient Logger LOG = Logger.getLogger(ItemServlet.class.getName());

    private final transient ItemService itemService;

    @Inject
    ItemServlet(final SessionService sessionService, final ItemService itemService, @Named("pretty") final Gson gson) {
        super(gson, sessionService);
        this.itemService = itemService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final String colId = extractCollectionIdFromRequest(req);

        LOG.info(format("List all items in the collection [%s]", colId));

        final List<ItemDto> items = itemService.listItems(userId, colId);

        items.forEach(item -> {
            final String href = format("%s%s", appendIfMissing(req.getRequestURL().toString(), "/"), item.getId());
            item.addLink(Link.create().withRel("self").withMethod(Link.Method.GET).withHref(href).build());
            item.addLink(Link.create().withRel("edit").withMethod(Link.Method.PUT).withHref(href).build());
            item.addLink(Link.create().withRel("delete").withMethod(Link.Method.DELETE).withHref(href).build());
        });

        sendResponse(resp, items);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final String colId = extractCollectionIdFromRequest(req);

        final ItemDto itemDto = gson.fromJson(req.getReader(), ItemDto.class);
        Preconditions.checkArgument(itemDto != null, "Request body is missing");

        LOG.info(format("User [%s] to store the following item [%s] in the collection [%s]", userId, gson.toJson(itemDto), colId));

        itemService.createItem(userId, colId, itemDto);
    }
}
