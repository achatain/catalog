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

package com.github.achatain.catalog.dto;

import com.google.common.collect.Maps;

import java.util.Map;

public class ItemDto extends HateoasDto {

    private String id;
    private Map<String, Object> attributes;

    private ItemDto() {
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private final Map<String, Object> attributes = Maps.newHashMap();

        public Builder withId(final String id) {
            this.id = id;
            return this;
        }

        public Builder withAttribute(final String key, final Object value) {
            this.attributes.put(key, value);
            return this;
        }

        public Builder withAttributes(final Map<String, Object> attributes) {
            this.attributes.putAll(attributes);
            return this;
        }

        public ItemDto build() {
            final ItemDto itemDto = new ItemDto();
            itemDto.id = id;
            itemDto.attributes = attributes;
            return itemDto;
        }
    }
}
