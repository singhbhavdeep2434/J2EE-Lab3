

package com.carpoolBhav;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Servlet implementation class CarpoolServlet
 */
@WebServlet("/CarpoolSystem/CarpoolServlet")
public class CarpoolServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Logger to log lifecycle methods and requests
	private static final Logger logger = Logger.getLogger(CarpoolServlet.class.getName());

	// List to store available rides (thread-safe)
	private List<String> availableRides;

    /**
     * Default constructor
     */
    public CarpoolServlet() {
        super();
    }

    /**
     * Servlet lifecycle method to initialize the availableRides list.
     */
    @Override
    public void init() throws ServletException {
        availableRides = new CopyOnWriteArrayList<>(); // Thread-safe list
        logger.info("CarpoolServlet initialized. List of available rides created.");
    }

    /**
     * Service method to log every incoming request (method type and URI).
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        logger.info("Received " + method + " request for: " + requestUri);
        super.service(request, response);
    }

    /**
     * Servlet lifecycle method to clean up resources.
     */
    @Override
    public void destroy() {
        logger.info("CarpoolServlet is being destroyed. Cleaning up resources.");
        super.destroy();
    }

	/**
	 * Handles GET requests to display available rides and a form to offer a new ride.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// Display the list of available rides
		out.println("<html><body>");
		out.println("<h2>Available Carpool Rides</h2>");
		if (availableRides.isEmpty()) {
			out.println("<p>No rides available.</p>");
		} else {
			out.println("<ul>");
			for (String ride : availableRides) {
				out.println("<li>" + ride + "</li>");
			}
			out.println("</ul>");
		}

		// Display form to offer a new ride
		out.println("<h3>Offer a Carpool Ride</h3>");
		out.println("<form method='POST' action='/CarpoolSystem/CarpoolServlet'>");
		out.println("Starting Location: <input type='text' name='start'><br>");
		out.println("Destination: <input type='text' name='destination'><br>");
		out.println("Seats Available: <input type='number' name='seats'><br>");
		out.println("<input type='submit' value='Offer Ride'>");
		out.println("</form>");
		out.println("</body></html>");
	}

	/**
	 * Handles POST requests to add a new ride to the list.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve form parameters
		String start = request.getParameter("start");
		String destination = request.getParameter("destination");
		String seats = request.getParameter("seats");

		// Format and add the new ride to the list
		String ride = "Ride from: " + start + " to: " + destination + " - " + seats + " seats available";
		availableRides.add(ride);

		// Redirect back to the GET request to display the updated list of rides
		response.sendRedirect("/CarpoolSystem/CarpoolServlet");
	}
}
