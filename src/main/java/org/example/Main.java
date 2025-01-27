package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(proxyBeanMethods = false)
public class Main {

    public static void main(String[] args) {

        // Lista de Futbolistas
        List<Futbolista> futbolistas = new ArrayList<>();

        futbolistas.add(new Futbolista("Iker", "Casillas", 33, Arrays.asList("Portero"), true));
        futbolistas.add(new Futbolista("Carles", "Puyol", 36, Arrays.asList("Central", "Lateral"), true));
        futbolistas.add(new Futbolista("Sergio", "Ramos", 28, Arrays.asList("Lateral", "Central"), true));
        futbolistas.add(new Futbolista("Andrés", "Iniesta", 30, Arrays.asList("Centrocampista", "Delantero"), true));
        futbolistas.add(new Futbolista("Fernando", "Torres", 30, Arrays.asList("Delantero"), true));
        futbolistas.add(new Futbolista("Leo", "Baptistao", 22, Arrays.asList("Delantero"), false));

        // Conexión al servidor de MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb://admin:admin123@localhost:27017")) {

            // Conexión a la base de datos "Futbol"
            MongoDatabase database = mongoClient.getDatabase("Futbol");

            // Obtenemos la colección "Futbolistas"
            MongoCollection<Document> collection = database.getCollection("Futbolistas");

            // Limpieza previa de la colección
            collection.drop();

            // PASO 4.1: "CREATE" -> Insertamos los documentos en la colección
            for (Futbolista fut : futbolistas) {
                collection.insertOne(fut.toDocument());
            }

            // PASO 4.2.1: "READ" -> Contamos e imprimimos todos los documentos
            long numDocumentos = collection.countDocuments();
            System.out.println("Número de documentos en la colección Futbolistas: " + numDocumentos + "\n");

            // Imprimimos todos los documentos
            System.out.println("Documentos en la colección:");
            collection.find().forEach(doc -> System.out.println(doc.toJson()));

            // PASO 4.2.2: "READ" -> Buscar jugadores que jueguen en la posición de "Delantero"
            System.out.println("\nFutbolistas que juegan en la posición de Delantero:");
            collection.find(Filters.regex("demarcacion", "Delantero"))
                    .forEach(doc -> {
                        Futbolista futbolista = new Futbolista(doc);
                        System.out.println(futbolista.toString());
                    });

            // PASO 4.3: "UPDATE" -> Incrementar edad en 100 años para jugadores mayores de 30 años
            collection.updateMany(Filters.gt("edad", 30), Updates.inc("edad", 100));

            // PASO 4.4: "DELETE" -> Borrar jugadores internacionales (internacional = true)
            collection.deleteMany(Filters.eq("internacional", true));

            // Verificamos el estado final de la colección
            System.out.println("\nEstado final de la colección:");
            collection.find().forEach(doc -> System.out.println(doc.toJson()));

        } catch (Exception ex) {
            System.out.println("Exception al conectar al servidor de MongoDB: " + ex.getMessage());
        }
    }
}
