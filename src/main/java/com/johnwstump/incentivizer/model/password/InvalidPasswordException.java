package com.johnwstump.incentivizer.model.password;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvalidPasswordException extends Exception {
    private final List<String> messages;

    public InvalidPasswordException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public String getCombinedMessage() {
        if (messages == null) {
            return "";
        }

        Set<String> uniqueMessages = new HashSet<>(messages);
        StringBuilder errorMessage = new StringBuilder();

        for (String message : uniqueMessages) {
            errorMessage.append(message.trim());
            errorMessage.append("\r\n");
        }

        return errorMessage.toString();
    }
}
