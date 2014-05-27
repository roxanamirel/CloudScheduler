package main;

import javax.xml.ws.Endpoint;

import userinterface.CloudManagerGui;

import analysis.Analysis;

import database.model.DataCenter;

import monitoring.HookTriggeredWS;
public class WebServiceServer {

	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9898/HookTriggeredWS", new HookTriggeredWS());
        System.out.println("HookTriggeredWS is ready");
        DataCenter dataCenter = new Analysis().recreateModel();
        CloudManagerGui.getInstance().update(dataCenter);
	}
}
