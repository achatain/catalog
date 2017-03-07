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
import java.util.stream.Collectors;

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
                        .withAttributes(item.getAttributes())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void createItem(final String userId, final String collectionId, final ItemDto itemDto) {
        final Item item = Item.create()
                .withAttributes(itemDto.getAttributes())
                .build();

        this.itemDao.createItem(userId, collectionId, item);
    }

    @Override
    public ItemDto readItem(final String userId, final String collectionId, final String itemId) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void updateItem(final String userId, final String collectionId, final ItemDto item) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteItem(final String userId, final String collectionId, final String itemId) {
        throw new RuntimeException("Not implemented");
    }

}
