package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.CategoriaDAO;
import cl.duoc.biblioteca.model.Categoria;
import cl.duoc.biblioteca.util.ValidationUtils;
import cl.duoc.biblioteca.view.CategoriaPanel;
import cl.duoc.biblioteca.view.ViewMessages;

public final class CategoriaController {
    private final CategoriaPanel view;
    private final CategoriaDAO dao;
    private int selectedId;

    public CategoriaController(CategoriaPanel view, CategoriaDAO dao) {
        this.view=view; this.dao=dao;
        view.getCrear().addActionListener(e->create()); view.getActualizar().addActionListener(e->update());
        view.getEliminar().addActionListener(e->delete()); view.getLimpiar().addActionListener(e->clear());
        view.getTable().getSelectionModel().addListSelectionListener(e->select()); load();
    }
    private void load(){ try{view.getModel().setRowCount(0);for(Categoria c:dao.listar())view.getModel().addRow(new Object[]{c.getId(),c.getNombre(),c.getDescripcion()});}catch(Exception e){ViewMessages.error(view,e);} }
    private Categoria read(){return new Categoria(selectedId,ValidationUtils.required(view.getNombre().getText(),"Nombre"),view.getDescripcion().getText().trim());}
    private void create(){try{dao.crear(read());clear();load();ViewMessages.info(view,"Categoría creada.");}catch(Exception e){ViewMessages.error(view,e);}}
    private void update(){try{required();dao.actualizar(read());clear();load();ViewMessages.info(view,"Categoría actualizada.");}catch(Exception e){ViewMessages.error(view,e);}}
    private void delete(){try{required();if(ViewMessages.confirm(view,"¿Eliminar la categoría seleccionada?")){dao.eliminar(selectedId);clear();load();}}catch(Exception e){ViewMessages.error(view,e);}}
    private void select(){int r=view.getTable().getSelectedRow();if(r<0)return;selectedId=(int)view.getModel().getValueAt(r,0);view.getNombre().setText(String.valueOf(view.getModel().getValueAt(r,1)));view.getDescripcion().setText(String.valueOf(view.getModel().getValueAt(r,2)));}
    private void clear(){selectedId=0;view.getNombre().setText("");view.getDescripcion().setText("");view.getTable().clearSelection();}
    private void required(){if(selectedId==0)throw new IllegalArgumentException("Selecciona una categoría.");}
}
