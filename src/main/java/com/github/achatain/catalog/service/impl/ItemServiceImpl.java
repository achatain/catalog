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

import com.github.achatain.catalog.dao.ItemDao;
import com.github.achatain.catalog.dto.ItemDto;
import com.github.achatain.catalog.entity.Item;
import com.github.achatain.catalog.service.ItemService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;

    @Inject
    ItemServiceImpl(final ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public List<ItemDto> listItems(final String userId, final String collectionId) {
        return itemDao
                .listItems(userId, collectionId)
                .stream()
                .map(item -> ItemDto.create()
                        .withId(item.getId())
                        .withAttributes(item.getAttributes())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void createItem(final String userId, final String collectionId, final ItemDto itemDto) {
        final Item item = Item.create()
                .withAttributes(itemDto.getAttributes())
                .build();

        itemDao.createItem(userId, collectionId, item);
    }

    @Override
    public Optional<ItemDto> readItem(final String userId, final String collectionId, final String itemId) {
        return itemDao.readItem(userId, collectionId, itemId).map(item -> ItemDto.create().withId(item.getId()).withAttributes(item.getAttributes()).build());
    }

    @Override
    public void updateItem(final String userId, final String collectionId, final String itemId, final ItemDto itemDto) {
        itemDao.readItem(userId, collectionId, itemId).orElseThrow(() -> new RuntimeException(format("No item found with id [%s] in collection [%s]", itemId, collectionId)));

        final Item item = Item.create()
                .withId(itemDto.getId())
                .withAttributes(itemDto.getAttributes())
                .build();

        itemDao.updateItem(userId, collectionId, itemId, item);
    }

    @Override
    public void deleteItem(final String userId, final String collectionId, final String itemId) {
        itemDao.deleteItem(userId, collectionId, itemId);
    }

}
