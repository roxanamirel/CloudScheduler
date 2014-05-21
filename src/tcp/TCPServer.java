/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import services.VMService;

import enums.ServiceType;
import factory.CloudManagerFactory;

import logger.CloudLogger;
import models.TemplateModel;

/**
 * 
 * @author oneadmin
 */
public class TCPServer implements Runnable {

	static String clientSentence;
	static String capitalizedSentence;

	public static void main(String... args) {

		listen();
	}

	public static void listen() {

		TemplateModel templateModel = null;
		Socket connectionSocket = null;
		boolean isListening = true;
		ServerSocket welcomeSocket = null;
		try {
			welcomeSocket = new ServerSocket(6789);
		} catch (IOException ex) {
			Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		while (isListening) {

			try {
				CloudLogger.getInstance().LogInfo(
						"Listening on port " + welcomeSocket.getLocalPort());

				connectionSocket = welcomeSocket.accept();
				if (connectionSocket.isConnected()) {
					CloudLogger.getInstance().LogInfo(
							"Accepted a connection from "
									+ connectionSocket.getRemoteSocketAddress()
											.toString());
					InputStream is = connectionSocket.getInputStream();
					ObjectInputStream obj = new ObjectInputStream(is);
					try {
						templateModel = (TemplateModel) obj.readObject();

						VMService vmService = (VMService) CloudManagerFactory
								.getService(ServiceType.VM);
						vmService.allocateICVirtualMachine(templateModel);

						if (!isListening) {
							connectionSocket.close();
						}

					} catch (ClassNotFoundException ex) {
						CloudLogger.getInstance().LogInfo(ex.getMessage());
					}

				}
			} catch (IOException ex) {
				CloudLogger.getInstance().LogInfo(ex.getMessage());
			}
		}

	}

	@Override
	public void run() {
		listen();
	}
}
