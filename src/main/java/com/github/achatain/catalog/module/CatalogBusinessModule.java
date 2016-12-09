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

package com.github.achatain.catalog.module;

import com.github.achatain.catalog.dao.UserDao;
import com.github.achatain.catalog.dao.impl.UserDaoImpl;
import com.github.achatain.catalog.service.CategoryService;
import com.github.achatain.catalog.service.CronService;
import com.github.achatain.catalog.service.UserService;
import com.github.achatain.catalog.service.impl.CategoryServiceImpl;
import com.github.achatain.catalog.service.impl.CronServiceImpl;
import com.github.achatain.catalog.service.impl.UserServiceImpl;
import com.google.inject.AbstractModule;

class CatalogBusinessModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CategoryService.class).to(CategoryServiceImpl.class);
        bind(CronService.class).to(CronServiceImpl.class);

        bind(UserDao.class).to(UserDaoImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
    }
}
