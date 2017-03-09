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

package com.github.achatain.catalog.module;

import com.github.achatain.catalog.filter.JsonResponseFilter;
import com.github.achatain.catalog.filter.UserFilter;
import com.github.achatain.javawebappauthentication.filter.SessionFilter;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.github.achatain.catalog.module.CatalogServletModule.API_ROOT_PATH;
import static com.github.achatain.catalog.module.CatalogServletModule.V1;

public class CatalogFilterModule extends ServletModule {

    private final Properties properties;

    CatalogFilterModule(final Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void configureServlets() {
        Map<String, String> initParams = new HashMap<>();
        initParams.put(SessionFilter.LOGIN_URL_REDIRECT, properties.getProperty(SessionFilter.LOGIN_URL_REDIRECT));

        filter("/needs-session/*").through(SessionFilter.class, initParams);
        filter(API_ROOT_PATH + V1 + "/*").through(SessionFilter.class, initParams);
        filter(API_ROOT_PATH + V1 + "/*").through(JsonResponseFilter.class);
        filter(API_ROOT_PATH + V1 + "/*").through(UserFilter.class);
    }

    @Provides
    protected Properties provideProperties() {
        return properties;
    }
}
