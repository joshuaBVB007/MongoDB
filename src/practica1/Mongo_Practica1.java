package practica1;



import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.GeoJsonObjectType;
import com.mongodb.client.result.DeleteResult;

public class Mongo_Practica1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//MongoClient mongoClient = MongoClients.create();
		
		String connectString = "mongodb://localhost:27017";
		MongoClient mongoClient = MongoClients.create(connectString);
		MongoDatabase db=mongoClient.getDatabase("Clase");
		MongoCollection <Document> coll=db.getCollection("Profe");
		
		
		while(true) {
			System.out.println("Introduce una opcion\n"
					+ "1.Listar bases de datos\n"
					+ "2.listar colecciones\n"
					+ "3.(READ) documentos\n"
					+ "4.(INSERT) Porfesor(documento)\n"
					+ "5-(INSERT) profesor con un objeto aula\n"
					+ "6.(UPDATE) un documento\n"
					+ "7.(DELETE) un documento por ID\n"
					+ "8.(DELETE) Borrar todos los profesores\n"
					+ "9-Consultar con una funcion de agregacion\n"
					+ "10-Volcar documentos en un fuchero de texto\n"
					+ "11-salir");
			Scanner e=new Scanner(System.in);
			int decision=e.nextInt();
			if(decision==1) {//VALIDADO
				System.out.println("---------------------------------");
				List<Document> databases=mongoClient.listDatabases().into(new ArrayList<>());
				databases.forEach(dbs -> System.out.println(dbs.toJson()));
				MongoCursor <String> dbsCursor=mongoClient.listDatabaseNames().iterator();
				while (dbsCursor.hasNext()) {
					System.out.println(dbsCursor.next());		
				}
				System.out.println("---------------------------------");
			}else if (decision==2) {//VALIDADO
				System.out.println("---------------------------------");
				db=mongoClient.getDatabase("Clase");
				for (String name:db.listCollectionNames()) {
					System.out.println(name);
				}
				System.out.println("---------------------------------");
				System.out.println("Query OK");
			}else if (decision==3) {//VALIDADO
				System.out.println("---------------------------------");
				MongoCollection <Document> coleccion=db.getCollection("Profe");
				List<Document> consulta=coleccion.find().into(new ArrayList<Document>());
				for (int i = 0; i < consulta.size(); i++) {
					System.out.println("-"+consulta.get(i).toString());
				}
				System.out.println("---------------------------------");
				System.out.println("Query OK");
			}else if (decision==4) {//VALIDADO
				System.out.println("---------------------------------");
				Document doc=new Document();
				
				System.out.println("Nombre?");
				String nombre=e.next();
				System.out.println("Edad?");
				int edad=e.nextInt();
				
				doc.put("nombre",nombre);
				doc.put("edad", edad);
				coll.insertOne(doc);
				System.out.println("---------------------------------");
				System.out.println("Query OK");			
			}else if (decision==5) {
				System.out.println("---------------------------------");
				Document main=new Document();	
				System.out.println("Nombre?");
				String nom=e.next();
				System.out.println("ID?");
				int id=e.nextInt();
				main.put("id", id);
				main.put("nombre", nom);
				JsonObject clase=new JsonObject();
				clase.put("aula","el sotano");
				clase.put("puerta","la del final");
				main.put("clase", clase);
				coll.insertOne(main);
				System.out.println("---------------------------------");
			}else if (decision==6) {//VALIDADO
				System.out.println("---------------------------------");
				System.out.println("Nombre a cambiar?");
				String nombre=e.next();	
				System.out.println("Nuevo nombre?");
				String nombre2=e.next();
				BasicDBObject query = new BasicDBObject();
				query.put("nombre", nombre); // (1)
				BasicDBObject newDocument = new BasicDBObject();
				newDocument.put("nombre", nombre2); // (2)
				BasicDBObject updateObject = new BasicDBObject();
				updateObject.put("$set", newDocument); // (3)
				db.getCollection("Profe").updateOne(query, updateObject);
				System.out.println("---------------------------------");
				/*Nota:modificará el primero que se encuentre
				https://kb.objectrocket.com/mongo-db/how-to-update-a-document-in-mongodb-using-java-384*/
			}else if(decision==7) {
				System.out.println("---------------------------------");
				System.out.println("Introduce Nombre a eliminar?");
				String nombre=e.next();
				BasicDBObject theQuery = new BasicDBObject();
				theQuery.put("nombre", nombre);
				DeleteResult result = coll.deleteMany(theQuery);
				System.out.println("The Numbers of Deleted Document(s) : " + result.getDeletedCount());
				System.out.println("---------------------------------");
			}else if (decision==8) {
				System.out.println("---------------------------------");
				BasicDBObject document=new BasicDBObject();
				coll.deleteMany(document);
				System.out.println("---------------------------------");
			}else if (decision==9) {
				System.out.println("---------------------------------");
				BasicDBObject document=new BasicDBObject();
				System.out.println("Total de documentos insertados: "+coll.countDocuments(document));
				System.out.println("---------------------------------");
			}else if (decision==10) {
				System.out.println("---------------------------------");
				MongoCollection <Document> coleccion=db.getCollection("Profe");
				List<Document> consulta=coleccion.find().into(new ArrayList<Document>());
				for (int i = 0; i < consulta.size(); i++) {
					System.out.println("-"+consulta.get(i).toString());
				}
				try {
					PrintWriter fichero=new PrintWriter(new File("jonathan.txt"));
					for (int i = 0; i < consulta.size(); i++) {
						fichero.println(consulta.get(i).toString());
					}
					fichero.close();
				} catch (Exception e2) {
					System.out.println("Ha fallado al escribir en fichero");
				}	
				System.out.println("---------------------------------");
			}else if (decision==11) {
				mongoClient.close();
				break;	
			}
			
			
		}

		
	}
}
