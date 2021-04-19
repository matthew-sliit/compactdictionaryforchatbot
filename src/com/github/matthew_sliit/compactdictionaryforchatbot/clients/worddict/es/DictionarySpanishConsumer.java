package com.github.matthew_sliit.compactdictionaryforchatbot.clients.worddict.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class DictionarySpanishConsumer implements BundleActivator{
	// Bundle's context.
    private BundleContext m_context = null;
    // The service tacker object.
    private ServiceTracker dictionary_tracker = null;
    
    private Boolean commited = true;
	@Override
	public void start(BundleContext context) throws Exception {
		 m_context = context;
		 // Create a service tracker to monitor dictionary services.
		 WordDictionary wordDictionary = null;
		 BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		 //initialize
		 String value1 = "", dictionaryLang = "EX"; Boolean isGeneric = false, exitcode=false;	
		 int value;
		//track dictionary service
	 	dictionary_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + WordDictionary.class.getName() + ")" +"(language_code=ES))"),null);
		dictionary_tracker.open();
		wordDictionary = (WordDictionary) dictionary_tracker.getService();
		try {
			value1 = wordDictionary.getSimpleName();
			System.out.println("Cliente iniciada para "+value1 + "\nClient started for " + value1);
		}catch (NullPointerException e) {
			System.out.println("¡El servicio de diccionario en español no está disponible!");
			exitcode = true;
		}
		value1 = "";
	     while (true && !exitcode)
         {
	    	try {
	    		System.out.println("=================================================");
				System.out.println("1-Agregar nueva palabra | 2-Agregar sinónimo | 3-buscar palabra | 4-Obtener sinónimos | 5-comprueba si tiene una palabra |"
			+ "| 6-Obtener todas las palabras | 7-Cometer | 8-Quitar palabra | 9-Salida");
				System.out.print("Ingrese el numero: ");
				value = Integer.parseInt(input.readLine());
				System.out.println("=================================================");
				
				if(value == 1) {
					//addNew word
					System.out.print("Palabra : ");
					String newword = input.readLine();
					if(wordDictionary.hasWord(newword)) {
						System.out.println("La palabra ya existe!");
					}else {
						System.out.print("Tipo : ");
						String type = input.readLine();
						System.out.print("Significado : ");
						String meaning = input.readLine();
						System.out.println("-------------------------------------------------");
						System.out.println("¡Añadiendo nueva palabra!");
						showOutput(newword,type,meaning);
						wordDictionary.addNewWord(newword, type, meaning);
					}
				}else if(value == 2) {
					//add synonym
					System.out.print("Palabra : ");
					String word = input.readLine();
					System.out.print("Sinónimo : ");
					String syn = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println("Añadiendo sinónimo de la palabra!");
					wordDictionary.addSynonym(word, syn);					
					
				}else if(value == 3) {
					//get word
					System.out.print("Palabra : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println( wordDictionary.getWordMeaning(word));					
					
				
				}else if(value == 4) {
					//get synonyms						
					System.out.print("Palabra : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println("Sinónimo :" + wordDictionary.getSynonyms(word));
				}
				else if(value == 5){
					System.out.print("Palabra : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println("Resultado :" + wordDictionary.hasWord(word));
					
				}else if(value == 6){
					for(String word : wordDictionary.getAllWords()) {
						System.out.println(word);
					}
				}else if(value == 7) {
					wordDictionary.Commit();//save as preferences
					wordDictionary.selfUpdate();
					//wordDictionary = new EnglishDictionary();//reset
				}else if(value == 8) {
					System.out.print("Palabra : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					if(wordDictionary.hasWord(word)) {
						wordDictionary.removeWord(word);
						System.out.println("¡Palabra eliminada correctamente!");
					}else {
						System.out.println("La palabra no está en el diccionario!");	
					}
				}else {
					exitcode = true;
					//input.close();
					break;
				}
	    	}catch (NumberFormatException | IOException e) {
	    		exitcode = true;
				break;
			}catch (DictionaryException | IndexOutOfBoundsException | NullPointerException e) {
	    		System.out.println("Excepción atrapada:"+wordDictionary.getSimpleName()+" ex="+e.getMessage());
			}
         }
	    
	     System.out.println("Detención de sumador de palabras del diccionario!");
	     //stop(context);
	}
	private static void showOutput(String newword,String type,String meaning) {
		System.out.println("Palabra:" + newword);
		System.out.println("Tipo:" + type);
		System.out.println("Significado:" + meaning);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// auto commit el final
		/*
		if(dictionary_tracker!=null) {
			if(!committed) {
			 	WordDictionary wordDictionary = (WordDictionary) dictionary_tracker.getService();
				wordDictionary.Commit();
			}
		}*/
		System.out.println("Sumador de palabras del diccionario detenido! \n Spanish Dictionary Stopped!");
	}

}
	

