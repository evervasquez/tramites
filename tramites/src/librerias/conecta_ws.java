package librerias;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;

public class conecta_ws {
	//http://192.168.60.70/aplicacion_horarios/web_service/servidor.php?wsdl
	private String METHOD_NAME;
	private static String NAMESPACE = "http://200.60.9.104/tramite/webservice/";
	//private static String NAMESPACE = "http://10.10.10.2/tramite/webservice/";
	private static String URL = NAMESPACE + "servidor.php?wsdl";
	private static String SOAP_ACTION;

	public Object get_ResultadoWS(Context contex, String[] a, String[] b,
			String c) {

		// []a = nombres de las variables
		// []b = datos de las variables
		//nombre del metodo
		METHOD_NAME = c;
		// String SOAP_ACTION =
		// "http://192.168.1.50/aplicacion_horarios/web_service/servidor.php/"+c;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		// enviamos los parametros
		for (int i = 0; i < a.length; i++) {
			request.addProperty(a[i], b[i]);
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);

		HttpTransportSE transporte = new HttpTransportSE(URL);
		transporte.debug = true;
		SOAP_ACTION = NAMESPACE + "servidor.php/" + METHOD_NAME;
		try {
			transporte.call(SOAP_ACTION, envelope);
			// recuperamos la respuesta en un objeto Object
			SoapObject response = (SoapObject) envelope.bodyIn;
			return response;
		} catch (Exception e) {
			//Toast toast1 = Toast.makeText(contex, "no se pudo conectar",
				//	Toast.LENGTH_SHORT);
			//toast1.show();
			
		}

		/*Runtime basurero = Runtime.getRuntime();
		basurero.freeMemory();*/

		return null;
	}


}