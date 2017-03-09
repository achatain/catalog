Catalog
============

https://github.com/achatain/catalog

##What is it?

A catalog app to organise collections of items.

##API

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

### /collections/{collection_id}/items/{item_id}

**GET** *get an item by id from the given collection*

**PUT** *edit an item by id from the given collection*

**DELETE** *delete an item by id from the given collection*
