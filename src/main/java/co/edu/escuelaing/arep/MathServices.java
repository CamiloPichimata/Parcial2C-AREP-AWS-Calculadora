package co.edu.escuelaing.arep;

import static spark.Spark.get;
import static spark.Spark.port;


public class MathServices {
	
	public static void main(String[] args) {
		port(getPort());
		get("/mathServices/:funcion", (req, res) -> {
			String funcion = req.params(":funcion"); 
			Double value = Double.parseDouble(req.queryParams("value"));
			System.out.println("Operacion: " + funcion + "(" + value + ")");
			Double rta = null;
			if (funcion.equals("acos")) {
				rta = Math.acos(value);
			} else if (funcion.equals("asin")) {
				rta = Math.asin(value);
			} else {
				System.out.println("La función recibida no es válida");
				return "{\"res\": \"Funcion no valida\"}";
			}
			String response = "{\"res\": \"" + rta + "\"}";
			System.out.println(" - Respuesta: " + response);
			return response;
		});
	}
	
	static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 5001;
	}
}
