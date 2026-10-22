/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql;

import java.time.Duration;
import java.util.Optional;

/**
 * {@code Template} is a helper class that increases productivity when performing common NoSQL operations.
 * The Template feature in Jakarta NoSQL simplifies the implementation of common database
 * operations by providing a basic API to the underlying persistence engine.
 * It follows the standard DAO (Data Access Object) pattern, a common design pattern used in software development.
 *
 * <p>
 * The Template pattern involves creating a skeletal structure for an algorithm, with some steps implemented and others left to be implemented by subclasses. Similarly, the
 * Template feature in Jakarta NoSQL makes a skeleton around NoSQL database operations, allowing developers to focus on implementing the specific logic required for their
 * application.
 * </p>
 * <p>
 * Overall, the Template feature in Jakarta NoSQL provides a simple and efficient way to implement common database operations while following established design patterns like
 * the Template Method. By using the Template feature, developers can save time and effort in implementing their NoSQL database operations, allowing them to focus on other
 * aspects of their application.
 * </p>
 *
 * <pre>{@code
 * @Inject
 * Template template;
 *
 * Book book = Book.builder()
 *         .id(id)
 *         .title("Java Concurrency in Practice")
 *         .author("Brian Goetz")
 *         .year(Year.of(2006))
 *         .edition(1)
 *         .build();
 *
 * template.insert(book);
 *
 * Optional<Book> optional = template.find(Book.class, id);
 *
 * System.out.println("The result " + optional);
 *
 * template.delete(Book.class,id);
 * }</pre>
 * <p>
 * Furthermore, in CRUD (Create, Read, Update, Delete) operations, Template provides a fluent API for selecting,
 * deleting, and querying entities, offering the ability to search and remove beyond the ID
 * attribute. Take a look at {@link QueryMapper} for more detail about the provided fluent-API.
 * </p>
 * <pre>{@code
 * @Inject
 * Template template;
 *
 * List<Book> books = template.select(Book.class)
 *         .where("author")
 *         .eq("Joshua Bloch")
 *         .and("edition")
 *         .gt(3)
 *         .result();
 *
 * Stream<Book> books = template.select(Book.class)
 *         .where("author")
 *         .eq("Joshua Bloch")
 *         .stream();
 *
 * Optional<Book> optional = template.select(Book.class)
 *         .where("title")
 *         .eq("Java Concurrency in Practice")
 *         .and("author")
 *         .eq("Brian Goetz")
 *         .and("year")
 *         .eq(Year.of(2006))
 *         .singleResult();
 *
 * template.delete(Book.class)
 *         .where("author")
 *         .eq("Joshua Bloch")
 *         .and("edition")
 *         .gt(3)
 *         .execute();
 *
 * }</pre>
 *
 * @see QueryMapper
 * @see Query
 * @since 1.0.0
 */
public interface Template {

    /**
     * Inserts an entity into the database. If an entity of this type with the same
     * unique identifier already exists in the database, the outcome is provider-specific.
     * This specification does not require duplicate-identifier detection or rejection and does not define
     * a standard exception for such conflicts. Depending on the capabilities and insertion semantics of the
     * underlying database, a provider may reject the insertion, overwrite, append, or perform an upsert.
     * When an insertion is rejected because of an existing identifier, the exception type is provider-specific.
     *
     * <p>The entity instance returned as a result of this method must include all values
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use the instance
     *  supplied as a parameter. This method makes no guarantees about the state of the
     * instance that is supplied as a parameter.</p>
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * Book book = new Book("978-0132350884", "Clean Code", "Robert C. Martin");
     * Book insertedBook = template.insert(book);
     * }</pre>

     * @param entity the entity to insert. Must not be {@code null}.
     * @param <T>    the entity type
     * @return the inserted entity, which may or may not be a different instance depending on whether the insert
     * caused values to be generated or automatically incremented.
     * @throws NullPointerException if the entity is null.
     */
    <T> T insert(T entity);

