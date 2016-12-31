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

import com.github.achatain.javawebappauthentication.module.AuthenticationModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class CatalogConfig extends GuiceServletContextListener {

    private final Properties properties;
    private final MongoClient mongoClient;

    CatalogConfig() {
        properties = loadProperties();
        mongoClient = new MongoClient(new MongoClientURI(properties.getProperty("mongodb.url")));
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new AuthenticationModule(),
//                new MockedAuthenticationModule(),
                new CatalogDatabaseModule(mongoClient),
                new CatalogBusinessModule(),
                new CatalogFilterModule(properties),
                new CatalogServletModule()
        );
    }

    private static Properties loadProperties() {
        final Properties props = new Properties();
        try (final InputStream is = CatalogServletModule.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(is);
            return props;
        }
        catch (final IOException e) {
            throw new RuntimeException("Unable to load config file from resources", e);
        }
    }
}
