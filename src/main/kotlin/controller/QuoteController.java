package controller;

import java.util.Arrays;

import constant.Command;
import exception.InvalidCommandException;
import exception.InvalidNumberException;
import exception.QuoteNotFoundException;
import exception.QuotesFileAccessException;
import model.Quote;
import service.QuoteService;
import view.QuoteView;

public class QuoteController {
    private final QuoteView quoteView;
    private final QuoteService quoteService;
    
    public QuoteController(QuoteView quoteView, QuoteService quoteService) {
        this.quoteView = quoteView;
        this.quoteService = quoteService;
    }
    
    public void run() {
        quoteView.showTitle();
        
        while (true) {
            String command = quoteView.requestCommand().trim();
            
            try {
                validateCommand(command);
            } catch (InvalidCommandException e) {
                quoteView.displayErrorMessage(e.getMessage());
                continue;
            }
            
            if(isExit(command)) { break; }
            processCommand(command);
        }
        
        quoteView.closeScanner();
    }
    
    private void validateCommand(String targetCommand) throws InvalidCommandException {
        if (Arrays.stream(Command.values())
                .map(Command::getValue)
                .noneMatch(value -> value.equals(targetCommand))) {
            throw new InvalidCommandException(targetCommand);
        }
    }
    
    private boolean isExit(String command) {
        return command.equals(Command.EXIT.getValue());
    }
    
    private void processCommand(String command) {
        if (command.equals(Command.ADD.getValue())) {
            handleAdd();
        }
        if (command.equals(Command.DELETE.getValue())) {
            handleDelete();
        }
        if (command.equals(Command.UPDATE.getValue())) {
            handleUpdate();
        }
        if (command.equals(Command.LIST.getValue())) {
            handleList();
        }
        if (command.equals(Command.BUILD.getValue())) {
            handleBuild();
        }
    }
    
    private void handleAdd() {
        try {
            String[] ContentAndAuthor = quoteView.requestRegister();
            int newId = quoteService.addQuote(ContentAndAuthor[1], ContentAndAuthor[0]);
            quoteView.alertSuccess(newId, Command.ADD);
        } catch (QuotesFileAccessException e) {
            quoteView.displayErrorMessage(e.getMessage());
        }
    }
    
    private void handleDelete() {
        try {
            int targetId = parseToIntId(quoteView.requestTargetId(Command.DELETE));
            quoteService.deleteQuote(quoteService.getQuoteById(targetId));
            quoteView.alertSuccess(targetId, Command.DELETE);
        } catch (InvalidNumberException | QuoteNotFoundException | QuotesFileAccessException e) {
            quoteView.displayErrorMessage(e.getMessage());
        }
    }
    
    private void handleUpdate() {
        try {
            int targetId = parseToIntId(quoteView.requestTargetId(Command.UPDATE));
            Quote targetQuote = quoteService.getQuoteById(targetId);
            String[] newContentAndAuthor = quoteView.requestUpdate(targetQuote.getContentAndAuthor());
            quoteService.updateQuote(targetQuote, newContentAndAuthor[1], newContentAndAuthor[0]);
        } catch (InvalidNumberException | QuoteNotFoundException | QuotesFileAccessException e) {
            quoteView.displayErrorMessage(e.getMessage());
        }
    }
    
    private void handleList() {
        try {
            quoteView.displayQuotes(quoteService.listQuotes().reversed());
        } catch (QuotesFileAccessException e) {
            quoteView.displayErrorMessage(e.getMessage());
        }
    }
    
    private void handleBuild() {
        try {
            quoteService.buildQuotes();
            quoteView.alertSuccess();
        } catch (QuotesFileAccessException e) {
            quoteView.displayErrorMessage(e.getMessage());
        }
    }
    
    private int parseToIntId(String input) throws InvalidNumberException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidNumberException("null");
        }
        
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new InvalidNumberException(input);
        }
    }
    
}