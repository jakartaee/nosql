package jakarta.nosql.communication.spi;

import java.util.function.Function;

/**
 * This interface represents a factory for creating database manager instances based on a database name.
 * It should throw a {@link NullPointerException} when the {@link String} parameter is null.
 *
 * <p>The {@link DatabaseManagerFactory} extends {@link java.util.function.Function}, indicating that it can be
 * used as a function to create database manager instances from database names.</p>
 *
 * <p>Implementations of this interface are expected to provide the necessary logic to instantiate appropriate
 * {@link DatabaseManager} instances based on the provided database name.</p>
 *
 * <p>Additionally, this interface extends {@link AutoCloseable}, indicating that implementations may manage
 * resources that require cleanup. The {@link #close()} method should be called to release any resources
 * held by the factory.</p>
 *
 * @see DatabaseManager
 */
public interface DatabaseManagerFactory extends Function<String, DatabaseManager>, AutoCloseable {


    /**
     * Closes any resources held by the factory.
     *
     * <p>Note: Some databases may not perform any specific actions upon closing.</p>
     */
    @Override
    void close();
}
