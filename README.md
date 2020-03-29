## FT word-indexer (AP by CAP)

##

### System consist of 3 modules

* Web gateway: Provides REST API for uploading files and searching words, also some additional endpoints like `/health`, `/nodes`

* File worker: Responsible for uploading, storage, extracting words from files

* Index data nodes: Sharded service for storing and searching words

* [Building and testing](doc/installation.md)
* [Description](doc/description.md)
