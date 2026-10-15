# acme-invoicing-worker

Plain-Java batch worker that turns fulfilled orders into invoices. It runs on a schedule (here: a `main`
that processes a sample batch), prices every order with the shared
[`com.acme:pricing`](https://github.com/sohan-demo-org/acme-pricing-lib) library, and renders the
invoices as text. Because it shares the library with the
[checkout service](https://github.com/sohan-demo-org/acme-checkout-service), the amount a customer saw
at checkout is the amount on the invoice.

```
fulfilled Orders ──►  InvoicingWorker  ──►  InvoiceGenerator  ──uses──►  com.acme:pricing
                                            (Order → Invoice)            PricingEngine / DiscountCode / TaxRules
                                                 │
                                                 ▼
                                            InvoiceRenderer (text)
```

## Code map

| Class | Responsibility |
|---|---|
| `Order` | input: order id, customer, tax region, promo code, lines |
| `InvoiceGenerator` | maps order lines to `LineItem`s, resolves the promo, calls `PricingEngine.calculateTotal`, numbers the invoice |
| `Invoice` | output: lines with line totals, subtotal, total |
| `InvoiceRenderer` | renders an `Invoice` as a printable text block |
| `InvoicingWorker` | entry point; `sampleBatch()` holds the demo orders |

Tests: JUnit 5 on `InvoiceGenerator` (numbers, promo handling, regions) and `InvoiceRenderer`.

## Running locally

```bash
# GitHub Packages requires a token even for public packages (classic PAT with read:packages).
export GH_PACKAGES_USER=<github-user> GH_PACKAGES_TOKEN=<token>

mvn verify                     # tests
mvn -q compile exec:java       # invoice the sample batch and print the invoices
```

`.mvn/maven.config` points Maven at the project-local `.mvn/settings.xml`, which supplies those
credentials for the `github` repository declared in `pom.xml`. CI (`.github/workflows/ci.yml`) does the
same with the org secret `GH_PACKAGES_TOKEN`.

## Dependency on the pricing library

The version is pinned once, in `pom.xml`:

```xml
<pricing.version>1.0.0</pricing.version>
```

When `acme-pricing-lib` publishes a new release, its release workflow notifies a Devin Automation,
which starts a session for this repo that bumps `pricing.version`, runs `mvn verify`, adapts the code
and tests to any API change, runs the worker against the sample batch, and opens a PR here for review.
Nothing is merged automatically.
