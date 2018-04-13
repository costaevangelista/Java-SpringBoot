package br.com.costawebs.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.costawebs.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

}
