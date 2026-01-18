package jakarta.nosql.communication.spi;

import java.util.function.Function;


public interface DatabaseManagerFactory extends AutoCloseable {

    DatabaseManager create(String databaseName);

    @Override
    void close();
}
