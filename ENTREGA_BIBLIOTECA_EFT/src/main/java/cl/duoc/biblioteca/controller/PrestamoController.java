package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.PrestamoDAO;
import cl.duoc.biblioteca.model.Prestamo;
import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.service.PrestamoService;
import cl.duoc.biblioteca.util.ValidationUtils;
import cl.duoc.biblioteca.view.PrestamoPanel;
import cl.duoc.biblioteca.view.ViewMessages;

import javax.swing.*;
import java.time.LocalDate;

public final class PrestamoController {
    private final PrestamoPanel view;
    private final Usuario usuario;
    private final PrestamoDAO dao;
    private final PrestamoService service;
    private final Runnable onStockChanged;

    public PrestamoController(PrestamoPanel view, Usuario usuario, PrestamoDAO dao, PrestamoService service){
        this(view, usuario, dao, service, () -> { });
    }

    public PrestamoController(PrestamoPanel view, Usuario usuario, PrestamoDAO dao,
                              PrestamoService service, Runnable onStockChanged){
        this.view=view;this.usuario=usuario;this.dao=dao;this.service=service;
        this.onStockChanged=onStockChanged;
        view.getPrestar().addActionListener(e->lend());view.getDevolver().addActionListener(e->returnBook());view.getRecargar().addActionListener(e->load());load();
    }
    private void load(){try{view.getModel().setRowCount(0);Integer scope=usuario.getEstudianteId();for(Prestamo p:dao.listarActivos(scope)){String state=p.estaAtrasado(LocalDate.now())?"ATRASADO":p.getEstado().name();view.getModel().addRow(new Object[]{p.getId(),p.getEstudianteId(),p.getEstudianteNombre(),p.getLibroId(),p.getLibroTitulo(),p.getFechaPrestamo(),p.getFechaVencimiento(),state});}}catch(Exception e){ViewMessages.error(view,e);}}
    private void lend(){
        final int studentId;final int bookId;
        try{studentId=ValidationUtils.positiveInt(view.getEstudianteId().getText(),"ID estudiante",false);bookId=ValidationUtils.positiveInt(view.getLibroId().getText(),"ID libro",false);if(!usuario.puedeOperarPrestamoDe(studentId))throw new SecurityException("No puedes registrar préstamos para otro estudiante.");}catch(Exception e){ViewMessages.error(view,e);return;}
        view.setBusy(true);
        new SwingWorker<Prestamo,Void>(){
            @Override protected Prestamo doInBackground() throws Exception{return service.registrarPrestamo(studentId,bookId);}
            @Override protected void done(){view.setBusy(false);try{Prestamo p=get();onStockChanged.run();view.setStatus("Préstamo #"+p.getId()+" registrado. Stock actualizado. Vence: "+p.getFechaVencimiento());load();}catch(Exception e){ViewMessages.error(view,e);}}
        }.execute();
    }
    private void returnBook(){
        int row=view.getTable().getSelectedRow();if(row<0){ViewMessages.error(view,new IllegalArgumentException("Selecciona un préstamo activo."));return;}
        int loanId=(int)view.getModel().getValueAt(row,0);int studentId=(int)view.getModel().getValueAt(row,1);
        if(!usuario.puedeOperarPrestamoDe(studentId)){ViewMessages.error(view,new SecurityException("No puedes devolver préstamos de otro estudiante."));return;}
        view.setBusy(true);
        new SwingWorker<Boolean,Void>(){
            @Override protected Boolean doInBackground() throws Exception{return service.devolverPrestamo(loanId);}
            @Override protected void done(){view.setBusy(false);try{boolean late=get();onStockChanged.run();view.setStatus(late?"Devolución registrada con atraso. Stock repuesto.":"Devolución registrada a tiempo. Stock repuesto.");load();}catch(Exception e){ViewMessages.error(view,e);}}
        }.execute();
    }
}
