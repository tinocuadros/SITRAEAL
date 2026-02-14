package uts.edu.java.sitraeal.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uts.edu.java.sitraeal.modelo.HistorialEstados;
import uts.edu.java.sitraeal.repositorio.HistorialEstadoRepository;
@Service
public class HistorialServiceImpl implements HistorialEstadoService {

	@Autowired
    private HistorialEstadoRepository historialRepo;
	
	@Override
	public List<HistorialEstados> listarHistorialPorEquipo(Integer idEquipo) {
		return historialRepo.findByEquipoIdEquipoOrderByFechaMovimientoDesc(idEquipo);
	}

	@Override
	@Transactional
	public void guardar(HistorialEstados historial) {
		historialRepo.save(historial);
		
	}

}
