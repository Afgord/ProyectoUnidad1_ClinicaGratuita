/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package views;

import controller.Cita_MedicaController;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Afgord
 */
public class frmAgendarCita extends javax.swing.JPanel {

    private final frmPrincipal principal;
    private final java.util.List<String> horasBase = new java.util.ArrayList<>();

    private static class ComboItem {

        final int id;
        final String text;

        ComboItem(int id, String text) {
            this.id = id;
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * Creates new form frmAgendarCita
     */
    public frmAgendarCita(frmPrincipal principal) {
        this.principal = principal;

        initComponents();

        dateCita.getDateEditor().addPropertyChangeListener("date", evt -> {
            java.util.Date d = dateCita.getDate();
            if (d == null) {
                return;
            }

            java.time.LocalDate ld = new java.sql.Date(d.getTime()).toLocalDate();
            java.time.DayOfWeek dow = ld.getDayOfWeek();

            if (dow == java.time.DayOfWeek.SATURDAY || dow == java.time.DayOfWeek.SUNDAY) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Solo hay consultas de Lunes a Viernes.");
                dateCita.setDate(null);
            }
        });

        // Editables para buscar escribiendo
        cmbPaciente.setEditable(true);
        cmbDoctor.setEditable(true);

        // Configurar fecha mínima (hoy) en el JDateChooser
        dateCita.setMinSelectableDate(new java.util.Date());

        // Cargar combos
        cargarHoras();
        cmbDoctor.addActionListener(e -> refrescarHorasDisponibles());
        dateCita.getDateEditor().addPropertyChangeListener("date", evt -> refrescarHorasDisponibles());
        cargarPacientes();
        cargarDoctores();
    }

    private void cargarHoras() {
        horasBase.clear();

        // Turno matutino 07:00 - 12:30
        LocalTime t = LocalTime.of(7, 0);
        while (!t.isAfter(LocalTime.of(12, 30))) {
            horasBase.add(String.format("%02d:%02d", t.getHour(), t.getMinute()));
            t = t.plusMinutes(30);
        }

        // Turno vespertino 15:00 - 18:30
        t = LocalTime.of(15, 0);
        while (!t.isAfter(LocalTime.of(18, 30))) {
            horasBase.add(String.format("%02d:%02d", t.getHour(), t.getMinute()));
            t = t.plusMinutes(30);
        }

        // Inicialmente el combo con todas (o vacío si prefieres)
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String h : horasBase) {
            model.addElement(h);
        }
        cmbHora.setModel(model);
        cmbHora.setSelectedIndex(-1);
    }

    private void refrescarHorasDisponibles() {
        int idDoctor = getSelectedId(cmbDoctor);
        Date fecha = getSqlDateFromChooser();

        // Si todavía no hay doctor o fecha, no filtramos: mostramos todas las horas base
        if (idDoctor <= 0 || fecha == null) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (String h : horasBase) {
                model.addElement(h);
            }
            cmbHora.setModel(model);
            cmbHora.setSelectedIndex(-1);
            return;
        }

        // Consultar horas ocupadas en BD (programada/en curso)
        Cita_MedicaController cc = new Cita_MedicaController();
        java.util.List<Time> ocupadas = cc.obtenerHorasOcupadas(idDoctor, fecha);

        // Convertir ocupadas a "HH:mm"
        java.util.Set<String> ocupadasStr = new java.util.HashSet<>();
        for (Time t : ocupadas) {
            if (t != null) {
                String s = t.toLocalTime().toString(); // puede venir "10:30" o "10:30:00"
                if (s.length() >= 5) {
                    s = s.substring(0, 5);
                }
                ocupadasStr.add(s);
            }
        }

