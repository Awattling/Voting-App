/* Author: Austin Wattling
 * Date: August 24, 2018
 */

package client;
import javax.swing.SwingUtilities;

/* Main Runner for voting client.*/
public class Client {
	static Views window;
	static SocketHandler handle; 
	
	
	public static void main(String[] args) {
		/* Opens a separate main thread for the UI found in the class view*/
		window = new Views();
		SwingUtilities.invokeLater(window);
		
		// Starts the main event loop// 
		handle = new SocketHandler();
		handle.setWindow(window);
		window.setSocketHandler(handle);
		handle.setup();
	}
}
