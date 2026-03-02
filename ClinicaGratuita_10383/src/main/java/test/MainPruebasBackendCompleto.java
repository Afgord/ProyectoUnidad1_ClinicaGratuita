/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import controller.Agenda_DiariaController;
import controller.Cita_MedicaController;
import controller.DoctorController;
import controller.MedicamentoController;
import controller.PacienteController;
import controller.RecetaController;
import controller.TratamientoController;
import controller.Tratamiento_MedicamentoController;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import model.Agenda_Diaria;
import model.Cita_Medica;
import model.Doctor;
import model.Medicamento;
import model.Paciente;
import model.Receta;
import model.Tratamiento;
import model.Tratamiento_Medicamento;

/**
 *
 * @author Afgord
 */
public class MainPruebasBackendCompleto {

    public static void main(String[] args) {
        banner();

        try {
            // 0) Controladores
            PacienteController pacienteC = new PacienteController();
            DoctorController doctorC = new DoctorController();
            Cita_MedicaController citaC = new Cita_MedicaController();
            Agenda_DiariaController agendaC = new Agenda_DiariaController();
            TratamientoController tratamientoC = new TratamientoController();
            MedicamentoController medicamentoC = new MedicamentoController();
            Tratamiento_MedicamentoController tmC = new Tratamiento_MedicamentoController();
            RecetaController recetaC = new RecetaController();

            // 1) Paciente
            int idPaciente = pruebasPaciente(pacienteC);

            // 2) Doctor
            int idDoctor = pruebasDoctor(doctorC);

            // 3) Cita médica (crea una cita válida en el próximo día hábil)
            int idCita = pruebasCitaMedica(citaC, idPaciente, idDoctor);

            // 4) Agenda del día (no asumimos que haya citas hoy)
            pruebasAgenda(agendaC);

            // 5) Tratamiento (finalizar consulta por SP) -> id_tratamiento
            int idTratamiento = pruebasTratamiento(tratamientoC, idCita);

            // 6) Medicamento (catálogo + UNIQUE)
            int idMedicamento = pruebasMedicamento(medicamentoC);

            // 7) Receta (relación + vista) usando idTratamiento/idCita
            pruebasReceta(tmC, recetaC, idTratamiento, idCita, idMedicamento);

            ok("\n✅ PRUEBAS E2E COMPLETADAS.");

        } catch (Exception e) {
            System.out.println("\n❌ PRUEBAS DETENIDAS POR EXCEPCIÓN:");
            e.printStackTrace();
        }
    }

    // =========================================================
    // Helpers: generación de datos únicos (evita choques con UNIQUE)
    // =========================================================
    private static String emailUnico(String prefijo, String dominio) {
        return prefijo + System.nanoTime() + "@" + dominio;
    }

    /**
     * Teléfono único de 10 dígitos. Mantiene tu estilo: "644" + 7 dígitos = 10.
     */
    private static String telefonoUnico10() {
        long n = Math.abs(System.nanoTime());
        String s = String.valueOf(n);

        // últimos 7 dígitos
        String ult7 = s.substring(Math.max(0, s.length() - 7));
        // rellenar a 7 si quedó corto
        ult7 = String.format("%7s", ult7).replace(' ', '0');

        return "644" + ult7; // 10 dígitos
    }

    // =========================================================
    // Helpers de salida / asserts
    // =========================================================
    private static void banner() {
        System.out.println("============================================");
        System.out.println("   PRUEBAS BACKEND - CLINICA GRATUITA (E2E)");
        System.out.println("============================================\n");
    }

    private static void ok(String msg) {
        System.out.println("✅ " + msg);
    }

    private static void info(String msg) {
        System.out.println("ℹ️  " + msg);
    }

    private static void warn(String msg) {
        System.out.println("⚠️  " + msg);
    }

