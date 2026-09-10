package net.ritasister.dc.api.service;


import java.util.List;

/**
 * Messaging service interface for sending messages and formatting text.
 *
 * @param <T> the type of the message sender
 */
public interface MessagingService<T> {

    /**
     * Sends a message to the specified sender.
     *
     * @param sender  the message sender
     * @param message the message to send
     */
    void sendMessage(T sender, String message);

    /**
     * Formats color codes in the given text.
     *
     * @param text the text to format
     * @return the formatted text with color codes applied
     */
    String formatColors(String text);

    /**
     * Formats color codes in a list of texts.
     *
     * @param text the list of texts to format
     * @return the list of formatted texts with color codes applied
     */
    List<String> formatColors(List<String> text);
}