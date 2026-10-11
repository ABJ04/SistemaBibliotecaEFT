package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.CategoriaDAO;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.model.Categoria;
import cl.duoc.biblioteca.model.Libro;
import cl.duoc.biblioteca.util.ValidationUtils;
import cl.duoc.biblioteca.view.LibroPanel;
import cl.duoc.biblioteca.view.ViewMessages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public final class LibroController {
    private final LibroPanel view;
    private final LibroDAO dao;
    private final CategoriaDAO categoriaDAO;
    private int selectedId;

    public LibroController(LibroPanel view, LibroDAO dao, CategoriaDAO categoriaDAO, boolean editable) {
        this.view = view;
        this.dao = dao;
        this.categoriaDAO = categoriaDAO;
        view.getRecargar().addActionListener(e -> refresh());
        view.getTable().getSelectionModel().addListSelectionListener(e -> select());
        if (editable) {
            view.getCrear().addActionListener(e -> create());
            view.getActualizar().addActionListener(e -> update());
            view.getEliminar().addActionListener(e -> delete());
            view.getLimpiar().addActionListener(e -> clear());
        }
        refresh();
    }

    /**
     * Vuelve a consultar los libros en MySQL. Se expone para que otros
     * controladores actualicen inmediatamente la tabla cuando cambia el stock.
     */
    public void refresh() {
        load();
    }

    private void load() {
        try {
            List<Categoria> categories = categoriaDAO.listar();
            view.getCategoria().removeAllItems();
            categories.forEach(view.getCategoria()::addItem);
            DefaultTableModel model = view.getModel();
            model.setRowCount(0);
            for (Libro l : dao.listar()) model.addRow(new Object[]{l.getId(),l.getTitulo(),l.getAutor(),l.getEditorial(),l.getIsbn(),l.getStock(),l.getCategoriaId(),l.getCategoriaNombre()});
        } catch (Exception e) { ViewMessages.error(view, e); }
    }

    private void create() {
        try { dao.crear(readForm(false)); clear(); refresh(); ViewMessages.info(view,"Libro creado."); }
        catch (Exception e) { ViewMessages.error(view,e); }
    }
    private void update() {
        try {
            requireSelection(); Libro l=readForm(true); l.setId(selectedId); dao.actualizar(l);
            clear(); refresh(); ViewMessages.info(view,"Libro actualizado.");
        } catch (Exception e) { ViewMessages.error(view,e); }
    }
    private void delete() {
        try {
            requireSelection();
            if (ViewMessages.confirm(view,"¿Eliminar el libro seleccionado?")) {
                if (!dao.eliminar(selectedId)) throw new IllegalStateException("No se pudo eliminar. Comprueba si tiene un préstamo activo.");
                clear(); refresh();
            }
        } catch (Exception e) { ViewMessages.error(view,e); }
    }
    private Libro readForm(boolean updating) {
        Categoria category=(Categoria)view.getCategoria().getSelectedItem();
        if(category==null) throw new IllegalArgumentException("Debes crear o seleccionar una categoría.");
        Libro l=new Libro();
        l.setTitulo(ValidationUtils.required(view.getTitulo().getText(),"Título"));
        l.setAutor(ValidationUtils.required(view.getAutor().getText(),"Autor"));
        l.setEditorial(ValidationUtils.required(view.getEditorial().getText(),"Editorial"));
        l.setIsbn(ValidationUtils.required(view.getIsbn().getText(),"ISBN"));
        l.setStock(ValidationUtils.positiveInt(view.getStock().getText(),"Stock",true));
        l.setCategoriaId(category.getId());
        return l;
    }
    private void select() {
        int row=view.getTable().getSelectedRow(); if(row<0)return;
        selectedId=(int)view.getModel().getValueAt(row,0);
        view.getTitulo().setText(String.valueOf(view.getModel().getValueAt(row,1)));
        view.getAutor().setText(String.valueOf(view.getModel().getValueAt(row,2)));
        view.getEditorial().setText(String.valueOf(view.getModel().getValueAt(row,3)));
        view.getIsbn().setText(String.valueOf(view.getModel().getValueAt(row,4)));
        view.getStock().setText(String.valueOf(view.getModel().getValueAt(row,5)));
        int categoryId=(int)view.getModel().getValueAt(row,6);
        for(int i=0;i<view.getCategoria().getItemCount();i++) if(view.getCategoria().getItemAt(i).getId()==categoryId) view.getCategoria().setSelectedIndex(i);
    }
    private void clear(){ selectedId=0; view.getTitulo().setText("");view.getAutor().setText("");view.getEditorial().setText("");view.getIsbn().setText("");view.getStock().setText("");view.getTable().clearSelection(); }
    private void requireSelection(){ if(selectedId==0)throw new IllegalArgumentException("Selecciona un libro en la tabla."); }
}