    /**
     * Inserts an entity into the database with an optional expiration.
     * Duplicate-identifier handling is provider-specific, as described in {@link #insert(Object)}.
     *
     * <p>The entity instance returned as a result of this method must include all values
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use the instance
     *  supplied as a parameter. This method makes no guarantees about the state of the
     * instance that is supplied as a parameter.</p>
     *
     * <p>Time-To-Live (TTL) is an optional capability. In databases that support per-entity expiration, the entity may be
     * removed automatically once the given duration has passed since its insertion. Databases that only offer retention
     * policies configured at the database or collection level do not provide an equivalent per-entity TTL, and other
     * databases offer no expiration at all. If the database or provider does not support TTL, the provider may throw an
     * {@link UnsupportedOperationException}. Providers may also support TTL only with a coarser granularity, in which
     * case the duration may be rounded to a supported unit or the TTL may not be applied. Expiration is therefore
     * not guaranteed by this method; consult the provider documentation.</p>
     * <pre>{@code
     *
     * @Inject
     * Template template;
     *
     * SessionToken token = new SessionToken("abc123", "user-42", Instant.now());
     * Duration ttl = Duration.ofMinutes(30);
     *
     * SessionToken inserted = template.insert(token, ttl);
     * }</pre>
     * @param entity the entity to insert. Must not be {@code null}.
     * @param ttl    time to live
     * @param <T>    the entity type
     * @return the inserted entity, which may or may not be a different instance depending on whether the insert caused
     * values to be generated or automatically incremented.
     * @throws NullPointerException          if the entity is null.
     * @throws UnsupportedOperationException if the provider rejects the requested TTL because it is unsupported
     */
    <T> T insert(T entity, Duration ttl);

    /**
     * Inserts multiple entities into the database.
     * Duplicate-identifier handling is provider-specific, as described in {@link #insert(Object)}.
     *
     * <p>The entities within the returned {@link Iterable} must include all values
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use
     * the entity instances that are supplied in the parameter. This method makes no guarantees
     * about the state of the entity instances that are supplied in the parameter.
     * The position of entities within the {@code Iterable} return value must correspond to the
     * position of entities in the parameter based on the unique identifier of the entity.</p>
     *
     * @param entities entities to insert.
     * @param <T>      the entity type
     * @return an iterable containing the inserted entities, which may or may not be different instances depending
     * on whether the insert caused values to be generated or automatically incremented.
     * @throws NullPointerException if the iterable is null or any element is null.
     */
    <T> Iterable<T> insert(Iterable<T> entities);

    /**
     * Inserts multiple entities into the database with an optional expiration.
     * Duplicate-identifier handling is provider-specific, as described in {@link #insert(Object)}.
     *
     * <p>The entities within the returned {@link Iterable} must include all values
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use
     * the entity instances that are supplied in the parameter. This method makes no guarantees
     * about the state of the entity instances that are supplied in the parameter.
     * The position of entities within the {@code Iterable} return value must correspond to the
     * position of entities in the parameter based on the unique identifier of the entity.</p>
     *
     * <p>Time-To-Live (TTL) is an optional capability. In databases that support per-entity expiration, the entities may be
     * removed automatically once the given duration has passed since their insertion. Databases that only offer retention
     * policies configured at the database or collection level do not provide an equivalent per-entity TTL, and other
     * databases offer no expiration at all. If the database or provider does not support TTL, the provider may throw an
     * {@link UnsupportedOperationException}. Providers may also support TTL only with a coarser granularity, in which
     * case the duration may be rounded to a supported unit or the TTL may not be applied. Expiration is therefore
     * not guaranteed by this method; consult the provider documentation.</p>
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * List<SessionToken> tokens = List.of(
     *     SessionToken.builder()
     *         .id("abc123")
     *         .userId("user-42")
     *         .issuedAt(Instant.now())
     *         .build(),
     *
     *     SessionToken.builder()
     *         .id("def456")
     *         .userId("user-99")
     *         .issuedAt(Instant.now())
     *         .build()
     * );
     *
     * Duration ttl = Duration.ofMinutes(30);
     *
     * Iterable<SessionToken> insertedTokens = template.insert(tokens, ttl);
     * }</pre>
     *
     * @param entities entities to insert.
     * @param <T>      the entity type
     * @param ttl      time to live
     * @return an iterable containing the inserted entities, which may or may not be different instances depending
     * on whether the insert caused values to be generated or automatically incremented.
     * @throws NullPointerException if the iterable is null or any element is null.
     * @throws UnsupportedOperationException if the provider rejects the requested TTL because it is unsupported
     */
    <T> Iterable<T> insert(Iterable<T> entities, Duration ttl);

