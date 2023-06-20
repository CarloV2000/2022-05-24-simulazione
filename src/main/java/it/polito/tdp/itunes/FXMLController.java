/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.CoppiaA;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<String> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<String> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {
    	String trackNAME = this.cmbCanzone.getValue();
    	String maxBytes = this.txtMemoria.getText();
    	Integer nMaxBytes;
    	String s = "Playlist creata: \n";
    	
    	if(trackNAME == null) {
			this.txtResult.setText("Selezionare una Canzone nella box Canzoni!");
			return;
		}
    	try {
    		nMaxBytes = Integer.parseInt(maxBytes);
    		if(maxBytes == null) {
    			this.txtResult.setText("Inserire un valore nel campo MaxBytes!");
    			return;
    		}
    		model.calcolaPlaylist(trackNAME, nMaxBytes);
    		List<Track>r = new ArrayList<>(model.getPlaylistMigliore());
    		for(Track x : r) {
    			s += "\n" + x.getName();
    		}
    		this.txtResult.setText(s);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero nel campo MaxBytes!");
    		return;
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String genreNAME = this.cmbGenere.getValue();
    	if(genreNAME == null) {
    		this.txtResult.setText("Inserire un genere nella box genere");
    		return;
    	}
    	String res = model.creaGrafo(genreNAME);
    	this.txtResult.setText(res);
    	//popolo la lista di canzoni con i vertici del grafo appena creato
    	for(Track x: model.getGrafo().vertexSet()) {
    		this.cmbCanzone.getItems().add(x.getName());
    	}
    	
    }

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	String genreName = this.cmbGenere.getValue();
    	if(genreName == null) {
    		this.txtResult.setText("Inserire un genere nella box genere");
    		return;
    	}
    	CoppiaA c = model.trovaArcoPiuPesante(genreName);
    	this.txtResult.appendText("\nArco piu pesante : "+c.getT1()+"<--->"+c.getT2()+" ("+c.getPeso()+") ");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(Genre x : model.getNameGenresMap().values()) {
    		this.cmbGenere.getItems().add(x.getName());
    	}
    }

}
