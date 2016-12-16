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

package com.github.achatain.catalog.entity;

import com.google.common.collect.Sets;

import java.util.Set;

public final class HateoasResponse {

    private Object object;
    private Set<Link> links;

    private HateoasResponse() {
        links = Sets.newHashSet();
    }

    Object getObject() {
        return object;
    }

    Set<Link> getLinks() {
        return links;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private Object object;
        private Set<Link> links;

        private Builder() {
            links = Sets.newHashSet();
        }

        public Builder withObject(final Object object) {
            this.object = object;
            return this;
        }

        public Builder withLink(final Link link) {
            this.links.add(link);
            return this;
        }

        public Builder withLinks(final Set<Link> links) {
            this.links.addAll(links);
            return this;
        }

        public HateoasResponse build() {
            final HateoasResponse hateoasResponse = new HateoasResponse();
            hateoasResponse.object = object;
            hateoasResponse.links = links;
            return hateoasResponse;
        }
    }

}