    /**
     * Modifies an entity that already exists in the database.
     *
     * <p>For an update to be made, a matching entity with the same unique identifier
     * must be present in the database. The intent of this method is to modify existing data, not to upsert:
     * portable applications should not rely on a missing entity being created or on any particular result for it.
     * As a provider-specific limitation, databases that do not distinguish between inserting and updating, such as
     * those that use an append or overwrite write model, may write the entity as the {@link #insert} method would.</p>
     *
     * <p>If the entity is versioned (for example, with an annotation or by
     * another convention from the entity model such as having an attribute named {@code version}),
     * the version is entity metadata. When the provider supports optimistic versioning, for example through
     * provider-supported conditional updates, the version must also match and is automatically incremented when
     * making the update. Not every database offers native conditional writes or atomic operations, so optimistic
     * locking is not a guarantee across all database models.</p>
     *
     * <p>When the database distinguishes updates from inserts, non-matching entities are ignored and do not cause an error to be raised.</p>
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * Book book = Book.builder()
     *     .isbn("978-1234567890")
     *     .title("Domain-Driven Design")
     *     .version(1)
     *     .build();
     *
     * Book updated = template.update(book);
     * }</pre>
     * @param <T>    the entity type
     * @param entity the entity to update. Must not be {@code null}.
     * @return the updated entity, which may or may not be a different instance depending on whether the update caused
     * values to be generated or automatically incremented.
     * @throws NullPointerException if the entity is null.
     */
    <T> T update(T entity);

    /**
     * Modifies entities that already exist in the database.
     *
     * <p>For an update to be made to an entity, a matching entity with the same unique identifier
     * must be present in the database. The intent of this method is to modify existing data, not to upsert:
     * portable applications should not rely on a missing entity being created or on any particular result for it.
     * As a provider-specific limitation, databases that do not distinguish between inserting and updating, such as
     * those that use an append or overwrite write model, may write the entity as the {@link #insert(Iterable)} method would.</p>
     *
     * <p>If the entity is versioned (for example, with an annotation or by
     * another convention from the entity model such as having an attribute named {@code version}),
     * the version is entity metadata. When the provider supports optimistic versioning, for example through
     * provider-supported conditional updates, the version must also match and is automatically incremented when
     * making the update. Not every database offers native conditional writes or atomic operations, so optimistic
     * locking is not a guarantee across all database models.</p>
     *
     * <p>When the database distinguishes updates from inserts, non-matching entities are ignored and do not cause an error to be raised.</p>
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * List<Book> booksToUpdate = List.of(
     *     Book.builder().isbn("978-1111111111").title("Effective Java").version(1).build(),
     *     Book.builder().isbn("978-2222222222").title("Clean Code").version(2).build()
     * );
     *
     * Iterable<Book> updatedBooks = template.update(booksToUpdate);
     * }</pre>
     * @param entities entities to update.
     * @param <T>      the entity class type
     * @return an iterable containing the updated entities, which may or may not be different instances depending
     * on whether the update caused values to be generated or automatically incremented.
     * @throws NullPointerException if either the iterable is null or any element is null.
     */
    <T> Iterable<T> update(Iterable<T> entities);

