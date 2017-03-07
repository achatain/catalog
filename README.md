Catalog
============

https://github.com/achatain/catalog

##What is it?

A catalog app to organise collections of items.

##API

### /collections

**GET** *list all collections*

**POST** *create a collection*

### /collections/{colname}

**GET** *get a collection by name*

**PUT** *edit the given collection*

**DELETE** *delete the given collection and its entire content*

### /collections/{colname}/items

**GET** *list all items in the given collection*

**POST** *store the provided item in the given collection*

### /collections/{colname}/items/{itemid}

**GET** *get an item by id from the given collection* (TODO)

**PUT** *edit an item by id from the given collection* (TODO)

**DELETE** *delete an item by id from the given collection* (TODO)
