package uts.edu.java.sitraeal.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import uts.edu.java.sitraeal.modelo.ReporteIncidente;
import uts.edu.java.sitraeal.repositorio.ReporteIncidenteRepository;
@Service
public class ReporteIncidenteServiceImpl implements ReporteIncidenteService {

	@Autowired
    private ReporteIncidenteRepository reporteIncidenteRepo;
	
	@Autowired
	private ReporteIncidenteRepository reporteIncidente;
	@Override
	public List<ReporteIncidente> listar() {
		
		return reporteIncidente.findAll();
	}

	@Override
	public ReporteIncidente ObtenerReporte(Integer idReport) {
		
		return reporteIncidente.findById(idReport).orElse(null);
	}

	@Override
	@Transactional
	public ReporteIncidente guardar(ReporteIncidente Reporte) {
		// TODO Auto-generated method stub
		return reporteIncidenteRepo.save(Reporte);
	}

}
