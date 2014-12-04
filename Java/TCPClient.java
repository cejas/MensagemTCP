import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class TCPClient {

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		BufferedReader InFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("192.168.0.105", 1807); // Socket do servidor
		DataOutputStream outtoServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = InFromUser.readLine();
		outtoServer.writeBytes(sentence + '\n');
		modifiedSentence = inFromServer.readLine();
		System.out.println("Resposta do servidor: " + modifiedSentence);
		clientSocket.close();
	}
}