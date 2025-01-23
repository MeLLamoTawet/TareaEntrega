package org.example;

import com.mongodb.client.MongoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication(proxyBeanMethods = false)
public class Main {

    public static void main(String args[]) {

        ArrayList<Futbolista> futbolistas = new ArrayList<Futbolista>();

        futbolistas.add(new Futbolista("Iker", "Casillas", 33, new ArrayList<String>(Arrays.asList("Portero")), true));
        futbolistas.add(new Futbolista("Carles", "Puyol", 36, new ArrayList<String>(Arrays.asList("Central", "Lateral")), true));
        futbolistas.add(new Futbolista("Sergio", "Ramos", 28, new ArrayList<String>(Arrays.asList("Lateral", "Central")), true));
        futbolistas.add(new Futbolista("Andrés", "Iniesta", 30, new ArrayList<String>(Arrays.asList("Centrocampista", "Delantero")), true));
        futbolistas.add(new Futbolista("Fernando", "Torres", 30, new ArrayList<String>(Arrays.asList("Delantero")), true));
        futbolistas.add(new Futbolista("Leo", " Baptistao", 22, new ArrayList<String>(Arrays.asList("Delantero")), false));

        try {
            // PASO 1: Conexión al Server de MongoDB Pasandole el host y el puerto
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // PASO 2: Conexión a la base de datos
            DB db = mongoClient.getDB("Futbol");

            // PASO 3: Obtenemos una coleccion para trabajar con ella
            DBCollection collection = db.getCollection("Futbolistas");

            // PASO 4: CRUD (Create-Read-Update-Delete)

            // PASO 4.1: "CREATE" -> Metemos los objetos futbolistas (o documentos en Mongo) en la coleccion Futbolista
            for (Futbolista fut : futbolistas) {
                collection.insert(fut.toDBObjectFutbolista());
            }

            // PASO 4.2.1: "READ" -> Leemos todos los documentos de la base de datos
            int numDocumentos = (int) collection.getCount();
            System.out.println("Número de documentos en la colección Futbolistas: " + numDocumentos + "\n");

            // Busco todos los documentos de la colección y los imprimo
            DBCursor cursor = collection.find();
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toString());
                }
            } finally {
                cursor.close();
            }

            // PASO 4.2.2: "READ" -> Hacemos una Query con condiciones (Buscar Futbolistas que sean delanteros) y lo pasamos a un objeto Java
            System.out.println("\nFutbolistas que juegan en la posición de Delantero");
            DBObject query = new BasicDBObject("demarcacion", new BasicDBObject("$regex", "Delantero"));
            cursor = collection.find(query);
            try {
                while (cursor.hasNext()) {
                    Futbolista futbolista = new Futbolista((BasicDBObject) cursor.next());
                    System.out.println(futbolista.toString());
                }
            } finally {
                cursor.close();
            }

            // PASO 4.3: "UPDATE" -> Actualizamos la edad de los jugadores. Sumamos 100 años a los jugadores que tengan mas de 30 años
            DBObject find = new BasicDBObject("edad", new BasicDBObject("$gt", 30));
            DBObject updated = new BasicDBObject().append("$inc", new BasicDBObject().append("edad", 100));
            collection.update(find, updated, false, true);

            // PASO 4.4: "DELETE" -> Borramos todos los futbolistas que sean internacionales (internacional = true)
            DBObject findDoc = new BasicDBObject("internacional", true);
            collection.remove(findDoc);

            // PASO FINAL: Cerrar la conexion
            mongoClient.close();

        } catch (UnknownHostException ex) {
            System.out.println("Exception al conectar al server de Mongo: " + ex.getMessage());
        }

    }

}
