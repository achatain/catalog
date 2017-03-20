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

package com.github.achatain.catalog.service.impl;

import com.github.achatain.catalog.dao.CollectionDao;
import com.github.achatain.catalog.dto.CollectionDto;
import com.github.achatain.catalog.dto.FieldDto;
import com.github.achatain.catalog.entity.Collection;
import com.github.achatain.catalog.entity.Field;
import com.github.achatain.catalog.service.CollectionService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.*;

public class CollectionServiceImpl implements CollectionService {

    private final CollectionDao collectionDao;

    @Inject
    CollectionServiceImpl(final CollectionDao collectionDao) {
        this.collectionDao = collectionDao;
    }

    @Override
    public List<CollectionDto> listCollections(final String userId) {
        final List<CollectionDto> collectionDtos = collectionDao.listCollections(userId).stream().map(colToColDto).collect(Collectors.toList());

        // TODO maybe just return a list of collection names instead of the full dto?
        collectionDtos
                .forEach(colDto -> {
                    final List<String> indexes = collectionDao.listCollectionIndexes(userId, colDto.getId());
                    colDto.getFields().forEach(fieldDto -> fieldDto.setIndexed(indexes.contains(fieldDto.getName())));
                });

        return collectionDtos;
    }

    @Override
    public Optional<CollectionDto> readCollection(final String userId, final String collectionId) {
        // TODO populate fieldDtos indexed property based on collection indexes (see CollectionServiceImpl.listCollections)
        return collectionDao.findById(userId, collectionId).map(colToColDto);
    }

    @Override
    public void createCollection(final String userId, final CollectionDto collectionDto) {
        final String baseCollectionId = lowerCase(removePattern(stripAccents(collectionDto.getName()), "\\W"));

        final Collection collection = Collection.create()
                .withId(findUniqueCollectionId(userId, baseCollectionId))
                .withName(collectionDto.getName())
                .withFields(collectionDto.getFields().stream().map(fieldDtoToField).collect(Collectors.toList()))
                .build();

        this.collectionDao.createCollection(userId, collection);
    }

    @Override
    public void updateCollection(final String userId, final String collectionId, final CollectionDto collectionDto) {
        collectionDao.findById(userId, collectionId).orElseThrow(() -> new RuntimeException(format("No collection found with name [%s]", collectionId)));

        final Collection collection = Collection.create()
                .withId(collectionId)
                .withName(collectionDto.getName())
                .withFields(collectionDto.getFields().stream().map(fieldDtoToField).collect(Collectors.toList()))
                .build();

        this.collectionDao.updateCollection(userId, collection);
    }

    @Override
    public void deleteCollection(final String userId, final String collectionId) {
        collectionDao.deleteCollection(userId, collectionId);
    }

    @Override
    public void createIndex(final String userId, final String collectionId, final String fieldName) {
        collectionDao.createIndex(userId, collectionId, fieldName);
    }

    @Override
    public void dropIndex(final String userId, final String collectionId, final String fieldName) {
        collectionDao.dropIndex(userId, collectionId, fieldName);
    }

    private String findUniqueCollectionId(final String userId, final String baseCollectionId) {
        final Optional<Collection> optionalCollection = collectionDao.findById(userId, baseCollectionId);

        if (optionalCollection.isPresent()) {
            int suffix = 1;
            while (collectionDao.findById(userId, format("%s%s", baseCollectionId, suffix)).isPresent()) {
                suffix++;
            }
            return format("%s%s", baseCollectionId, suffix);
        } else {
            return baseCollectionId;
        }
    }

    private final Function<FieldDto, Field> fieldDtoToField = fieldDto -> Field.create()
            .withName(fieldDto.getName())
            .withIndexed(fieldDto.isIndexed())
            .build();

    private final Function<Field, FieldDto> fieldToFieldDto = field -> FieldDto.create()
            .withName(field.getName())
            .withIndexed(field.getIndexed())
            .build();

    private final Function<Collection, CollectionDto> colToColDto = col -> CollectionDto.create()
            .withId(col.getId())
            .withName(col.getName())
            .withFields(col.getFields().stream().map(fieldToFieldDto).collect(Collectors.toList()))
            .build();
}
