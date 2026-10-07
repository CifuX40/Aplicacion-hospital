package MarDeLuna.model

import MarDeLuna.view.Admin
import MarDeLuna.view.AgregarPublicacionUI
import MarDeLuna.view.Aspiracion
import MarDeLuna.view.CarroIngresos
import MarDeLuna.view.CarroParadas
import MarDeLuna.view.ChequeoRespirador
import MarDeLuna.view.ControlEnfermeria
import MarDeLuna.view.ControlesCarga
import MarDeLuna.view.Desfibrilador
import MarDeLuna.view.Empaquetado
import MarDeLuna.view.Endoscopios
import MarDeLuna.view.EquipoQuirofano
import MarDeLuna.view.Esterilizacion
import MarDeLuna.view.Evita600
import MarDeLuna.view.Habitacion
import MarDeLuna.view.Historia
import MarDeLuna.view.HojaInformativaPacientes
import MarDeLuna.view.HospitalDeDia
import MarDeLuna.view.Hospitalizacion
import MarDeLuna.view.IniciarSesion
import MarDeLuna.view.Lavadora
import MarDeLuna.view.PaginasWeb
import MarDeLuna.view.Plantas
import MarDeLuna.view.PrimeraPlanta
import MarDeLuna.view.ProcedimientoIngresos
import MarDeLuna.view.Publicaciones
import MarDeLuna.view.Quirofano
import MarDeLuna.view.RCP
import MarDeLuna.view.Rea
import MarDeLuna.view.ResiduosHospitalarios
import MarDeLuna.view.SalaEndoscopias
import MarDeLuna.view.SalaQuirofano
import MarDeLuna.view.Savina
import MarDeLuna.view.SegundaPlanta
import MarDeLuna.view.TomaConstantes
import MarDeLuna.view.TomaOxigeno
import MarDeLuna.view.Uci
import MarDeLuna.view.UciMedica
import MarDeLuna.view.UciPostquirurgica
import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*

@Composable
fun Navegacion(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "iniciar_sesion") {
        composable("agregar_publicacion") { AgregarPublicacionUI(navController) }
        composable("admin") { Admin(navController) }
        composable("aspiracion") { Aspiracion(navController) }
        composable("carro_ingresos") { CarroIngresos(navController) }
        composable("carro_paradas") { CarroParadas(navController) }
        composable("chequeo_respirador") { ChequeoRespirador(navController) }
        composable("controles_carga") { ControlesCarga(navController) }
        composable("control_enfermeria") { ControlEnfermeria(navController) }
        composable("desfibrilador") { Desfibrilador(navController) }
        composable("empaquetado") { Empaquetado(navController) }
        composable("endoscopios") { Endoscopios(navController) }
        composable("equipo_quirofano") { EquipoQuirofano(navController) }
        composable("esterilizacion") { Esterilizacion(navController) }
        composable("evita_600") { Evita600(navController) }
        composable("habitacion") { Habitacion(navController) }
        composable("historia") { Historia(navController) }
        composable("hoja_informativa_pacientes") { HojaInformativaPacientes(navController) }
        composable("hospital_dia") { HospitalDeDia(navController) }
        composable("hospitalizacion") { Hospitalizacion(navController) }
        composable("lavadora") { Lavadora(navController) }
        composable("Plantas") { Plantas(navController) }
        composable("paginas_web") { PaginasWeb(navController) }
        composable("primera_planta") { PrimeraPlanta(navController) }
        composable("procedimiento_ingresos") { ProcedimientoIngresos(navController) }
        composable("publicaciones") { Publicaciones(navController) }
        composable("toma_constantes") { TomaConstantes(navController) }
        composable("quirófano") { Quirofano(navController) }
        composable("rcp") { RCP(navController) }
        composable("rea") { Rea(navController) }
        composable("residuos_hospitalarios") { ResiduosHospitalarios(navController) }
        composable("sala_endoscopias") { SalaEndoscopias(navController) }
        composable("sala_quirofano") { SalaQuirofano(navController) }
        composable("savina") { Savina(navController) }
        composable("segunda_planta") { SegundaPlanta(navController) }
        composable("iniciar_sesion") { IniciarSesion(navController) }
        composable("toma_oxigeno") { TomaOxigeno(navController) }
        composable("uci") { Uci(navController) }
        composable("uci_medica") { UciMedica(navController) }
        composable("uci_postquirurgica") { UciPostquirurgica(navController) }
    }
}