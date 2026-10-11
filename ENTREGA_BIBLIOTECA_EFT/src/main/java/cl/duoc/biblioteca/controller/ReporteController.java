package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.ReporteDAO;
import cl.duoc.biblioteca.util.ValidationUtils;
import cl.duoc.biblioteca.view.LibroPanel;
import cl.duoc.biblioteca.view.ReportePanel;
import cl.duoc.biblioteca.view.ViewMessages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public final class ReporteController {
    private final ReportePanel view; private final ReporteDAO dao;
    public ReporteController(ReportePanel view,ReporteDAO dao){this.view=view;this.dao=dao;view.getMasPrestados().addActionListener(e->run("Más prestados",new String[]{"Libro ID","Título","Autor","Total"},()->dao.librosMasPrestados()));view.getActuales().addActionListener(e->run("Actuales",new String[]{"Préstamo","Libro","Estudiante","Inicio","Vencimiento","Situación"},()->dao.librosActualmentePrestados()));view.getHistorial().addActionListener(e->history());}
    private void history(){try{int id=ValidationUtils.positiveInt(view.getEstudianteId().getText(),"ID estudiante",false);run("Historial",new String[]{"Préstamo","Libro","Inicio","Vencimiento","Devolución","Estado"},()->dao.historialPorEstudiante(id));}catch(Exception e){ViewMessages.error(view,e);}}
    private void run(String name,String[] columns,SqlRows supplier){
        new SwingWorker<List<Object[]>,Void>(){
            @Override protected List<Object[]> doInBackground() throws Exception{return supplier.get();}
            @Override protected void done(){try{DefaultTableModel model=LibroPanel.readonlyModel(columns);for(Object[] row:get())model.addRow(row);view.getTable().setModel(model);}catch(Exception e){ViewMessages.error(view,e);}}
        }.execute();
    }
    @FunctionalInterface private interface SqlRows{List<Object[]> get() throws Exception;}
}
