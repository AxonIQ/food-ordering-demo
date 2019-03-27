# The 'command' package
This directory will contain the Command Model of our sample application.

The Command Model in this situation reflects the 'C' in CQRS; Command-Query Responsibility Segregation.

To enforce the segregation between this directory and for example the `query` directory,
 we suggest to make all classes package-private.

Check [this](https://axoniq.io/resources/cqrs) page for more specifics on CQRS.