package co.edu.escuelaing.arep;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class ServiceProxy {

	private static int ServidorAUsar = 0;
	
	private static ArrayList<String> servidores = new ArrayList<String>();
	
	public static void main(String[] args) {
		servidores.add("http://ec2-54-146-54-19.compute-1.amazonaws.com");
		servidores.add("http://ec2-54-235-229-173.compute-1.amazonaws.com");
		
		port(getPort());
		get("/:funcion", (req, res) -> {
			String funcion = req.params(":funcion");
			Double value = Double.parseDouble(req.queryParams("value"));
			
			// Se aplica el algoritmo Round-Robin
			RoundRobin();

			// Se forma la URL teniendo en cuenta el servidor
			System.out.println("\nServidor a usar: " + servidores.get(ServidorAUsar));
			URL url = new URL(servidores.get(ServidorAUsar) + ":5001/mathServices/" + funcion + "?value=" + value);
			System.out.println("URL utilizada: " + url);
			
			// Se realiza la consulta al servidor de MathServices
			String response = consultaMathServices(url);
	        System.out.println("Respuesta: " + response);
			
			return response;
		});
	}
	
	private static String consultaMathServices(URL url) {

		StringBuffer response = new StringBuffer();

		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			System.out.println("Código de respuesta: " + connection.getResponseCode());
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));			
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
	
	        in.close();
		} catch (ProtocolException e) {
				e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        return response.toString(); 
	}

	private static void RoundRobin() {
		ServidorAUsar++;

		if (ServidorAUsar == servidores.size()) {
			ServidorAUsar = 0;
		}
	}

	static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 5000;
	}
}
