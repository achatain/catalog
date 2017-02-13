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

package com.github.achatain.catalog.entity;

import com.google.common.base.Objects;

public final class Link {

    private String rel;
    private String href;
    private Method method;

    private Link() {
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Link link = (Link) o;
        return Objects.equal(rel, link.rel) && Objects.equal(href, link.href) && method == link.method;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rel, href, method);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String rel;
        private String href;
        private Method method;

        private Builder() {
        }

        public Builder withRel(final String rel) {
            this.rel = rel;
            return this;
        }

        public Builder withHref(final String href) {
            this.href = href;
            return this;
        }

        public Builder withMethod(final Method method) {
            this.method = method;
            return this;
        }

        public Link build() {
            final Link link = new Link();
            link.rel = rel;
            link.href = href;
            link.method = method;
            return link;
        }
    }

    public static enum Method {
        GET, POST, PUT, DELETE
    }
}
