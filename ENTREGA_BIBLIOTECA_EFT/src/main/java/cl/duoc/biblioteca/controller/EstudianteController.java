package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.util.ValidationUtils;
import cl.duoc.biblioteca.view.EstudiantePanel;
import cl.duoc.biblioteca.view.ViewMessages;

public final class EstudianteController {
    private final EstudiantePanel view;
    private final EstudianteDAO dao;
    private int selectedId;

    public EstudianteController(EstudiantePanel view, EstudianteDAO dao){
        this.view=view;this.dao=dao;
        view.getCrear().addActionListener(e->create());view.getActualizar().addActionListener(e->update());
        view.getEliminar().addActionListener(e->delete());view.getLimpiar().addActionListener(e->clear());
        view.getTable().getSelectionModel().addListSelectionListener(e->select());load();
    }
    private void load(){try{view.getModel().setRowCount(0);for(Estudiante s:dao.listar())view.getModel().addRow(new Object[]{s.getEstudianteIdValor(),s.getRut(),s.getNombres(),s.getApellidos(),s.getEmail(),s.getCurso(),s.getUsername()});}catch(Exception e){ViewMessages.error(view,e);}}
    private Estudiante read(boolean create){
        Estudiante s=new Estudiante();s.setEstudianteId(selectedId);
        s.setRut(ValidationUtils.required(view.getRut().getText(),"RUT"));s.setNombres(ValidationUtils.required(view.getNombres().getText(),"Nombres"));
        s.setApellidos(ValidationUtils.required(view.getApellidos().getText(),"Apellidos"));s.setEmail(ValidationUtils.required(view.getEmail().getText(),"Email"));
        s.setCurso(ValidationUtils.required(view.getCurso().getText(),"Curso"));s.setUsername(ValidationUtils.required(view.getUsername().getText(),"Usuario"));
        String pass=new String(view.getPassword().getPassword());if(create&&pass.isBlank())throw new IllegalArgumentException("La contraseña es obligatoria al crear.");s.setPasswordTemporal(pass);return s;
    }
    private void create(){try{dao.crear(read(true));clear();load();ViewMessages.info(view,"Estudiante y acceso creados.");}catch(Exception e){ViewMessages.error(view,e);}}
    private void update(){try{required();dao.actualizar(read(false));clear();load();ViewMessages.info(view,"Estudiante actualizado.");}catch(Exception e){ViewMessages.error(view,e);}}
    private void delete(){try{required();if(ViewMessages.confirm(view,"¿Desactivar el estudiante y su acceso?")){dao.eliminar(selectedId);clear();load();}}catch(Exception e){ViewMessages.error(view,e);}}
    private void select(){int r=view.getTable().getSelectedRow();if(r<0)return;selectedId=(int)view.getModel().getValueAt(r,0);view.getRut().setText(String.valueOf(view.getModel().getValueAt(r,1)));view.getNombres().setText(String.valueOf(view.getModel().getValueAt(r,2)));view.getApellidos().setText(String.valueOf(view.getModel().getValueAt(r,3)));view.getEmail().setText(String.valueOf(view.getModel().getValueAt(r,4)));view.getCurso().setText(String.valueOf(view.getModel().getValueAt(r,5)));view.getUsername().setText(String.valueOf(view.getModel().getValueAt(r,6)));view.getPassword().setText("");}
    private void clear(){selectedId=0;view.getRut().setText("");view.getNombres().setText("");view.getApellidos().setText("");view.getEmail().setText("");view.getCurso().setText("");view.getUsername().setText("");view.getPassword().setText("");view.getTable().clearSelection();}
    private void required(){if(selectedId==0)throw new IllegalArgumentException("Selecciona un estudiante.");}
}