        // Reconstruir combo con las NO ocupadas
        String seleccionActual = (cmbHora.getSelectedItem() != null)
                ? cmbHora.getSelectedItem().toString().trim()
                : null;

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String h : horasBase) {
            if (!ocupadasStr.contains(h)) {
                model.addElement(h);
            }
        }

        cmbHora.setModel(model);

        // Si la hora que estaba seleccionada ya no está disponible, limpiar; si sí, conservar
        if (seleccionActual != null) {
            model.setSelectedItem(seleccionActual);
            if (!seleccionActual.equals(model.getSelectedItem())) {
                cmbHora.setSelectedIndex(-1);
            }
        } else {
            cmbHora.setSelectedIndex(-1);
        }
    }

    // TODO: aquí conectas con tu lógica real (vista_directorio_pacientes)
    private void cargarPacientes() {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();

        dao.PacienteDAO dao = new dao.PacienteDAO();
        for (model.Paciente p : dao.obtenerTodos()) {
            model.addElement(new ComboItem(p.getId_paciente(), p.getId_paciente() + " - " + p.getNombre()));
        }

        cmbPaciente.setModel(model);
        cmbPaciente.setSelectedIndex(-1);
    }

    // TODO: aquí conectas con tu lógica real (vista_seleccion_doctor)
    private void cargarDoctores() {
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();

        dao.DoctorDAO dao = new dao.DoctorDAO();
        for (model.Doctor d : dao.obtenerTodos()) {
            String texto = d.getId_doctor() + " - " + d.getNombre() + " - " + d.getEspecialidad();
            model.addElement(new ComboItem(d.getId_doctor(), texto));
        }

        cmbDoctor.setModel(model);
        cmbDoctor.setSelectedIndex(-1);
    }

    private int getSelectedId(javax.swing.JComboBox<?> combo) {
        Object sel = combo.getSelectedItem();
        if (sel instanceof ComboItem item) {
            return item.id;
        }

        // Si el usuario escribió texto, intentar extraer ID al inicio: "12 - Nombre..."
        if (sel != null) {
            String s = sel.toString().trim();
            try {
                // intenta parsear antes de un espacio o guion
                String num = s.split("\\s|-", 2)[0].trim();
                return Integer.parseInt(num);
            } catch (Exception ignore) {
            }
        }
        return -1;
    }

    private Date getSqlDateFromChooser() {
        java.util.Date d = dateCita.getDate();
        if (d == null) {
            return null;
        }
        return new Date(d.getTime());
    }

    private Time getSqlTimeFromCombo() {
        Object sel = cmbHora.getSelectedItem();
        if (sel == null) {
            return null;
        }

        String s = sel.toString().trim(); // "HH:mm"
        try {
            LocalTime lt = LocalTime.parse(s.length() == 5 ? s : s.substring(0, 5));
            return Time.valueOf(lt);
        } catch (Exception e) {
            return null;
        }
    }

    private void limpiarFormulario() {
        ((javax.swing.JComboBox<?>) cmbPaciente).setSelectedIndex(-1);
        ((javax.swing.JComboBox<?>) cmbDoctor).setSelectedIndex(-1);
        cmbHora.setSelectedIndex(-1);
        txtMotivo.setText("");
        dateCita.setDate(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHEADER = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlBODY = new javax.swing.JPanel();
        lblPaciente = new javax.swing.JLabel();
        cmbPaciente = new javax.swing.JComboBox();
        lblDoctor = new javax.swing.JLabel();
        cmbDoctor = new javax.swing.JComboBox();
        lblFecha = new javax.swing.JLabel();
        dateCita = new com.toedter.calendar.JDateChooser();
        lblHora = new javax.swing.JLabel();
        cmbHora = new javax.swing.JComboBox<>();
        lblMotivo = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        pnlBOTTOM = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setText("AGENDAR CITA MÉDICA");

        javax.swing.GroupLayout pnlHEADERLayout = new javax.swing.GroupLayout(pnlHEADER);
        pnlHEADER.setLayout(pnlHEADERLayout);
        pnlHEADERLayout.setHorizontalGroup(
            pnlHEADERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHEADERLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHEADERLayout.setVerticalGroup(
            pnlHEADERLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHEADERLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        lblPaciente.setText("Paciente");

        cmbPaciente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbPaciente.addActionListener(this::cmbPacienteActionPerformed);

        lblDoctor.setText("Doctor");

        cmbDoctor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblFecha.setText("Fecha");

        lblHora.setText("Hora");

        cmbHora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblMotivo.setText("Motivo");

        txtMotivo.setColumns(20);
        txtMotivo.setRows(5);
        jScrollPane2.setViewportView(txtMotivo);

        javax.swing.GroupLayout pnlBODYLayout = new javax.swing.GroupLayout(pnlBODY);
        pnlBODY.setLayout(pnlBODYLayout);
        pnlBODYLayout.setHorizontalGroup(
            pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBODYLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBODYLayout.createSequentialGroup()
                        .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlBODYLayout.createSequentialGroup()
                                .addComponent(lblFecha)
                                .addGap(18, 18, 18)
                                .addComponent(dateCita, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lblPaciente)
                            .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDoctor)
                            .addGroup(pnlBODYLayout.createSequentialGroup()
                                .addComponent(lblHora)
                                .addGap(18, 18, 18)
                                .addComponent(cmbHora, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(282, 282, 282))
                    .addGroup(pnlBODYLayout.createSequentialGroup()
                        .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlBODYLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(lblMotivo))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlBODYLayout.setVerticalGroup(
            pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBODYLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateCita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlBODYLayout.createSequentialGroup()
                        .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPaciente)
                            .addComponent(lblDoctor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFecha)
                            .addGroup(pnlBODYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblHora)
                                .addComponent(cmbHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(lblMotivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnGuardar.setText("AGENDAR");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnCancelar.setText("CANCELAR");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        javax.swing.GroupLayout pnlBOTTOMLayout = new javax.swing.GroupLayout(pnlBOTTOM);
        pnlBOTTOM.setLayout(pnlBOTTOMLayout);
        pnlBOTTOMLayout.setHorizontalGroup(
            pnlBOTTOMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBOTTOMLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(btnGuardar)
                .addGap(44, 44, 44)
                .addComponent(btnCancelar)
                .addContainerGap(117, Short.MAX_VALUE))
        );
        pnlBOTTOMLayout.setVerticalGroup(
            pnlBOTTOMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBOTTOMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBOTTOMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlHEADER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBOTTOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBODY, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHEADER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBODY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBOTTOM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPacienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPacienteActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        principal.mostrarMenu();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        int idPaciente = getSelectedId(cmbPaciente);
        int idDoctor = getSelectedId(cmbDoctor);
        Date fecha = getSqlDateFromChooser();
        Time hora = getSqlTimeFromCombo();
        String motivo = txtMotivo.getText().trim();

        if (idPaciente <= 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un paciente válido (o escribe el ID).");
            return;
        }

        if (idDoctor <= 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un doctor válido (o escribe el ID).");
            return;
        }

        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha.");
            return;
        }
        if (hora == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una hora.");
            return;
        }
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El motivo es obligatorio.");
            return;
        }

        if (new dao.PacienteDAO().obtenerPorId(idPaciente) == null) {
            JOptionPane.showMessageDialog(this, "El paciente no existe.");
            return;
        }

        if (new dao.DoctorDAO().obtenerPorId(idDoctor) == null) {
            JOptionPane.showMessageDialog(this, "El doctor no existe.");
            return;
        }

        Cita_MedicaController cc = new Cita_MedicaController();
        boolean ok = cc.insertar(fecha, hora, motivo, idPaciente, idDoctor);

        JOptionPane.showMessageDialog(this, cc.getUltimo_mensaje());

        if (ok) {
            limpiarFormulario();
            // si prefieres regresar al menú al agendar:
            // principal.mostrarMenu();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox cmbDoctor;
    private javax.swing.JComboBox<String> cmbHora;
    private javax.swing.JComboBox cmbPaciente;
    private com.toedter.calendar.JDateChooser dateCita;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDoctor;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblMotivo;
    private javax.swing.JLabel lblPaciente;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBODY;
    private javax.swing.JPanel pnlBOTTOM;
    private javax.swing.JPanel pnlHEADER;
    private javax.swing.JTextArea txtMotivo;
    // End of variables declaration//GEN-END:variables
}
