package uts.edu.java.sitraeal.servicio;

import java.util.List;

import uts.edu.java.sitraeal.modelo.ReporteIncidente;

public interface ReporteIncidenteService {
List<ReporteIncidente>listar();
ReporteIncidente ObtenerReporte(Integer idReport);
ReporteIncidente guardar(ReporteIncidente Reporte);


}
