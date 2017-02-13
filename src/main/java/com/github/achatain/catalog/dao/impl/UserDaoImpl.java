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

package com.github.achatain.catalog.dao.impl;

import com.github.achatain.catalog.dao.UserDao;
import com.github.achatain.javawebappauthentication.entity.AuthenticatedUser;
import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

import static com.github.achatain.catalog.module.CatalogDatabaseModule.SYSTEM_DATABASE;
import static com.mongodb.client.model.Filters.eq;

public class UserDaoImpl implements UserDao {

    private static final String USER_COLLECTION = "users";

    private final MongoDatabase mongoDatabase;
    private final Gson gson;

    @Inject
    private UserDaoImpl(@Named(SYSTEM_DATABASE) final MongoDatabase mongoDatabase, final Gson gson) {
        this.mongoDatabase = mongoDatabase;
        this.gson = gson;
    }

    @Override
    public Optional<AuthenticatedUser> findUser(final String id) {
        final Optional<Document> foundUserDoc = Optional.ofNullable(mongoDatabase.getCollection(USER_COLLECTION).find(eq("id", id)).first());
        return foundUserDoc.map(doc -> gson.fromJson(doc.toJson(), AuthenticatedUser.class));
    }

    @Override
    public void save(final AuthenticatedUser user) {
        mongoDatabase.getCollection(USER_COLLECTION).insertOne(Document.parse(gson.toJson(user)));
    }
}
