import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class SocketToSerialMediator {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.err
					.println("Error: No arguments provided. Example: 'java SocketToSerialMediator COM1 1234'");
			System.exit(0);
		} else if(args.length < 2) {
			System.err
			.println("Error: No socket port provided.");
			if(args.length < 1) {
				System.err
				.println("Error: No serial port provided.");
			}
			System.err.println("Example: 'java SocketToSerialMediator COM1 1234'");
			System.exit(0);
		} else {
			String serialPortName = args[0];
			int socketPort = Integer.valueOf(args[1]);
			ServerSocket ss = null;
			Socket s = null;
			InputStream in = null;
			DataInputStream din = null;
			OutputStream out = null;
			try {
				CommPortIdentifier portIdentifier = CommPortIdentifier
						.getPortIdentifier(serialPortName);
				if (portIdentifier.isCurrentlyOwned()) {
					System.err.println("Error: Serial port is currently in use");
				} else {
					CommPort commPort = portIdentifier.open("", 2000);
					if (commPort instanceof SerialPort) {
						SerialPort serialPort = (SerialPort) commPort;
						serialPort.setSerialPortParams(9600,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
						out = serialPort.getOutputStream();
					} else {
						System.err
								.println("Error: Only serial ports are handled.");
					}
				}
				ss = new ServerSocket(socketPort);
				System.out.println("Waiting for socket connection on port: " + serialPortName + ".");
				s = ss.accept();
				System.out.println("Connection opened.");
				InputStream socketIn = s.getInputStream();
				din = new DataInputStream(socketIn);
				Byte b = null;
				byte[] outByteArray = new byte[1];
				while ((b = din.readByte()) != null) {
					System.out.println("Received Byte: " + b.toString());
					outByteArray[0] = b;
					if (out != null) {
						out.write(outByteArray);
						out.flush();
						System.out.println("Transmitted ByteArray: " + Arrays.toString(outByteArray));
					}
				}
			} catch (EOFException e) {
			} catch (IOException e) {
				System.err.println(e);
			} catch (NoSuchPortException e) {
				System.err.println(e);
			} catch (PortInUseException e) {
				System.err.println(e);
			} catch (UnsupportedCommOperationException e) {
				System.err.println(e);
			} finally {
				if(din != null) {
					try {
						din.close();
					} catch (IOException e) {
					}
				}
				if(in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
				if(s != null) {
					try {
						s.close();
					} catch (IOException e) {
					}
				}
				if(ss != null) {
					try {
						ss.close();
					} catch (IOException e) {
					}
				}
				if(out != null) {
					try {
						out.close();
					} catch (IOException e) {
					}
				}
				System.out.println("Connections have been closed. Program is shutting down.");
				System.exit(0);
			}
		}
	}
}
