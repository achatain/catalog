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

import com.github.achatain.catalog.dao.CollectionDao;
import com.github.achatain.catalog.dto.CollectionDto;
import com.github.achatain.catalog.entity.Collection;
import com.github.achatain.catalog.entity.Item;
import com.github.achatain.catalog.service.CollectionService;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

public class CollectionServiceImpl implements CollectionService {

    private final CollectionDao collectionDao;

    @Inject
    CollectionServiceImpl(final CollectionDao collectionDao) {
        this.collectionDao = collectionDao;
    }

    @Override
    public List<CollectionDto> listCollections(final String userId) {
        final List<Collection> collections = collectionDao.listCollections(userId);

        final Function<Collection, CollectionDto> colToColDto = col -> CollectionDto.create()
                .withId(col.getId())
                .withName(col.getName())
                .withFields(col.getFields())
                .build();

        return collections.stream().map(colToColDto).collect(Collectors.toList());
    }

    @Override
    public void createCollection(final String userId, final CollectionDto collectionDto) {
        final Collection collection = Collection.create()
                .withId(lowerCase(removePattern(stripAccents(collectionDto.getName()), "\\W")))
                .withName(collectionDto.getName())
                .withFields(collectionDto.getFields())
                .build();

        this.collectionDao.createCollection(userId, collection);
    }

    @Override
    public void deleteCollection(final String userId, final String collectionId) {
        collectionDao.deleteCollection(userId, collectionId);
    }

    @Override
    public List<Item> listItems(final String userId, final String collectionName) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void saveItem(final String userId, final Item item) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Item getItem(final String userId, final String id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void editItem(final String userId, final String id, final Item item) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteItem(final String userId, final String id) {
        throw new RuntimeException("Not implemented");
    }
}
