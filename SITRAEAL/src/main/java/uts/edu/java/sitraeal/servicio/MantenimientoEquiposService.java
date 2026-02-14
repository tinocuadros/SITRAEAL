package uts.edu.java.sitraeal.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uts.edu.java.sitraeal.modelo.Equipo;
import uts.edu.java.sitraeal.modelo.EstadoEquipo;
import uts.edu.java.sitraeal.repositorio.EquipoRepository;
import uts.edu.java.sitraeal.repositorio.EstadoEquipoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class MantenimientoEquiposService {

	@Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private EstadoEquipoRepository estadoEquipoRepository; 

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void actualizarEquiposVencidos() {
        LocalDate hoy = LocalDate.now();
        
        EstadoEquipo estadoVencido = estadoEquipoRepository.findOptionalByNombre("VENCIDO")
                .orElse(null); 

        if (estadoVencido == null) {
            System.err.println("SITRAEAL ERROR: El estado 'Vencido' no existe en la BD. Abortando actualización.");
            return;
        }

        // Buscamos equipos cuya fecha sea anterior a hoy
        List<Equipo> equiposVencidos = equipoRepository.findByFechaVencimientoBefore(hoy);

        int contador = 0;
        for (Equipo equipo : equiposVencidos) {
            // Solo cambiamos el estado si es diferente a 'Vencido'
            // Comparamos los IDs para mayor seguridad
            if (equipo.getEstado() == null || !equipo.getEstado().getIdEstado().equals(estadoVencido.getIdEstado())) {
                equipo.setEstado(estadoVencido);
                contador++;
            }
        }

        if (contador > 0) {
            equipoRepository.saveAll(equiposVencidos);
            System.out.println("SITRAEAL: Se han actualizado " + contador + " equipos al estado 'Vencido'.");
        }
    }
}