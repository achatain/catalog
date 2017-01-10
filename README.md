Catalog
============

https://github.com/achatain/catalog

##What is it?

A catalog app to organise collections of items.

##API

### /collections

**GET** *list all collections*

**POST** *create a collection* (TODO: ensure short name unicity at creation)

### /collections/{colname}

**PUT** *edit the named collection* (TODO)

**DELETE** *delete the named collection and its entire content* (TODO)

### /collections/{colname}/items

**GET** *list all items in the named collection* (TODO)

**POST** *store the provided item in the named collection* (TODO)

### /collections/{colname}/items/{itemid}

**GET** *return an item by id from the named collection* (TODO)

**PUT** *edit an item by id from the named collection* (TODO)

**DELETE** *delete an item by id from the named collection* (TODO)
