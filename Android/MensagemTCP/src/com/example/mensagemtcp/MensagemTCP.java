package com.example.mensagemtcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MensagemTCP extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mensagem_tcp);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mensagem_tc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		String message = "";
		String messagevolta = "";

		class Dados {
			String endereco;
			int porta;
			String message;

			public Dados(final String end, final int por, final String mes) {
				this.endereco = end;
				this.porta = por;
				this.message = mes;
			}
		}

		TextView resposta;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_mensagem_tc, container, false);

			Button botao = (Button) rootView.findViewById(R.id.button1);
			final EditText endereco = (EditText) rootView.findViewById(R.id.editText1);
			final EditText porta = (EditText) rootView.findViewById(R.id.EditText01);
			final EditText msg = (EditText) rootView.findViewById(R.id.editText2);
			resposta = (TextView) rootView.findViewById(R.id.TextView02);

			botao.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {

					Dados dado = new Dados(endereco.getText().toString(), Integer.parseInt(porta.getText().toString()),
							msg.getText().toString());
					new Connection().execute(dado);

				}
			});

			return rootView;
		}

		public class Connection extends AsyncTask<Dados, Integer, String> {

			@Override
			protected String doInBackground(Dados... dado) {

				String sentence;

				Socket clientSocket = new Socket();
				
				try {
				
					// clientSocket = new Socket(dado[0].endereco, dado[0].porta);
					clientSocket.connect(new InetSocketAddress(dado[0].endereco, dado[0].porta), 2000); // Timeout = 2000ms
					DataOutputStream outtoServer = new DataOutputStream(clientSocket.getOutputStream());
					BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
							clientSocket.getInputStream()));
					sentence = dado[0].message; // InFromUser.readLine();
					outtoServer.writeBytes(sentence + '\n');
					messagevolta = inFromServer.readLine();
					clientSocket.close();
					
				} catch (NumberFormatException e) {
				
					messagevolta = "Falha!";
					e.printStackTrace();
					
				} catch (SocketTimeoutException e) {
				
					messagevolta = "Timeout!";
					e.printStackTrace();
					
				} catch (IOException e) {
				
					messagevolta = "Falha!";
					e.printStackTrace();
					
				}

				return messagevolta;
			}

			@Override
			protected void onPreExecute() {

				resposta.setText("");

			}

			protected void onPostExecute(String mess) {

				resposta.setText(mess);

			}

		}

	}
}
