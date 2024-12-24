package jakarta.nosql.tck.entities;

import jakarta.nosql.Id;
import jakarta.nosql.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAnimal {

    @Id
    protected String id;
}
