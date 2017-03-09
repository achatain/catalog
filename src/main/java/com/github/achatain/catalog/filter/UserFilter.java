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

package com.github.achatain.catalog.filter;

import com.github.achatain.catalog.service.UserService;
import com.github.achatain.javawebappauthentication.service.SessionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public final class UserFilter implements Filter {

    private final transient SessionService sessionService;
    private final transient UserService userService;

    @Inject
    UserFilter(final SessionService sessionService, final UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        sessionService.getUserFromSession(((HttpServletRequest) request).getSession()).ifPresent(userService::saveIfNotFound);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