    /**
     * Deletes a given entity. Deletion is performed by matching the Id, and if
     * the entity is versioned (for example, with a provider-supported version annotation or by
     * a convention of the entity model) and the provider supports optimistic versioning, then also the
     * version. Other attributes of the entity do not need to match.
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * SessionToken token = SessionToken.builder()
     *     .token("abc123")
     *     .userId("user-42")
     *     .version(1)
     *     .build();
     *
     * template.delete(token);
     * }</pre>
     * @param entity must not be {@code null}.
     * @param <T>    the entity type
     * @throws NullPointerException              when the entity is null
     */
    <T> void delete(T entity);

    /**
     * Deletes the given entities. Deletion of each entity is performed by
     * matching the unique identifier, and if the entity is versioned (for
     * example, with a provider-supported version annotation or by a convention of the entity model)
     * and the provider supports optimistic versioning, then also the version.
     * Other attributes of the entity do not need to match.
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * List<SessionToken> tokens = List.of(
     *     SessionToken.builder()
     *         .token("abc123")
     *         .userId("user-42")
     *         .version(1)
     *         .build(),
     *     SessionToken.builder()
     *         .token("def456")
     *         .userId("user-99")
     *         .version(2)
     *         .build()
     * );
     *
     * template.delete(tokens);
     * }</pre>
     * @param <T>    the entity type
     * @param entities Must not be {@code null}. Must not contain {@code null}
     *                 elements.
     * @throws NullPointerException              If the iterable is {@code null}
     *                                           or contains {@code null}
     *                                           elements.
     */
    <T> void delete(Iterable<? extends T> entities);

    /**
     * Retrieves an entity by its Id.
     * <p>Example usage:
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * Optional<SessionToken> token = template.find(SessionToken.class, "abc123");
     * }</pre>
     * @param type the entity class
     * @param id   the id value
     * @param <T>  the entity class type
     * @param <K>  the id type
     * @return the entity instance, otherwise {@link Optional#empty()}
     * @throws NullPointerException when either the type or id are null
     */
    <T, K> Optional<T> find(Class<T> type, K id);

    /**
     * Deletes by ID or key.
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * template.delete(SessionToken.class, "abc123");
     * }</pre>
     * @param type the entity class
     * @param id   the id value
     * @param <T>  the entity class type
     * @param <K>  the id type
     * @throws NullPointerException when either the type or id are null
     */
    <T, K> void delete(Class<T> type, K id);

    /**
     * Start a query using the fluent API. The return value is a mutable and non-thread-safe instance.
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * List<Person> results = template.select(Person.class)
     *     .where("name").eq("Ada")
     *     .and("age").gte(30)
     *     .orderBy("age").asc()
     *     .limit(10)
     *     .result();
     * }</pre>
     * @param type the entity class
     * @param <T>  the entity type
     * @return a {@link QueryMapper.MapperFrom} instance
     * @throws NullPointerException          when type is null
     * @throws UnsupportedOperationException when the database cannot operate,
     *                                       such as key-value where most operations are key-based.
     */
    <T> QueryMapper.MapperFrom select(Class<T> type);

    /**
     * Start a query builder using the fluent API. The returned value is a mutable and non-thread-safe instance.
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * template.delete(Book.class)
     *     .where("author").eq("Ada")
     *     .and("publishedYear").gte(2020)
     *     .execute();
     * }</pre>
     * @param type the entity class
     * @param <T>  the entity type
     * @return a {@link QueryMapper.MapperDeleteFrom} instance
     * @throws NullPointerException          when type is null
     * @throws UnsupportedOperationException when the database cannot operate,
     *                                       such as key-value where most operations are key-based.
     */
    <T> QueryMapper.MapperDeleteFrom delete(Class<T> type);

