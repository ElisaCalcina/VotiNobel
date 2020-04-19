package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List<Esame> esami;
	
	private double bestMedia=0.0;
	private Set<Esame> bestSoluzione= null;
	
	public Model() {
		EsameDAO dao= new EsameDAO();
		this.esami=dao.getTuttiEsami();
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		//prepariamo le strutture dati utili per la ricorsiva
		Set<Esame> parziale= new HashSet<Esame>();
		cerca1(parziale, 0, numeroCrediti);
		
		return bestSoluzione;
	}
//APPROCCIO 1
	//la complessità è : 2^N
	private void cerca1(Set<Esame> parziale, int L, int m){
		//casi di terminazione
		
		int crediti= sommaCrediti(parziale);
			if(crediti>m) {
				return; //mi fermo subito
			}
			if(crediti==m) {
				double media= calcolaMedia(parziale);
				if(media>bestMedia) { //maggiore delle medie che ho incontrato fino ad adesso
					bestSoluzione= new HashSet<>(parziale);
					bestMedia= media;
				}
			}
			
			//se arriviamo qui, crediti<m
			if(L==esami.size()) {
				return;
			}
			
		//generiamo i sottoproblemi
		//esami[L] è da aggiungere o no? provo entrambe le cose
		
		//provo ad aggiungerlo
		parziale.add(esami.get(L));
		cerca1(parziale, L+1, m);
		parziale.remove(esami.get(L));
		
		//provo a non aggiungerlo
		cerca1(parziale, L+1, m);

		
	}
//APPROCCIO 2	
	//La complessità è: N!
	private void cerca2(Set<Esame> parziale, int L, int m){
		//casi di terminazione
		
		int crediti= sommaCrediti(parziale);
			if(crediti>m) {
				return; //mi fermo subito
			}
			if(crediti==m) {
				double media= calcolaMedia(parziale);
				if(media>bestMedia) { //maggiore delle medie che ho incontrato fino ad adesso
					bestSoluzione= new HashSet<>(parziale);
					bestMedia= media;
				}
			}
			
			//se arriviamo qui, crediti<m
			if(L==esami.size()) {
				return;
			}
			
		//generiamo i sottoproblemi
			for(Esame e: esami) {
				//devo controllare che l'esame non sia già contenuto in parziale
				if(!parziale.contains(e)) {
					parziale.add(e);
					cerca2(parziale, L+1, m);
					parziale.remove(e);
				}
			}
	}


	public double calcolaMedia(Set<Esame> parziale) {
		int crediti=0;
		int somma=0;
		
		for(Esame e:parziale) {
			crediti+=e.getCrediti();
			somma+= e.getVoto()*e.getCrediti();
		}
		
		return somma/crediti;
	}


	private int sommaCrediti(Set<Esame> parziale) {
		int somma=0;
		
		for(Esame e: parziale) {
			somma+=e.getCrediti();
		}
		
		return somma;
	}
}
