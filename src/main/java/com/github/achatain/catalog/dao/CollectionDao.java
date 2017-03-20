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

package com.github.achatain.catalog.dao;

import com.github.achatain.catalog.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionDao {

    List<Collection> listCollections(String userId);

    List<String> listCollectionIndexes(String userId, String collectionId);

    Optional<Collection> findById(String userId, String collectionId);

    void createCollection(String userId, Collection collection);

    void deleteCollection(String userId, String collectionId);

    void updateCollection(String userId, Collection collection);

    void createIndex(String userId, String collectionId, String fieldName);

    void dropIndex(String userId, String collectionId, String fieldName);
}
