# acme-invoicing-worker

Batch worker that turns fulfilled orders into invoices, priced with the shared
[`com.acme:pricing`](https://github.com/sohan-demo-org/acme-pricing-lib) library.

```bash
# GitHub Packages needs a token even for public packages
export GH_PACKAGES_USER=<github-user> GH_PACKAGES_TOKEN=<token with read:packages>

mvn verify                                  # unit tests
mvn -q compile exec:java                    # invoice the sample batch and print it
```

## Upgrading the pricing library

The version is pinned by `<pricing.version>` in `pom.xml`. New releases of the
library trigger a Devin session that bumps it, runs the test suite, fixes any
breakage and opens a PR here.
