package com.classrecorder.teacherserver.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import com.classrecorder.teacherserver.server.websockets.record.WebSocketRecordHandler;

@RestController
public class RecordStateController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	WebSocketRecordHandler wsRecordHandler;
	
	@RequestMapping("/api/recording/currentState")
	public ResponseEntity<?> currentRecordStatus() {
		if(wsRecordHandler.isRecording()) {
			return new ResponseEntity<>("Recording", HttpStatus.OK);
		}
		if(wsRecordHandler.isPaused()) {
			return new ResponseEntity<>("Paused", HttpStatus.OK);
		}
		if(wsRecordHandler.isStopped()) {
			return new ResponseEntity<>("Stopped", HttpStatus.OK);
		}
		return new ResponseEntity<>("Illegal Recording status", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping("/api/getRecordTime")
    public ResponseEntity<?> getRecordTime() {
	    if(wsRecordHandler.isRecording() || wsRecordHandler.isPaused()) {
	        return new ResponseEntity<>(wsRecordHandler.getRecordTime(), HttpStatus.OK);
        }
	    return new ResponseEntity<>("", HttpStatus.OK);
    }

    @RequestMapping("/api/getLocalIp")
    public ResponseEntity<?> getLocalIp() throws SocketException {

        List<String> addresses = new ArrayList<>();
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements();)
        {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();)
            {
                InetAddress addr = a.nextElement();
                if(addr.getHostAddress().startsWith("192.168")) {
                    addresses.add(addr.getHostAddress());
                }
            }
        }
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @RequestMapping("/api/getAllNetworkInterfaces")
    public ResponseEntity<?> getAllNetworkInterfaces() throws Exception {
        List<String> addresses = new ArrayList<>();
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements();)
        {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();)
            {
                InetAddress addr = a.nextElement();
                addresses.add(addr.getHostAddress());
                
            }
        }
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }
    
    @RequestMapping("/api/testConnection")
    public ResponseEntity<?> methodName(HttpServletResponse response) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie","key="+"value");
        ResponseEntity.status(HttpStatus.OK).headers(headers).build();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
	
}