    /**
     * Start an update builder using the fluent API. The returned value is a mutable and non-thread-safe instance.
     * <pre>{@code
     * @Inject
     * Template template;
     *
     * template.update(Book.class)
     *     .set("title").to("Domain-Driven Design with Java")
     *     .set("publishedYear").to(2025)
     *     .where("author").eq("Ada")
     *     .execute();
     * }</pre>
     *
     * This operation updates matching entities based on the defined predicates.
     * The update semantics and supported operations depend on the underlying database.
     *
     * @param type the entity class
     * @param <T>  the entity type
     * @return a {@link QueryMapper.MapperUpdateFrom} instance
     * @throws NullPointerException          when type is null
     * @throws UnsupportedOperationException when the database cannot operate,
     *                                       such as key-value stores that only support key-based updates.
     */
    <T> QueryMapper.MapperUpdateFrom update(Class<T> type);


    /**
     * Creates a query from a raw string using Jakarta Common Query Language (JCQL).
     * <p>
     * The entity type is inferred from the query string (e.g., {@code FROM Person}),
     * so no result class needs to be explicitly passed.
     * <p>
     * The returned {@link Query} instance is mutable and not thread-safe.
     * <p>
     * Example usage:
     * <pre>{@code
     * Query query = template.query("SELECT * FROM Person WHERE name = :name");
     * List<Person> people = query.bind("name", "Ada").result();
     * }</pre>
     *
     * @param query the Jakarta Common Query Language (JCQL) string to execute (e.g., {@code SELECT * FROM Person
     * WHERE active = true})
     * @return a new {@link Query} instance bound to this query string
     * @throws NullPointerException if the query string is {@code null}
     * @throws UnsupportedOperationException if the database does not support dynamic queries
     * @since 1.1.0
     */
    Query query(String query);

    /**
     * Creates a {@link TypedQuery} using the given query string and result type.
     *
     * <p>This method provides a type-safe way to execute queries by explicitly specifying the expected
     * result type. The provided {@code type} must be one of the following:
     * <ul>
     *  <li>An entity class annotated with {@code @Entity}. The query may explicitly include a {@code FROM} clause,
     *   or omit it, in which case the entity is inferred from the {@code type} parameter.</li>
     *   <li>A Java {@code record} annotated with {@code @Projection}, which maps partial or flattened results based on the query output.</li>
     * </ul>
     *
     * <p>When using a projection, the query can omit the {@code SELECT} clause entirely if the record component names match
     * the entity’s attributes. The {@code FROM} clause can only be omitted if the {@link jakarta.nosql.Projection#from()} attribute is specified.</p>
     *
     * <p>If the query references a different entity than the one implied by the {@code type} argument,
     * the provider may throw an {@link IllegalArgumentException} to indicate a mismatch.</p>
     *
     * <p>This method returns a {@link TypedQuery}, which improves safety and readability by:
     * <ul>
     *   <li>Restricting the result type to {@code T}, eliminating the need for casting</li>
     *   <li>Allowing fluent parameter binding and result handling</li>
     * </ul>
     *
     * <pre>{@code
     * List<TechProductView> techProducts = template
     *     .typedQuery("FROM Product WHERE category = 'TECH'", TechProductView.class)
     *     .result();
     *
     * Optional<PromotionalProduct> promo = template
     *     .typedQuery("WHERE price < :maxPrice", PromotionalProduct.class)
     *     .bind("maxPrice", 100)
     *     .singleResult();
     * }</pre>
     *
     * @param query the query string using Jakarta Common Query Language (JCQL)
     * @param type  the expected result type (entity or projection class)
     * @param <T>   the type of the result
     * @return a {@link TypedQuery} instance to bind parameters and fetch results
     * @throws NullPointerException     if the query or type is {@code null}
     * @throws IllegalArgumentException if the provider detects that the provided {@code type} is incompatible with the
     *                                  entity in the query
     * @throws UnsupportedOperationException if the query is not supported by the underlying provider
     * @since 1.1.0
     */
    <T> TypedQuery<T> typedQuery(String query, Class<T> type);

}
