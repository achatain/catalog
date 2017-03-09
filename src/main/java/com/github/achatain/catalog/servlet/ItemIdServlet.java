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
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.String.format;

@Singleton
public class ItemIdServlet extends AuthenticatedJsonHttpServlet {

    public static final String REGEX_PATH = "\\%s\\%s\\/collections\\/\\w+\\/items\\/\\w+(\\/)?";

    private static final Logger LOG = Logger.getLogger(ItemIdServlet.class.getName());

    private final transient ItemService itemService;

    @Inject
    ItemIdServlet(final SessionService sessionService, final ItemService itemService, @Named("pretty") final Gson gson) {
        super(gson, sessionService);
        this.itemService = itemService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final String collectionId = extractCollectionIdFromRequest(req);
        final String itemId = extractItemIdFromRequest(req);

        final Optional<ItemDto> optionalItem = itemService.readItem(userId, collectionId, itemId);

        if (optionalItem.isPresent()) {
            sendResponse(resp, optionalItem.get());
        } else {
            sendNotFoundError(resp, format("No item was found with id [%s] in collection [%s]", itemId, collectionId));
        }
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final String collectionId = extractCollectionIdFromRequest(req);
        final String itemId = extractItemIdFromRequest(req);

        final ItemDto itemDto = gson.fromJson(req.getReader(), ItemDto.class);
        Preconditions.checkArgument(itemDto != null, "Request body is missing");

        LOG.info(format("User [%s] to edit item [%s] in collection [%s] with [%s]", userId, itemId, collectionId, gson.toJson(itemDto)));

        itemService.updateItem(userId, collectionId, itemId, itemDto);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String userId = getUserId(req);
        final String collectionId = extractCollectionIdFromRequest(req);
        final String itemId = extractItemIdFromRequest(req);

        LOG.info(format("User [%s] to delete item [%s] in collection [%s]", userId, itemId, collectionId));

        itemService.deleteItem(userId, collectionId, itemId);
    }
}
