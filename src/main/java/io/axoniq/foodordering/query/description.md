# The 'query' package
This directory will contain the Query Model of our sample application.

The Query Model in this situation reflects the 'Q' in CQRS; Command-Query Responsibility Segregation.

To enforce the segregation between this directory and for example the `command` directory,
 we suggest to make all classes package-private.

Check [this](https://axoniq.io/resources/cqrs) page for more specifics on CQRS.