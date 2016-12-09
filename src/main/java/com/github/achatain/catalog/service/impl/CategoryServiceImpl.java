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

package com.github.achatain.catalog.service.impl;

import com.github.achatain.catalog.common.PaginatedResponse;
import com.github.achatain.catalog.entity.Category;
import com.github.achatain.catalog.entity.Item;
import com.github.achatain.catalog.service.CategoryService;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.util.function.Consumer;

public class CategoryServiceImpl implements CategoryService {

    @Override
    public void save(final Category category) {
        final MongoClient mongoClient = new MongoClient();
        Consumer<Document> consumer = System.out::println;
        mongoClient.listDatabases().forEach(consumer);
        Document first = mongoClient.getDatabase("acnl").getCollection("items").find().projection(Projections.excludeId()).first();
        Item item = new GsonBuilder().create().fromJson(first.toJson(), Item.class);
        System.out.println(item);
    }

    @Override
    public PaginatedResponse<Category> getAll() {
        return null;
    }

    @Override
    public PaginatedResponse<Category> getAll(final String pageToken) {
        return null;
    }
}
