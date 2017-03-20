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

import com.github.achatain.catalog.dao.CollectionDao;
import com.github.achatain.catalog.dao.MongoDao;
import com.github.achatain.catalog.entity.Collection;
import com.github.achatain.catalog.exception.IndexDropException;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.String.format;

public class CollectionDaoImpl extends MongoDao implements CollectionDao {

    private static final String COLLECTIONS_COLLECTION_NAME = "collections_";
    private static final String ATTRIBUTES_PREFIX = "attributes.";

    @Inject
    CollectionDaoImpl(final MongoClient mongoClient, final Gson gson) {
        super(mongoClient, gson);
    }

    private MongoCollection<Document> getMetaCollection(final String userId) {
        return getDatabase(userId).getCollection(COLLECTIONS_COLLECTION_NAME);
    }

    private Function<String, Bson> idFilter = id -> eq("id", id);

    @Override
    public List<Collection> listCollections(final String userId) {
        final FindIterable<Document> foundCollections = getMetaCollection(userId).find();
        final List<Collection> collections = new ArrayList<>();
        final Consumer<Document> docToCol = doc -> collections.add(gson.fromJson(doc.toJson(), Collection.class));
        foundCollections.forEach(docToCol);
        return collections;
    }

    @Override
    public List<String> listCollectionIndexes(final String userId, final String collectionId) {
        return getDatabase(userId).getCollection(collectionId).listIndexes()
                .map(document -> document.getString("name"))
                .into(new ArrayList<>());
    }

    @Override
    public Optional<Collection> findById(String userId, String collectionId) {
        final Optional<Document> foundDocument = Optional.ofNullable(getMetaCollection(userId).find(idFilter.apply(collectionId)).first());
        return foundDocument.map(doc -> Optional.of(gson.fromJson(doc.toJson(), Collection.class))).orElseGet(Optional::empty);
    }

    @Override
    public void createCollection(final String userId, final Collection collection) {
        getMetaCollection(userId).insertOne(Document.parse(gson.toJson(collection)));
    }

    @Override
    public void deleteCollection(final String userId, final String collectionId) {
        getMetaCollection(userId).deleteOne(idFilter.apply(collectionId));
        getDatabase(userId).getCollection(collectionId).drop();
    }

    @Override
    public void updateCollection(final String userId, final Collection collection) {
        getMetaCollection(userId).findOneAndReplace(idFilter.apply(collection.getId()), Document.parse(gson.toJson(collection)));
    }

    @Override
    public void createIndex(final String userId, final String collectionId, final String fieldName) {
        getDatabase(userId).getCollection(collectionId).createIndex(
                Indexes.ascending(ATTRIBUTES_PREFIX + fieldName),
                new IndexOptions().name(fieldName).background(true)
        );
    }

    @Override
    public void dropIndex(final String userId, final String collectionId, final String fieldName) throws IndexDropException {
        try {
            getDatabase(userId).getCollection(collectionId).dropIndex(fieldName);
        } catch (final Exception e) {
            throw new IndexDropException(format("Failed to drop index on field [%s] from collection [%s] for user [%s]", fieldName, collectionId, userId), e);
        }
    }
}