    private static void fail(String msg) {
        throw new RuntimeException("❌ " + msg);
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            fail(msg);
        }
        ok(msg);
    }

    private static void assertFalse(boolean cond, String msg) {
        if (cond) {
            fail(msg);
        }
        ok(msg);
    }

    private static void assertNotNull(Object obj, String msg) {
        if (obj == null) {
            fail(msg);
        }
        ok(msg);
    }

    private static void assertGreater(int value, int min, String msg) {
        if (value <= min) {
            fail(msg + " (valor=" + value + ")");
        }
        ok(msg + " (valor=" + value + ")");
    }

    // =========================================================
    // 1) PACIENTE
    // =========================================================
    private static int pruebasPaciente(PacienteController pc) {
        System.out.println("---- 1) PACIENTE ----");

        assertFalse(
                pc.insertarPaciente("", 20, "Masculino", "Dir", "a@b.com", "6441234567"),
                "Paciente: nombre vacío debe fallar"
        );
        info("Mensaje: " + pc.getUltimo_mensaje());

        assertFalse(
                pc.insertarPaciente("Juan", -1, "Masculino", "Dir", "a@b.com", "6441234567"),
                "Paciente: edad fuera de rango debe fallar"
        );
        info("Mensaje: " + pc.getUltimo_mensaje());

        assertFalse(
                pc.insertarPaciente("Juan", 20, "Otro", "Dir", "a@b.com", "6441234567"),
                "Paciente: sexo inválido debe fallar"
        );
        info("Mensaje: " + pc.getUltimo_mensaje());

        assertFalse(
                pc.insertarPaciente("Juan", 20, "Masculino", "Dir", "correo_mal", "6441234567"),
                "Paciente: email inválido debe fallar"
        );
        info("Mensaje: " + pc.getUltimo_mensaje());

        assertFalse(
                pc.insertarPaciente("Juan", 20, "Masculino", "Dir", "a@b.com", "123"),
                "Paciente: teléfono inválido debe fallar"
        );
        info("Mensaje: " + pc.getUltimo_mensaje());

        // ✅ Email y teléfono únicos
        String email = emailUnico("p", "mx.com");
        String tel = telefonoUnico10();

        info("Email generado: " + email);
        info("Teléfono generado: " + tel);

        boolean okInsert = pc.insertarPaciente(
                "Paciente Prueba",
                25,
                "Masculino",
                "Calle Prueba 123",
                email,
                tel
        );

        info("Insert OK?: " + okInsert);
        info("Mensaje controller: " + pc.getUltimo_mensaje());
        assertTrue(okInsert, "Paciente: insertar OK");

        int idPaciente = buscarPacienteIdPorEmail(pc.obtenerTodos(), email);
        assertGreater(idPaciente, 0, "Paciente: obtener ID por email");

        // duplicado email (tel único para asegurar que el fallo sea por email)
        boolean dup = pc.insertarPaciente(
                "Paciente Duplicado",
                30,
                "Masculino",
                "Otra",
                email,
                telefonoUnico10()
        );
        info("Insert duplicado?: " + dup);
        info("Mensaje controller: " + pc.getUltimo_mensaje());
        assertFalse(dup, "Paciente: insertar con email duplicado debe fallar");

        Paciente p = pc.obtenerPorId(idPaciente);
        assertNotNull(p, "Paciente: obtenerPorId OK");

        // update OK (email/tel únicos)
        p.setDireccion("Calle Actualizada 999");
        p.setTelefono(telefonoUnico10());
        p.setEmail(("upd_" + emailUnico("p", "mx.com")).toLowerCase());

        boolean updOk = pc.actualizar(p);
        info("Update OK?: " + updOk);
        info("Mensaje controller: " + pc.getUltimo_mensaje());
        assertTrue(updOk, "Paciente: actualizar OK");

        // update inválido (tel)
        p.setTelefono("abc");
        boolean updBad = pc.actualizar(p);
        info("Update bad?: " + updBad);
        info("Mensaje controller: " + pc.getUltimo_mensaje());
        assertFalse(updBad, "Paciente: actualizar con teléfono inválido debe fallar");

        System.out.println();
        return idPaciente;
    }

    private static int buscarPacienteIdPorEmail(List<Paciente> lista, String email) {
        if (lista == null) {
            return 0;
        }
        return lista.stream()
                .filter(x -> x.getEmail() != null && x.getEmail().equalsIgnoreCase(email))
                .map(Paciente::getId_paciente)
                .findFirst()
                .orElse(0);
    }

    // =========================================================
    // 2) DOCTOR
    // =========================================================
    private static int pruebasDoctor(DoctorController dc) {
        System.out.println("---- 2) DOCTOR ----");

        // inválidos
        assertFalse(
                dc.insertarDoctor("", "d@mail.com", "6441234567", "General"),
                "Doctor: nombre vacío debe fallar"
        );
        info("Mensaje: " + dc.getUltimo_mensaje());

        assertFalse(
                dc.insertarDoctor("Doc", "correo_mal", "6441234567", "General"),
                "Doctor: email inválido debe fallar"
        );
        info("Mensaje: " + dc.getUltimo_mensaje());

        assertFalse(
                dc.insertarDoctor("Doc", "d@mail.com", "123", "General"),
                "Doctor: teléfono inválido debe fallar"
        );
        info("Mensaje: " + dc.getUltimo_mensaje());

        // Insert OK (email/tel únicos)
        String email = emailUnico("doctor_test_", "mail.com");
        String tel = telefonoUnico10();

        info("Email generado: " + email);
        info("Teléfono generado: " + tel);

        boolean okInsert = dc.insertarDoctor(
                "Doctor Prueba",
                email,
                tel,
                "Medicina General"
        );
        info("Insert OK?: " + okInsert);
        info("Mensaje controller: " + dc.getUltimo_mensaje());
        assertTrue(okInsert, "Doctor: insertar OK");

        int idDoctor = buscarDoctorIdPorEmail(dc.obtenerTodos(), email);
        assertGreater(idDoctor, 0, "Doctor: obtener ID por email");

        // duplicado email (tel único para asegurar que falle por email)
        boolean dup = dc.insertarDoctor("Doctor Duplicado", email, telefonoUnico10(), "X");
        info("Insert duplicado?: " + dup);
        info("Mensaje controller: " + dc.getUltimo_mensaje());
        assertFalse(dup, "Doctor: insertar con email duplicado debe fallar");

        // obtenerPorId
        Doctor d = dc.obtenerPorId(idDoctor);
        assertNotNull(d, "Doctor: obtenerPorId OK");
        info("Doctor obtenido: id=" + d.getId_doctor() + ", email=" + d.getEmail());

        // actualizar OK (tel único)
        d.setTelefono(telefonoUnico10());
        boolean updOk = dc.actualizar(d);
        info("Update OK?: " + updOk);
        info("Mensaje controller: " + dc.getUltimo_mensaje());
        assertTrue(updOk, "Doctor: actualizar OK");

        // actualizar inválido (estado de teléfono)
        d.setTelefono("12");
        boolean updBad = dc.actualizar(d);
        info("Update bad?: " + updBad);
        info("Mensaje controller: " + dc.getUltimo_mensaje());
        assertFalse(updBad, "Doctor: actualizar con teléfono inválido debe fallar");

        System.out.println();
        return idDoctor;
    }

    private static int buscarDoctorIdPorEmail(List<Doctor> lista, String email) {
        if (lista == null) {
            return 0;
        }
        return lista.stream()
                .filter(x -> x.getEmail() != null && x.getEmail().equalsIgnoreCase(email))
                .map(Doctor::getId_doctor)
                .findFirst()
                .orElse(0);
    }

    // =========================================================
    // 3) CITA MÉDICA
    // =========================================================
    private static int pruebasCitaMedica(Cita_MedicaController cc, int idPaciente, int idDoctor) {
        System.out.println("---- 3) CITA MÉDICA ----");

        // Construimos una cita VÁLIDA: próximo día hábil, hora 10:30 (en turno)
        LocalDate fechaValida = siguienteDiaHabil(LocalDate.now());
        LocalTime horaValida = LocalTime.of(10, 30);

        Date fechaSql = Date.valueOf(fechaValida);
        Time horaSql = Time.valueOf(horaValida);

        // Insert OK
        boolean okInsert = cc.insertar(
                fechaSql,
                horaSql,
                "Consulta de prueba (backend)",
                idPaciente,
                idDoctor
        );
        assertTrue(okInsert, "Cita: insertar OK (día hábil y horario permitido)");
        info("Mensaje: " + cc.getUltimo_mensaje());

        int idCita = buscarIdCitaRecienCreada(cc.obtenerTodos(), fechaSql, horaSql, idPaciente, idDoctor);
        assertGreater(idCita, 0, "Cita: obtener ID recién creada");

        // inválido: fecha en pasado
        boolean past = cc.insertar(
                Date.valueOf(LocalDate.now().minusDays(1)),
                horaSql,
                "X",
                idPaciente,
                idDoctor
        );
        assertFalse(past, "Cita: fecha en pasado debe fallar");
        info("Mensaje: " + cc.getUltimo_mensaje());

        // inválido: fuera de horario (14:00)
        boolean badHour = cc.insertar(
                fechaSql,
                Time.valueOf(LocalTime.of(14, 0)),
                "X",
                idPaciente,
                idDoctor
        );
        assertFalse(badHour, "Cita: hora fuera de horario debe fallar");
        info("Mensaje: " + cc.getUltimo_mensaje());

        // inválido: fin de semana
        LocalDate sabado = proximoSabado(LocalDate.now());
        boolean weekend = cc.insertar(
                Date.valueOf(sabado),
                horaSql,
                "X",
                idPaciente,
                idDoctor
        );
        assertFalse(weekend, "Cita: fin de semana debe fallar");
        info("Mensaje: " + cc.getUltimo_mensaje());

        // Actualizar: estado inválido
        Cita_Medica cita = cc.obtenerPorId(idCita);
        assertNotNull(cita, "Cita: obtenerPorId OK");

        cita.setEstado("invalido");
        boolean updBad = cc.actualizar(cita);
        assertFalse(updBad, "Cita: actualizar con estado inválido debe fallar");
        info("Mensaje: " + cc.getUltimo_mensaje());

        // Actualizar: estado válido (mantenemos programada para poder finalizar consulta después)
        cita.setEstado("en curso");
        boolean updOk = cc.actualizar(cita);
        assertTrue(updOk, "Cita: actualizar estado válido OK");
        info("Mensaje: " + cc.getUltimo_mensaje());

        System.out.println();
        return idCita;
    }

    private static int buscarIdCitaRecienCreada(List<Cita_Medica> lista, Date fecha, Time hora, int idPaciente, int idDoctor) {
        if (lista == null || lista.isEmpty()) {
            return 0;
        }

        Optional<Cita_Medica> match = lista.stream()
                .filter(c -> c.getFecha() != null && c.getHora() != null)
                .filter(c -> c.getFecha().equals(fecha))
                .filter(c -> c.getHora().equals(hora))
                .filter(c -> c.getId_paciente() == idPaciente)
                .filter(c -> c.getId_doctor() == idDoctor)
                // si hay varias por coincidencia, tomamos la de mayor id
                .max(Comparator.comparingInt(Cita_Medica::getId_cita));

        return match.map(Cita_Medica::getId_cita).orElse(0);
    }

    private static LocalDate siguienteDiaHabil(LocalDate desde) {
        LocalDate d = desde;
        // Para ser más seguro, usamos mañana como base:
        d = d.plusDays(1);
        while (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
            d = d.plusDays(1);
        }
        return d;
    }

    private static LocalDate proximoSabado(LocalDate desde) {
        LocalDate d = desde;
        while (d.getDayOfWeek() != DayOfWeek.SATURDAY) {
            d = d.plusDays(1);
        }
        // asegurar que sea futuro (si hoy ya es sábado, usamos el siguiente)
        if (d.equals(desde)) {
            d = d.plusDays(7);
        }
        return d;
    }

    // =========================================================
    // 4) AGENDA HOY
    // =========================================================
    private static void pruebasAgenda(Agenda_DiariaController ac) {
        System.out.println("---- 4) AGENDA HOY ----");

        List<Agenda_Diaria> agenda = ac.obtenerAgendaHoy();
        assertNotNull(agenda, "Agenda: obtenerAgendaHoy no null");
        info("Mensaje: " + ac.getUltimo_mensaje());
        info("Citas en agenda hoy: " + agenda.size());

        if (agenda.isEmpty()) {
            warn("Agenda hoy vacía (esto puede ser normal).");
        }

        System.out.println();
    }

    // =========================================================
    // 5) TRATAMIENTO (SP finalizar consulta) -> id_tratamiento
    // =========================================================
    private static int pruebasTratamiento(TratamientoController tc, int idCita) {
        System.out.println("---- 5) TRATAMIENTO (FINALIZAR CONSULTA) ----");

        assertFalse(tc.finalizarConsulta(0, "X", 5), "Tratamiento: id_cita inválido debe fallar");
        info("Mensaje: " + tc.getUltimo_mensaje());

        assertFalse(tc.finalizarConsulta(idCita, "   ", 5), "Tratamiento: descripción vacía debe fallar");
        info("Mensaje: " + tc.getUltimo_mensaje());

        assertFalse(tc.finalizarConsulta(idCita, "X", 0), "Tratamiento: duración inválida debe fallar");
        info("Mensaje: " + tc.getUltimo_mensaje());

        boolean finOk = tc.finalizarConsulta(idCita, "Tratamiento de prueba (backend)", 7);
        info("Finalizar OK?: " + finOk);
        info("Mensaje: " + tc.getUltimo_mensaje());
        assertTrue(finOk, "Tratamiento: finalizarConsulta OK");

        int idTrat = buscarTratamientoPorCita(tc.obtenerTodos(), idCita);
        assertGreater(idTrat, 0, "Tratamiento: obtener id_tratamiento por id_cita");

        System.out.println();
        return idTrat;
    }

    private static int buscarTratamientoPorCita(List<Tratamiento> lista, int idCita) {
        if (lista == null || lista.isEmpty()) {
            return 0;
        }

        return lista.stream()
                .filter(t -> t.getId_cita() == idCita)
                .max(Comparator.comparingInt(Tratamiento::getId_tratamiento))
                .map(Tratamiento::getId_tratamiento)
                .orElse(0);
    }

    // =========================================================
    // 6) MEDICAMENTO (UNIQUE nombre)
    // =========================================================
    private static int pruebasMedicamento(MedicamentoController mc) {
        System.out.println("---- 6) MEDICAMENTO ----");

        String nombre = "TEST_MED_" + System.currentTimeMillis();
        assertTrue(mc.insertar(nombre), "Medicamento: insertar OK");
        info("Mensaje: " + mc.getUltimo_mensaje());

        assertFalse(mc.insertar(nombre), "Medicamento: duplicado nombre debe fallar (UNIQUE)");
        info("Mensaje: " + mc.getUltimo_mensaje());

        List<Medicamento> lista = mc.obtenerTodos();
        assertNotNull(lista, "Medicamento: obtenerTodos no null");
        assertTrue(!lista.isEmpty(), "Medicamento: obtenerTodos no vacío");
        info("Mensaje: " + mc.getUltimo_mensaje());

        int idMed = buscarMedicamentoIdPorNombre(lista, nombre);
        if (idMed <= 0) {
            warn("No encontré el medicamento por nombre; tomaré uno existente para receta.");
            idMed = lista.get(0).getId_medicamento();
        }
        assertGreater(idMed, 0, "Medicamento: ID disponible");

        Medicamento m = mc.obtenerPorId(idMed);
        assertNotNull(m, "Medicamento: obtenerPorId OK");

        System.out.println();
        return idMed;
    }

    private static int buscarMedicamentoIdPorNombre(List<Medicamento> lista, String nombre) {
        if (lista == null) {
            return 0;
        }
        return lista.stream()
                .filter(x -> x.getNombre() != null && x.getNombre().equalsIgnoreCase(nombre))
                .map(Medicamento::getId_medicamento)
                .findFirst()
                .orElse(0);
    }

    // =========================================================
    // 7) RECETA (relación + vista) + eliminar
    // =========================================================
    private static void pruebasReceta(Tratamiento_MedicamentoController tmc,
            RecetaController rc,
            int idTratamiento,
            int idCita,
            int idMedicamento) {
        System.out.println("---- 7) RECETA (RELACION + VISTA) ----");

        // Agregar OK
        boolean addOk = tmc.agregarMedicamento(idTratamiento, idMedicamento, "Tomar 1 capsula cada 8 horas");
        info("Agregar OK?: " + addOk);
        info("Mensaje: " + tmc.getUltimo_mensaje());
        info("IDs usados -> idTratamiento=" + idTratamiento + ", idMedicamento=" + idMedicamento);
        assertTrue(addOk, "Receta: agregar medicamento OK");

        // Agregar duplicado
        assertFalse(
                tmc.agregarMedicamento(idTratamiento, idMedicamento, "Repetido"),
                "Receta: agregar duplicado debe fallar"
        );
        info("Mensaje: " + tmc.getUltimo_mensaje());

        // Tabla puente por tratamiento
        List<Tratamiento_Medicamento> puente = tmc.obtenerPorTratamiento(idTratamiento);
        assertNotNull(puente, "Receta: obtenerPorTratamiento no null");
        info("Mensaje: " + tmc.getUltimo_mensaje());
        info("Items (tabla puente): " + puente.size());

        // Vista completa por cita
        List<Receta> vista = rc.obtenerRecetaPorCita(idCita);
        assertNotNull(vista, "Receta: vista_receta_completa no null");
        info("Mensaje: " + rc.getUltimo_mensaje());

        vista.forEach(r -> System.out.println(
                "   - " + r.getMedicamento() + " | " + r.getIndicaciones()
                + " | " + r.getDias_tratamiento() + " días"
        ));

        // Eliminar de receta
        assertTrue(
                tmc.eliminar(idTratamiento, idMedicamento),
                "Receta: eliminar medicamento OK"
        );
        info("Mensaje: " + tmc.getUltimo_mensaje());

        // Vista después de eliminar (puede quedar vacía)
        List<Receta> vista2 = rc.obtenerRecetaPorCita(idCita);
        assertNotNull(vista2, "Receta: vista después de eliminar no null");
        info("Items receta después de eliminar: " + vista2.size());

        System.out.println();
    }

}
