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

import com.github.achatain.javawebappauthentication.exception.AuthenticationException;
import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.api.client.util.Preconditions;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public abstract class AuthenticatedJsonHttpServlet extends JsonHttpServlet {

    private static final String COL_ID_REGEX = "(.+)(/collections/)(\\w+)(/.*)?";
    private static final Integer COL_ID_REGEX_GROUP = 3;

    private static final String ITEM_ID_REGEX = "(.+)(/collections/)(\\w+)(/items/)(\\w+)(/.*)?";
    private static final Integer ITEM_ID_REGEX_GROUP = 5;

    private static final String FIELD_NAME_REGEX = "(.+)(/collections/)(\\w+)(/indexes/)(.+)";
    private static final Integer FIELD_NAME_REGEX_GROUP = 5;

    private final Pattern colIdPattern;
    private final Pattern itemIdPattern;
    private final Pattern fieldNamePattern;

    private final transient SessionService sessionService;

    @Inject
    AuthenticatedJsonHttpServlet(final Gson gson, final SessionService sessionService) {
        super(gson);
        this.sessionService = sessionService;
        this.colIdPattern = Pattern.compile(COL_ID_REGEX);
        this.itemIdPattern = Pattern.compile(ITEM_ID_REGEX);
        this.fieldNamePattern = Pattern.compile(FIELD_NAME_REGEX);
    }

    final String getUserId(final HttpServletRequest request) {
        return sessionService.getUserFromSession(request.getSession()).orElseThrow(() -> new AuthenticationException(format("No user is logged in... Request details are [%s]", gson.toJson(request)))).getId();
    }

    final String extractCollectionIdFromRequest(final HttpServletRequest request) {
        final Optional<String> optionalColId = extractFromRequest(request, colIdPattern, COL_ID_REGEX_GROUP);
        Preconditions.checkArgument(optionalColId.isPresent(), "No collection id was provided in the request URI");
        return optionalColId.get();
    }

    final String extractItemIdFromRequest(final HttpServletRequest request) {
        final Optional<String> optionalItemId = extractFromRequest(request, itemIdPattern, ITEM_ID_REGEX_GROUP);
        Preconditions.checkArgument(optionalItemId.isPresent(), "No item id was provided in the request URI");
        return optionalItemId.get();
    }

    final String extractFieldNameFromRequest(final HttpServletRequest request) {
        final Optional<String> optionalFieldName = extractFromRequest(request, fieldNamePattern, FIELD_NAME_REGEX_GROUP);
        Preconditions.checkArgument(optionalFieldName.isPresent(), "No field name was provided in the request URI");
        return optionalFieldName.get();
    }

    private Optional<String> extractFromRequest(final HttpServletRequest request, final Pattern pattern, final int group) {
        final Matcher matcher = pattern.matcher(request.getRequestURI());

        final Optional<String> result;

        if (matcher.matches() && matcher.groupCount() >= group) {
            result = Optional.ofNullable(matcher.group(group));
        } else {
            result = Optional.empty();
        }

        return result;
    }
}
