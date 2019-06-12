
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.claseSinNombre;

@Repository
public interface claseSinNombreRepository extends JpaRepository<claseSinNombre, Integer> {

}
