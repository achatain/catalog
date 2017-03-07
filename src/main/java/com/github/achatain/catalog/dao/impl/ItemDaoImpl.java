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
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemDaoImpl extends MongoDao implements ItemDao {

    @Inject
    ItemDaoImpl(final MongoClient mongoClient, final Gson gson) {
        super(mongoClient, gson);
    }

    @Override
    public List<Item> listItems(final String userId, final String collectionName) {
        final FindIterable<Document> foundItems = getDatabase(userId).getCollection(collectionName).find();
        final List<Item> items = new ArrayList<>();
        final Consumer<Document> docToItem = doc -> items.add(gson.fromJson(doc.toJson(), Item.class));
        foundItems.forEach(docToItem);
        return items;
    }

    @Override
    public void createItem(final String userId, final String collectionId, final Item item) {
        getDatabase(userId).getCollection(collectionId).insertOne(Document.parse(gson.toJson(item)));
    }

    @Override
    public Item readItem(final String userId, final String id) {
        throw new NotImplementedException("ItemDaoImpl.readItem");
    }

    @Override
    public void updateItem(final String userId, final String id, final Item item) {
        throw new NotImplementedException("ItemDaoImpl.updateItem");
    }

    @Override
    public void deleteItem(final String userId, final String id) {
        throw new NotImplementedException("ItemDaoImpl.deleteItem");
    }
}
