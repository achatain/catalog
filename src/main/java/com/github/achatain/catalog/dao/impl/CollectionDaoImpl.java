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

package com.github.achatain.catalog.dao.impl;

import com.github.achatain.catalog.dao.CollectionDao;
import com.github.achatain.catalog.dao.MongoDao;
import com.github.achatain.catalog.entity.Collection;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CollectionDaoImpl extends MongoDao implements CollectionDao {

    private static final String COLLECTIONS_COLLECTION_NAME = "collections_";

    @Inject
    CollectionDaoImpl(final MongoClient mongoClient, final Gson gson) {
        super(mongoClient, gson);
    }

    @Override
    public List<Collection> listCollections(final String userId) {
        final FindIterable<Document> foundCollections = getDatabase(userId).getCollection(COLLECTIONS_COLLECTION_NAME).find();
        final List<Collection> collections = new ArrayList<>();
        final Consumer<Document> docToCol = doc -> collections.add(gson.fromJson(doc.toJson(), Collection.class));
        foundCollections.forEach(docToCol);
        return collections;
    }
}
