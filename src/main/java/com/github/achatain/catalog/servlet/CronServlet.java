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

import com.github.achatain.catalog.service.CronService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Singleton
public class CronServlet extends HttpServlet {

    private final transient CronService cronService;

    @Inject
    CronServlet(final CronService cronService) {
        this.cronService = cronService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Collection<String>> headers = new HashMap<>();

        String header;
        final Enumeration<String> headerEnum = req.getHeaderNames();

        while (headerEnum.hasMoreElements()) {
            header = headerEnum.nextElement();
            headers.put(header, Collections.list(req.getHeaders(header)));
        }

        cronService.acknowledgeCronRequest(headers);
    }
}
