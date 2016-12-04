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

import com.github.achatain.javawebappauthentication.filter.SessionFilter;
import com.github.achatain.javawebappauthentication.servlet.GoogleSigninServlet;
import com.github.achatain.javawebappauthentication.servlet.SignOutServlet;
import com.google.inject.servlet.ServletModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class CatalogServletModule extends ServletModule {

    private final Properties props;

    CatalogServletModule() {
        props = new Properties();
        try (final InputStream is = CatalogServletModule.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(is);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load config file from resources", e);
        }
    }

    @Override
    protected void configureServlets() {
        Map<String, String> initParams = new HashMap<>();
        initParams.put(SessionFilter.LOGIN_URL_REDIRECT, props.getProperty(SessionFilter.LOGIN_URL_REDIRECT));
        filter("/needs-session/*").through(SessionFilter.class, initParams);

        serve("/google-auth").with(GoogleSigninServlet.class);
        serve("/signout").with(SignOutServlet.class);
    }
}
