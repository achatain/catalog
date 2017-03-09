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

import com.github.achatain.catalog.dao.ItemDao;
import com.github.achatain.catalog.dao.MongoDao;
import com.github.achatain.catalog.entity.Item;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mongodb.client.model.Filters.eq;

public class ItemDaoImpl extends MongoDao implements ItemDao {

    @Inject
    ItemDaoImpl(final MongoClient mongoClient, final Gson gson) {
        super(mongoClient, gson);
    }

    private Function<String, Bson> idFilter = id -> eq("_id", new ObjectId(id));

    @Override
    public List<Item> listItems(final String userId, final String collectionId) {
        final FindIterable<Document> foundItems = getDatabase(userId).getCollection(collectionId).find();
        final List<Item> items = new ArrayList<>();
        final Consumer<Document> docConsumer = doc -> items.add(docToItem.apply(doc));
        foundItems.forEach(docConsumer);
        return items;
    }

    @Override
    public void createItem(final String userId, final String collectionId, final Item item) {
        getDatabase(userId).getCollection(collectionId).insertOne(Document.parse(gson.toJson(item)));
    }

    @Override
    public Optional<Item> readItem(final String userId, final String collectionId, final String itemId) {
        final Optional<Document> foundDocument = Optional.ofNullable(getDatabase(userId).getCollection(collectionId).find(idFilter.apply(itemId)).first());
        return foundDocument
                .map(doc -> Optional.of(docToItem.apply(doc)))
                .orElseGet(Optional::empty);
    }

    @Override
    public void updateItem(final String userId, final String collectionId, final String itemId, final Item item) {
        getDatabase(userId).getCollection(collectionId).findOneAndReplace(idFilter.apply(item.getId()), Document.parse(gson.toJson(item)));
    }

    @Override
    public void deleteItem(final String userId, final String collectionId, final String itemId) {
        getDatabase(userId).getCollection(collectionId).deleteOne(idFilter.apply(itemId));
    }

    private final Function<Document, Item> docToItem = doc -> Item.create()
            .withId(doc.getObjectId("_id").toHexString())
            .withAttributes(gson.fromJson(doc.toJson(), Item.class).getAttributes())
            .build();
}
