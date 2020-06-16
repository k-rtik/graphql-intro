# GraphQL intro
## Data
Map of artists and some of their albums deserialized from `artists.json`.

May separate it into `artists.json` and `albums.json` to show how GraphQL
helps to avoid under-fetching.

Map can be added to, but doesn't affect `artists.json`. When the service
is `reset()`: it sets the map to only data read from `artists.json`.

## Guide sections
1. Explain data, entity classes, schema
2. Basic `@Query getArtist()` that returns one object
    - Also show how GraphQL helps to avoid over-fetching
3. `@Query getArtists()` which returns multiple objects
    - Query with multiple `getArtist()` vs `getArtists()`
    - Partial results
4. Add a property to the data model using `@Source` as a parameter
    - Adding `@Query` makes it a query as well
5. `@Mutation` with `Artist` as parameter to add a new artist to the map
    - Show the effect of `@NotNull`
6. `@Mutation` without parameters for `reset`
7. `GraphQLClient` for integration testing the service
    - `@Query` with the same value and different methods with different return
    types for choosing different properties in response object

## Other topics
1. Annotations like `@Name` and `@JsonbProperty` in entity class
2. `@Description` to display on schema and `graphiql`
3. Security, logging, metrics, etc.