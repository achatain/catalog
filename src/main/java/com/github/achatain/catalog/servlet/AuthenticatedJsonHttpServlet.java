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

import com.github.achatain.javawebappauthentication.service.SessionService;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AuthenticatedJsonHttpServlet extends JsonHttpServlet {

    private static final String COL_NAME_REGEX = "(.+)(/collections/)(\\w+)(/.*)?";
    private static final Integer COL_NAME_REGEX_GROUP = 3;

    private static final String ITEM_NAME_REGEX = "(.+)(/collections/)(\\w+)(/items/)(\\w+)(/.*)?";
    private static final Integer ITEM_NAME_REGEX_GROUP = 5;

    private final Pattern colNamePattern;
    private final Pattern itemNamePattern;
    private final SessionService sessionService;

    @Inject
    AuthenticatedJsonHttpServlet(final Gson gson, final SessionService sessionService) {
        super(gson);
        this.sessionService = sessionService;
        this.colNamePattern = Pattern.compile(COL_NAME_REGEX);
        this.itemNamePattern = Pattern.compile(ITEM_NAME_REGEX);
    }

    final String getUserId(final HttpServletRequest request) {
        return sessionService.getUserFromSession(request.getSession()).getId();
    }

    final Optional<String> extractCollectionNameFromRequest(final HttpServletRequest request) {
        return extractFromRequest(request, colNamePattern, COL_NAME_REGEX_GROUP);
    }

    final Optional<String> extractItemNameFromRequest(final HttpServletRequest request) {
        return extractFromRequest(request, itemNamePattern, ITEM_NAME_REGEX_GROUP);
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
