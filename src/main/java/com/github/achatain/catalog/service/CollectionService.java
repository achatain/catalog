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

package com.github.achatain.catalog.service;

import com.github.achatain.catalog.dto.CollectionDto;
import com.github.achatain.catalog.entity.Item;

import java.util.List;
import java.util.Optional;

public interface CollectionService {

    List<CollectionDto> listCollections(String userId);

    void createCollection(String userId, CollectionDto collectionDto);

    Optional<CollectionDto> readCollection(String userId, String collectionId);

    void updateCollection(String userId, String collectionId, CollectionDto collectionDto);

    void deleteCollection(String userId, String collectionId);

    List<Item> listItems(String userId, String collectionName);

    void createItem(String userId, Item item);

    Item readItem(String userId, String id);

    void updateItem(String userId, String id, Item item);

    void deleteItem(String userId, String id);
}
