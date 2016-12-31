Catalog
============

https://github.com/achatain/catalog

##What is it?

A catalog app to keep track and organise collections of items.

##API

### /collections

**GET** *list all collections*

**POST** *create a collection*

### /collections/{colname}

**PUT** *edit the named collection*

**DELETE** *delete the named collection and its entire content*

### /collections/{colname}/items

**GET** *list all items in the named collection*

**POST** *store the provided item in the named collection*

### /collections/{colname}/items/{itemid}

**GET** *return an item by id from the named collection*

**PUT** *edit an item by id from the named collection*

**DELETE** *delete an item by id from the named collection*
