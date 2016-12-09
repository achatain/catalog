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

package com.github.achatain.catalog.module;

import com.github.achatain.catalog.api.CategoryServlet;
import com.github.achatain.catalog.api.CronServlet;
import com.github.achatain.javawebappauthentication.servlet.GoogleSigninServlet;
import com.github.achatain.javawebappauthentication.servlet.SignOutServlet;
import com.google.inject.servlet.ServletModule;

class CatalogServletModule extends ServletModule {

    public static final String API_ROOT_URI = "/api";
    public static final String V1_URI = "/v1";
    public static final String WILDCHAR = "/*";

    @Override
    protected void configureServlets() {
        serve("/google-auth").with(GoogleSigninServlet.class);
        serve("/signout").with(SignOutServlet.class);
        serve("/cron").with(CronServlet.class);
        serveRegex("\\/api\\/v1\\/\\w+\\/category(\\/.*)*").with(CategoryServlet.class);
    }
}
