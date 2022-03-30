package co.edu.escuelaing.arep;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ServiceProxy {

	public static void main(String[] args) {
		port(getPort());
		get("/:funcion", (req, res) -> {
			String funcion = req.params(":funcion");
			Double value = Double.parseDouble(req.queryParams("value"));
			
			URL url = new URL("http://localhost:5001/mathServices/" + funcion + "?value=" + value);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			System.out.println("Código de respuesta: " + connection.getResponseCode());
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
	        in.close();
	        System.out.println("Respuesta: " + response.toString());
			
			return response.toString();
		});
	}
	
	static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 5000;
	}
}
