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

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class CatalogDatabaseModule extends AbstractModule {

    public static final String SYSTEM_DATABASE = "system";

    private final MongoClient mongoClient;
    private final Provider<MongoDatabase> systemDatabaseProvider;

    CatalogDatabaseModule(final MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.systemDatabaseProvider = () -> mongoClient.getDatabase(SYSTEM_DATABASE);
    }

    @Override
    protected void configure() {
        bind(MongoClient.class).toInstance(mongoClient);
        bind(MongoDatabase.class).annotatedWith(Names.named(SYSTEM_DATABASE)).toProvider(systemDatabaseProvider);
    }
}
