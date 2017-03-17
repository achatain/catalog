Catalog
============
[![Build Status](https://travis-ci.org/achatain/catalog.svg?branch=master)](https://travis-ci.org/achatain/catalog)

https://github.com/achatain/catalog

## What is it?

A catalog app to organise collections of items.

## Application server configuration

Recommended to use WildFly. See the [wiki](https://github.com/achatain/catalog/wiki) for server configuration details.

## API

### /collections

**GET** *list all collections*

**POST** *create a collection*

### /collections/{collection_id}

**GET** *get a collection by id*

**PUT** *edit the given collection*

**DELETE** *delete the given collection and its entire content*

### /collections/{collection_id}/items

**GET** *list all items in the given collection*

**POST** *store the provided item in the given collection*

### /collections/{collection_id}/indexes

**POST** *create an index on the given field from the given collection* (DEV IN PROGRESS)

**DELETE** *drop the index on the given field from the given collection* (DEV IN PROGRESS)

### /collections/{collection_id}/items/{item_id}

**GET** *get an item by id from the given collection*

**PUT** *edit an item by id from the given collection*

**DELETE** *delete an item by id from the given collection*
