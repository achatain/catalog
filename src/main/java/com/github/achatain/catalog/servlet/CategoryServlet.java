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

import com.github.achatain.catalog.dao.UserDao;
import com.github.achatain.javawebappauthentication.service.SessionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class CategoryServlet extends HttpServlet {

    public static final String URI = "/category";

    private final transient SessionService sessionService;
    private final transient UserDao userDao;

    @Inject
    private CategoryServlet(final SessionService sessionService, final UserDao userDao) {
        this.sessionService = sessionService;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("PathInfo [" + req.getPathInfo() + "]");
        final Matcher matcher = Pattern.compile("(/catalog/api/v1/)(\\w+)(/.*)").matcher(req.getRequestURI());
        if (matcher.matches()) {
            System.out.println("Game selector [" + matcher.group(2) + "]");
        }
    }
}
