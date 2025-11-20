package app;

import view.MainMenu;

/**
 * Main entry point for the Internship Management System application.
 * Initializes and starts the main menu interface for user interaction with the system.
 * This application manages internship postings, student applications, and career center operations.
 *
 * @version 1.0
 */
public class InternshipApp {
    /**
     * Main method that serves as the application entry point.
     * Creates and starts the main menu interface.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start();
    }
}
