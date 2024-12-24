package jakarta.nosql.tck.entities;

import jakarta.nosql.Id;
import jakarta.nosql.MappedSuperclass;

import java.util.Objects;

@MappedSuperclass
public abstract class AbstractAnimal {

    @Id
    protected String id;


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbstractAnimal that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
