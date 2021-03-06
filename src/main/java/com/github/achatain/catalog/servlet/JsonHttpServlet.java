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

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class JsonHttpServlet extends HttpServlet {

    protected final Gson gson;

    @Inject
    JsonHttpServlet(final Gson gson) {
        this.gson = gson;
    }

    final void sendResponse(final HttpServletResponse response, final Object body) throws IOException {
        response.getWriter().write(gson.toJson(body));
        response.getWriter().flush();
    }

    final void sendCustomError(final HttpServletResponse response, final int code, final String msg) throws IOException {
        response.sendError(code, msg);
    }

    final void sendNotFoundError(final HttpServletResponse response, final String msg) throws IOException {
        sendCustomError(response, HttpStatusCodes.STATUS_CODE_NOT_FOUND, msg);
    }
}
